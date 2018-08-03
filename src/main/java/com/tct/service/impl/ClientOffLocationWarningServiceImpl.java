package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ClientOffLocationWarningMessage;
import com.tct.codec.pojo.ClientOffLocationWarningReplyBody;
import com.tct.codec.pojo.ClientOffLocationWarningReplyMessage;
import com.tct.codec.pojo.ServerInWareHouseReplyBody;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.dao.ClientOffLocationWarningDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.po.SosMessageCustom;
import com.tct.service.ClientOffLocationWarningService;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientOffLocationWarningService")
public class ClientOffLocationWarningServiceImpl implements ClientOffLocationWarningService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientOffLocationWarningDao clientOffLocationWarningDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientOffLocationWarningMessage message = (ClientOffLocationWarningMessage)msg;
		
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		//创建发送到终端队列的队列名
		Hashtable<String , String> userQueueMap=null;
		if (userOnlineQueueHashMap.containsKey(deviceGunCustom.getDeviceNo())) {
			userQueueMap=userOnlineQueueHashMap.get(deviceGunCustom.getDeviceNo());
		}
		if(userQueueMap==null) {
			userQueueMap=new Hashtable<String,String>();
		}
		userQueueMap.put("sendQueue", message.getSessionToken());
		
		userOnlineQueueHashMap.put(deviceGunCustom.getDeviceNo(), userQueueMap);	
		
		//将接收到的消息放在本地的接收消息队列上
		Hashtable<String, Object> messageMap=null;
		if (unhandlerReceiveMessageHashMap.containsKey(message.getSessionToken())) {
			messageMap= unhandlerReceiveMessageHashMap.get(message.getSessionToken());
		}
		if(messageMap ==null) {
			messageMap=new Hashtable<String,Object>();
		}
		
		messageMap.put(message.getSerialNumber(), message);		
		unhandlerReceiveMessageHashMap.put(message.getSessionToken(), messageMap);
		
		//插入device_location表，插入sos_message表，更新 gun表状态
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		
		GunCustom gunCustom = new GunCustom();
		gunCustom.setBluetoothMac(message.getMessageBody().getBluetoothMac());
		gunCustom.setRealTimeState(0);
		gunCustom.setWarehouseId(Integer.valueOf(message.getMessageBody().getAreaCode()));
		
		SosMessageCustom sosMessageCustom =  new SosMessageCustom();
		sosMessageCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		sosMessageCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		sosMessageCustom.setLatitude(message.getMessageBody().getLa());
		sosMessageCustom.setLongitude(message.getMessageBody().getLo());
		sosMessageCustom.setSosTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setFinallyTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setState(0);
		
		boolean flag=clientOffLocationWarningDao.updateClientOffLocationWaring(deviceLocationCustom, gunCustom, sosMessageCustom);
		
		if(flag) {
			ClientOffLocationWarningReplyMessage clientOffLocationWarningReplyMessage = new ClientOffLocationWarningReplyMessage();
			ClientOffLocationWarningReplyBody clientOffLocationWarningReplyBody = new ClientOffLocationWarningReplyBody();
			clientOffLocationWarningReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientOffLocationWarningReplyBody.setReserve(Integer.toString(0));
			clientOffLocationWarningReplyMessage.setDeviceType(message.getDeviceType());
			clientOffLocationWarningReplyMessage.setFormatVersion(message.getFormatVersion());
			clientOffLocationWarningReplyMessage.setMessageBody(clientOffLocationWarningReplyBody);
			clientOffLocationWarningReplyMessage.setMessageType("18");
			clientOffLocationWarningReplyMessage.setSendTime(StringUtil.getDateString());
			clientOffLocationWarningReplyMessage.setSerialNumber(message.getSerialNumber());
			clientOffLocationWarningReplyMessage.setServiceType(message.getServiceType());
			clientOffLocationWarningReplyMessage.setSessionToken(message.getSessionToken());
			
			String msgJson = JSONObject.toJSONString(clientOffLocationWarningReplyMessage);
			//将APP回应消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unhandlerReceiveMessageHashMap.containsKey(message.getSessionToken())) {
				tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(message.getSessionToken());
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), msgJson);
			unSendReplyMessageHashMap.put(message.getSessionToken(), tempUnSendReplyMessageMap);
			
			//将web端回应消息放进消息缓存队列
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage = new ServerInWareHouseReplyMessage();
			ServerInWareHouseReplyBody serverInWareHouseReplyBody =  new ServerInWareHouseReplyBody();
			serverInWareHouseReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverInWareHouseReplyBody.setGunTag(gunCustom2.getGunTag());
			serverInWareHouseReplyBody.setState(Integer.toString(2));

			serverInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			serverInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			serverInWareHouseReplyMessage.setMessageBody(serverInWareHouseReplyBody);
			serverInWareHouseReplyMessage.setMessageType("18");
			serverInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			serverInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());
			serverInWareHouseReplyMessage.setServiceType(message.getServiceType());
			
			String serverInWareHouseReplyJson = JSONObject.toJSONString(serverInWareHouseReplyMessage);
			
			if(unhandlerReceiveMessageHashMap.containsKey("WebOutQueue")) {
				tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get("WebOutQueue");
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), serverInWareHouseReplyJson);
			unSendReplyMessageHashMap.put("WebOutQueue", tempUnSendReplyMessageMap);
			flag = true;
		}
		
		
		return false;
	}

}
