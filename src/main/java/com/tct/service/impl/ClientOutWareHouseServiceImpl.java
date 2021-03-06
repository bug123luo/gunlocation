package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ClientOutWareHouseMessage;
import com.tct.codec.pojo.ClientOutWareHouseReplyBody;
import com.tct.codec.pojo.ClientOutWareHouseReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.AuthCodeDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.WatchDeviceCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.WatchDeviceCustom;
import com.tct.po.WatchDeviceQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;



@Service(value="clientOutWareHouseService")
public class ClientOutWareHouseServiceImpl implements SimpleService {

	@Autowired
	AuthCodeDao authcodeDao;
	
	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Autowired
	WatchDeviceCustomMapper watchDeviceCustomMapper;
	
	@Resource
	private OutQueueSender outQueueSender;
	
	@Resource
	private WebOutQueueSender webOutQueueSender;
	
	@Resource
	@Qualifier("outQueueDestination")
	private Destination outQueueDestination;
	
	@Resource
	@Qualifier("webOutQueueDestination")
	private Destination webOutQueueDestination;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		
		ClientOutWareHouseMessage message = (ClientOutWareHouseMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		//根据用户名查询在线队列的人的名称
		WatchDeviceCustom watchDeviceCustom = new WatchDeviceCustom();
		watchDeviceCustom.setDeviceName(message.getMessageBody().getUsername());
		watchDeviceCustom.setPassword(message.getMessageBody().getCommand());
		WatchDeviceQueryVo watchDeviceQueryVo = new WatchDeviceQueryVo();
		watchDeviceQueryVo.setWatchDeviceCustom(watchDeviceCustom);
			
		//从手表设备表中查出对应的枪支号码，枪支mac地址，警员编号，密码
		WatchDeviceCustom watchDeviceCustom2 = watchDeviceCustomMapper.selectByWatchDeviceQueryVo(watchDeviceQueryVo);
	
		userOnlineSessionCache.put(watchDeviceCustom2.getDeviceNo(), message.getSessionToken());
		
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		deviceGunCustom.setDeviceNo(watchDeviceCustom.getDeviceNo());
		deviceGunCustom.setGunMac(watchDeviceCustom2.getGunTag());
		deviceGunCustom.setGunMac(watchDeviceCustom2.getGunMac());
		deviceGunCustom.setOutWarehouseTime(StringUtil.getDate(message.getSendTime()));
		deviceGunCustom.setState(0);
		deviceGunCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		deviceGunCustomMapper.updateByDeviceGunCustom(deviceGunCustom);
		
		ClientOutWareHouseReplyBody clientOutWareHouseReplyBody =  new ClientOutWareHouseReplyBody();
		clientOutWareHouseReplyBody.setApplyTime(StringUtil.getDateString());
		clientOutWareHouseReplyBody.setBluetoothMac(watchDeviceCustom2.getGunMac());
		clientOutWareHouseReplyBody.setBroadcastInterval("");
		clientOutWareHouseReplyBody.setConncetionInterval("");
		clientOutWareHouseReplyBody.setConnectionTimeout("");
		clientOutWareHouseReplyBody.setDeadlineTime("");
		clientOutWareHouseReplyBody.setGunTag(watchDeviceCustom2.getGunTag());
		clientOutWareHouseReplyBody.setHeartbeat("");
		clientOutWareHouseReplyBody.setMatchTime("");
		clientOutWareHouseReplyBody.setPowerAlarmLevel("");
		clientOutWareHouseReplyBody.setPowerSampling("");
		clientOutWareHouseReplyBody.setReserve("");
		clientOutWareHouseReplyBody.setSafeCode("");
		clientOutWareHouseReplyBody.setSoftwareversion("");
		clientOutWareHouseReplyBody.setSystemTime(StringUtil.getDateString());
		clientOutWareHouseReplyBody.setTransmittingPower("");
		
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
				+StringConstant.MSG_BODY_SEPARATOR+clientOutWareHouseReplyMessage.getMessageBody().getGunTag()
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
		outQueueSender.sendMessage(outQueueDestination, strJson);
		//将回应消息放进消息缓存队列中
/*		String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
			tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put(message.getSerialNumber(), strJson);
		unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);*/
		
		return true;
	}

}
