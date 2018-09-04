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
import com.tct.cache.UserOnlineSessionCache;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.codec.pojo.ClientDeviceBindingMessage;
import com.tct.codec.pojo.ClientDeviceBindingReplyBody;
import com.tct.codec.pojo.ClientDeviceBindingReplyMessage;
import com.tct.codec.pojo.ServerDeviceBindingBody;
import com.tct.codec.pojo.ServerDeviceBindingReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.jms.producer.WebTopicSender;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientDeviceBindingService")
public class ClientDeviceBindingServiceImpl implements SimpleService {

	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
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
		
		boolean flag = false;
		
		ClientDeviceBindingMessage message = (ClientDeviceBindingMessage)msg;
		
		ConcurrentHashMap<String, String> deviceNoBingingWebUserCache = DeviceNoBingingWebUserCache.getDeviceNoWebUserHashMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();					

		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		if (Integer.parseInt(message.getMessageBody().getReserve())==1) {
			
		    //20180904 0724 luochengcong modified 当服务器收到上传绑定消息之后，如果数据库中不存在记录则往数据库中插入数据
			if(deviceGunCustom==null) {
				String deviceNo = (String)StringUtil.getKey(userOnlineSessionCache, message.getMessageBody().getAuthCode());
				deviceGunCustom=new DeviceGunCustom();
				deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
				deviceGunCustom.setCreateTime(StringUtil.getDate(message.getMessageBody().getBindTime()));
				deviceGunCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getBindTime()));
				deviceGunCustom.setDeviceNo(deviceNo);
				deviceGunCustom.setState(0);
				deviceGunCustom.setOutWarehouseTime(StringUtil.getDate(message.getMessageBody().getBindTime()));
				deviceGunCustomMapper.insertSelective(deviceGunCustom);
			}
			
			DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
			deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
			deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
			deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
			deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
			deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
			
			GunCustom gunCustom = new GunCustom();
			gunCustom.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getBindTime()));
			gunCustom.setState(Integer.valueOf(0));
			gunCustom.setRealTimeState(Integer.valueOf(0));
			flag = clientDeviceBindingDao.updateDeviceBindingState(deviceLocationCustom, gunCustom);
			
			//改成由服务器端插入后 20180904 0726 luochengcong 数据库中记录已经插入不需要再修改状态
			//deviceGunCustom.setState(Integer.valueOf(0));
			//deviceGunCustomMapper.updateByDeviceGunCustom(deviceGunCustom);
			
			//发送返回消息到客户端并且通知web前端绑定成功，枪支出库
			ClientDeviceBindingReplyMessage clientDeviceBindingReplyMessage = new ClientDeviceBindingReplyMessage();
			ClientDeviceBindingReplyBody clientDeviceBindingReplyBody = new ClientDeviceBindingReplyBody();
			clientDeviceBindingReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientDeviceBindingReplyBody.setReserve(Integer.toString(1));
			clientDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
			clientDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
			clientDeviceBindingReplyMessage.setMessageBody(clientDeviceBindingReplyBody);
			clientDeviceBindingReplyMessage.setMessageType("08");
			clientDeviceBindingReplyMessage.setSendTime(StringUtil.getDateString());
			clientDeviceBindingReplyMessage.setSerialNumber(message.getSerialNumber());
			clientDeviceBindingReplyMessage.setServiceType(message.getServiceType());
			clientDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientDeviceBindingReplyMessage, simpleReplyMessage);
			String replyBody = StringConstant.MSG_BODY_PREFIX+clientDeviceBindingReplyBody.getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+clientDeviceBindingReplyBody.getAuthCode()
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String bingJson = JSONObject.toJSONString(simpleReplyMessage);
			
			outQueueSender.sendMessage(outQueueDestination, bingJson);

			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
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
			serverDeviceBindingReplyMessage.setUserName(deviceNoBingingWebUserCache.get(deviceGunCustom.getDeviceNo()));
			
			String serverbingJson = JSONObject.toJSONString(serverDeviceBindingReplyMessage);
			webTopicSender.sendMessage(webtopicDestination, serverbingJson);
			deviceNoBingingWebUserCache.remove(deviceGunCustom.getDeviceNo());

			//webOutQueueSender.sendMessage(webOutQueueDestination, serverbingJson);	
			flag = true;
		}else {
			
			DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
			deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
			deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
			deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
			deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
			deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
			
			GunCustom gunCustom = new GunCustom();
			gunCustom.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getBindTime()));
			gunCustom.setState(Integer.valueOf(1));
			gunCustom.setRealTimeState(Integer.valueOf(1));
			flag = clientDeviceBindingDao.updateDeviceBindingState(deviceLocationCustom, gunCustom);

			//改成由服务器处理出库消息后，当失败时将不再插入数据，也不修改数据20180904 0727
			//deviceGunCustom.setState(Integer.valueOf(1));
			//deviceGunCustomMapper.updateByDeviceGunCustom(deviceGunCustom);
			
			//发送返回消息到客户端并且通知web前端绑定成功，枪支出库
			ClientDeviceBindingReplyMessage clientDeviceBindingReplyMessage = new ClientDeviceBindingReplyMessage();
			ClientDeviceBindingReplyBody clientDeviceBindingReplyBody = new ClientDeviceBindingReplyBody();
			clientDeviceBindingReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientDeviceBindingReplyBody.setReserve(Integer.toString(0));
			clientDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
			clientDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
			clientDeviceBindingReplyMessage.setMessageBody(clientDeviceBindingReplyBody);
			clientDeviceBindingReplyMessage.setMessageType("08");
			clientDeviceBindingReplyMessage.setSendTime(StringUtil.getDateString());
			clientDeviceBindingReplyMessage.setSerialNumber(message.getSerialNumber());
			clientDeviceBindingReplyMessage.setServiceType(message.getServiceType());
			clientDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
			
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientDeviceBindingReplyMessage, simpleReplyMessage);
			String replyBody = StringConstant.MSG_BODY_PREFIX+clientDeviceBindingReplyBody.getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+clientDeviceBindingReplyBody.getAuthCode()
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String bingJson = JSONObject.toJSONString(simpleReplyMessage);	
			outQueueSender.sendMessage(outQueueDestination, bingJson);
	
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerDeviceBindingReplyMessage serverDeviceBindingReplyMessage = new ServerDeviceBindingReplyMessage();
			ServerDeviceBindingBody serverDeviceBindingBody =  new ServerDeviceBindingBody();
			serverDeviceBindingBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverDeviceBindingBody.setGunTag(gunCustom2.getGunTag());
			serverDeviceBindingBody.setState(Integer.toString(0));
			serverDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
			serverDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
			serverDeviceBindingReplyMessage.setMessageBody(serverDeviceBindingBody);
			serverDeviceBindingReplyMessage.setMessageType("08");
			serverDeviceBindingReplyMessage.setSendTime(message.getSendTime());
			serverDeviceBindingReplyMessage.setSerialNumber(message.getSerialNumber());
			serverDeviceBindingReplyMessage.setServiceType(message.getServiceType());
			serverDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
			serverDeviceBindingReplyMessage.setUserName(deviceNoBingingWebUserCache.get(deviceGunCustom.getDeviceNo()));

			String serverbingJson = JSONObject.toJSONString(serverDeviceBindingReplyMessage);
			webTopicSender.sendMessage(webtopicDestination, serverbingJson);
			deviceNoBingingWebUserCache.remove(deviceGunCustom.getDeviceNo());
			//webOutQueueSender.sendMessage(webOutQueueDestination, serverbingJson);
			
		}
		return flag;
	}

}
