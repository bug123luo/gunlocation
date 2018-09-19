package com.tct.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.collections.MappingChange.Map;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ClientOutWareHouseMessage;
import com.tct.codec.pojo.ClientOutWareHouseReplyBody;
import com.tct.codec.pojo.ClientOutWareHouseReplyMessage;
import com.tct.codec.pojo.ServerDeviceBindingBody;
import com.tct.codec.pojo.ServerDeviceBindingReplyMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.AuthCodeDao;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.jms.producer.WebTopicSender;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.mapper.WatchDeviceCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.po.WatchDeviceCustom;
import com.tct.po.WatchDeviceQueryVo;
import com.tct.service.BluetoothPara;
import com.tct.service.SimpleService;
import com.tct.service.SpringContextUtil;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientOutWareHouseService")
public class ClientOutWareHouseServiceImpl implements SimpleService {

	@Autowired
	@Qualifier("stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	@Qualifier("jedisTemplate")
	private RedisTemplate<String,Map<String, ?>> jedisTemplate;
	
	@Autowired
	AuthCodeDao authcodeDao;
	
	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Autowired
	WatchDeviceCustomMapper watchDeviceCustomMapper;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
	
	@Resource
	private OutQueueSender outQueueSender;
	
	@Resource
	private WebOutQueueSender webOutQueueSender;
	
	@Resource
	private WebTopicSender webTopicSender;
	
	@Resource
	@Qualifier("topicDestination")
	private Destination webtopicDestination;
	
	@Resource
	@Qualifier("outQueueDestination")
	private Destination outQueueDestination;
	
	@Resource
	@Qualifier("webOutQueueDestination")
	private Destination webOutQueueDestination;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		
		ClientOutWareHouseMessage message = (ClientOutWareHouseMessage)msg;
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		//根据用户名查询在线队列的人的名称
		WatchDeviceCustom watchDeviceCustom = new WatchDeviceCustom();
		watchDeviceCustom.setDeviceName(message.getMessageBody().getUsername());
		watchDeviceCustom.setPassword(message.getMessageBody().getCommand());
		WatchDeviceQueryVo watchDeviceQueryVo = new WatchDeviceQueryVo();
		watchDeviceQueryVo.setWatchDeviceCustom(watchDeviceCustom);
			
		//从手表设备表中查出对应的枪支号码，枪支mac地址，警员编号，密码
		WatchDeviceCustom watchDeviceCustom2 = watchDeviceCustomMapper.selectByWatchDeviceQueryVo(watchDeviceQueryVo);
		
		if (watchDeviceCustom2==null) {
			log.info("watch_device 中用户不存在，请先注册");
			return false;
		}
	
		//if(userOnlineSessionCache.get(watchDeviceCustom2.getDeviceNo())==null) {
		
		if(stringRedisTemplate.opsForValue().get(watchDeviceCustom2.getDeviceNo())==null) {
			log.info("用户未登陆，请先登录，再出库");
			return false;
		}
		
		int countBing = deviceGunCustomMapper.selectCount(watchDeviceCustom2);
		
		if(countBing >1) {
			log.info("枪支已经出库，请先入库再重新出库");
			return false;
		}
		
		//userOnlineSessionCache.put(watchDeviceCustom2.getDeviceNo(), message.getSessionToken());
		stringRedisTemplate.opsForValue().set(watchDeviceCustom2.getDeviceNo(), message.getSessionToken());
		jedisTemplate.opsForHash().put(StringConstant.SESSION_DEVICE_HASH, message.getSessionToken(), watchDeviceCustom2.getDeviceNo());
		GunQueryVo gunQueryVo = new GunQueryVo();
		GunCustom gunCustom = new GunCustom();
		gunCustom.setBluetoothMac(watchDeviceCustom2.getGunMac());
		gunQueryVo.setGunCustom(gunCustom);
		GunCustom gunCustom2 = gunCustomMapper.selectBybluetoothMac(gunQueryVo);
		if(gunCustom2==null) {
			log.info("枪支表中不存在该枪支");
			return false;
		}
		
		
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		deviceGunCustom.setDeviceNo(watchDeviceCustom2.getDeviceNo());
		deviceGunCustom.setGunMac(watchDeviceCustom2.getGunTag());
		deviceGunCustom.setGunMac(watchDeviceCustom2.getGunMac());
		deviceGunCustom.setOutWarehouseTime(StringUtil.getDate(message.getSendTime()));
		deviceGunCustom.setState(0);
		deviceGunCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		deviceGunCustomMapper.insertSelective(deviceGunCustom);
		
		//腕表直接绑定出库后直接执行绑定命令业务操作
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		deviceLocationCustom.setState(0);
		
		gunCustom.setBluetoothMac(watchDeviceCustom2.getGunMac());
		gunCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		gunCustom.setState(Integer.valueOf(0));
		gunCustom.setRealTimeState(Integer.valueOf(0));
		clientDeviceBindingDao.updateDeviceBindingState(deviceLocationCustom, gunCustom);
		
		//腕带出库的调试消息
		BluetoothPara bluetoothPara = SpringContextUtil.getBean("bluetoothPara");
		ClientOutWareHouseReplyBody clientOutWareHouseReplyBody =  new ClientOutWareHouseReplyBody();
		clientOutWareHouseReplyBody.setApplyTime(StringUtil.getDateString());
		clientOutWareHouseReplyBody.setBluetoothMac(watchDeviceCustom2.getGunMac());
		clientOutWareHouseReplyBody.setBroadcastInterval(bluetoothPara.getBroadcastInterval());
		clientOutWareHouseReplyBody.setConncetionInterval(bluetoothPara.getConncetionInterval());
		clientOutWareHouseReplyBody.setConnectionTimeout(bluetoothPara.getConnectionTimeout());
		clientOutWareHouseReplyBody.setDeadlineTime("1");
		clientOutWareHouseReplyBody.setGunTag(gunCustom2.getGunType()+"+"+gunCustom2.getGunModel()+"+"+watchDeviceCustom2.getGunTag());
		clientOutWareHouseReplyBody.setHeartbeat(bluetoothPara.getHeartbeat());
		clientOutWareHouseReplyBody.setMatchTime(bluetoothPara.getMatchTime());
		clientOutWareHouseReplyBody.setPowerAlarmLevel(bluetoothPara.getPowerAlarmLevel());
		clientOutWareHouseReplyBody.setPowerSampling(bluetoothPara.getPowerSampling());
		clientOutWareHouseReplyBody.setReserve("1");
		clientOutWareHouseReplyBody.setSafeCode("1");
		clientOutWareHouseReplyBody.setSoftwareversion(bluetoothPara.getSoftwareversion());
		clientOutWareHouseReplyBody.setSystemTime(StringUtil.getDateString());
		clientOutWareHouseReplyBody.setTransmittingPower(bluetoothPara.getTransmittingPower());
		
		ClientOutWareHouseReplyMessage clientOutWareHouseReplyMessage = new ClientOutWareHouseReplyMessage();
		clientOutWareHouseReplyMessage.setMessageBody(clientOutWareHouseReplyBody);
		clientOutWareHouseReplyMessage.setDeviceType(message.getDeviceType());
		clientOutWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
		clientOutWareHouseReplyMessage.setMessageType("06");
		clientOutWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
		clientOutWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
		clientOutWareHouseReplyMessage.setServiceType(message.getServiceType());
		clientOutWareHouseReplyMessage.setSessionToken(message.getSessionToken());
				
		SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
		BeanUtils.copyProperties(clientOutWareHouseReplyMessage, simpleReplyMessage);
		String replyBody=StringConstant.MSG_BODY_PREFIX+clientOutWareHouseReplyMessage.getMessageBody().getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getBluetoothMac()
				//+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getGunTag()
				+StringConstant.MSG_BODY_SEPARATOR+gunCustom2.getGunType()+"+"+gunCustom2.getGunModel()+"+"+watchDeviceCustom2.getGunTag()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getApplyTime()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getDeadlineTime()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getPowerAlarmLevel()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getTransmittingPower()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getBroadcastInterval()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getConncetionInterval()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getConnectionTimeout()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getSoftwareversion()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getHeartbeat()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getPowerSampling()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getSystemTime()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getMatchTime()
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getSafeCode()
				+StringConstant.MSG_BODY_SUFFIX;
		simpleReplyMessage.setMessageBody(replyBody);
		
		String strJson = JSONObject.toJSONString(simpleReplyMessage);
		log.info("Client Out WareHouse Message is send to  {}",deviceGunCustom.getDeviceNo());
		outQueueSender.sendMessage(outQueueDestination, strJson);
				
		ServerDeviceBindingReplyMessage serverDeviceBindingReplyMessage = new ServerDeviceBindingReplyMessage();
		ServerDeviceBindingBody serverDeviceBindingBody =  new ServerDeviceBindingBody();
		serverDeviceBindingBody.setDeviceNo(deviceGunCustom.getDeviceNo());
		serverDeviceBindingBody.setGunTag(gunCustom2.getGunTag());
		serverDeviceBindingBody.setState(Integer.toString(1));
		serverDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
		serverDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
		serverDeviceBindingReplyMessage.setMessageBody(serverDeviceBindingBody);
		serverDeviceBindingReplyMessage.setMessageType("08");
		serverDeviceBindingReplyMessage.setSendTime(message.getSendTime());
		serverDeviceBindingReplyMessage.setSerialNumber(message.getSerialNumber());
		serverDeviceBindingReplyMessage.setServiceType(message.getServiceType());
		serverDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
		serverDeviceBindingReplyMessage.setUserName("1");
		
		String serverbingJson = JSONObject.toJSONString(serverDeviceBindingReplyMessage);
		log.info("The {} Client Out WareHouse Message is send to WebServer",deviceGunCustom.getDeviceNo());
		webTopicSender.sendMessage(webtopicDestination, serverbingJson);
		return true;
	}

}
