package com.tct.service.impl;

import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ClientInWareHouseMessage;
import com.tct.codec.pojo.ClientInWareHouseReplyBody;
import com.tct.codec.pojo.ClientInWareHouseReplyMessage;
import com.tct.codec.pojo.ServerInWareHouseBody;
import com.tct.codec.pojo.ServerInWareHouseReplyBody;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.dao.ClientInWareHouseDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.ClientInWareHouseService;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientInWareHouseService")
public class ClientInWareHouseServiceImpl implements ClientInWareHouseService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientInWareHouseDao clientInWareHouseDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
		
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientInWareHouseMessage message = (ClientInWareHouseMessage)msg;
		
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
		
		
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		DeviceGunCustom deviceGunCustom2 = new DeviceGunCustom();
		deviceGunCustom2.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunCustom2.setInWarehouseTime(new Date());
		deviceGunCustom2.setState(1);
		
		boolean flag = clientInWareHouseDao.updateDeviceInWareHouseState(deviceLocationCustom, deviceGunCustom);
		if (flag) {
			ClientInWareHouseReplyMessage clientInWareHouseReplyMessage =  new ClientInWareHouseReplyMessage();
			ClientInWareHouseReplyBody clientInWareHouseReplyBody = new ClientInWareHouseReplyBody();
			clientInWareHouseReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientInWareHouseReplyBody.setReserve(Integer.toString(0));
			
			clientInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			clientInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			clientInWareHouseReplyMessage.setMessageType("12");
			clientInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			clientInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			clientInWareHouseReplyMessage.setServiceType(message.getServiceType());
			clientInWareHouseReplyMessage.setMessageBody(clientInWareHouseReplyBody);
			
			String clientInWareHouseReplyjson = JSONObject.toJSONString(clientInWareHouseReplyMessage);
			//将APP回应消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unhandlerReceiveMessageHashMap.containsKey(message.getSessionToken())) {
				tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(message.getSessionToken());
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), clientInWareHouseReplyjson);
			unSendReplyMessageHashMap.put(message.getSessionToken(), tempUnSendReplyMessageMap);
			
			
			//将向服务器的发送消息放在缓存队列中
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage =  new ServerInWareHouseReplyMessage();
			ServerInWareHouseReplyBody serverInWareHouseReplyBody = new ServerInWareHouseReplyBody();
			serverInWareHouseReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverInWareHouseReplyBody.setGunTag(gunCustom2.getGunTag());
			serverInWareHouseReplyBody.setState(Integer.toString(0));
			
			serverInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			serverInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			serverInWareHouseReplyMessage.setMessageType("12");
			serverInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			serverInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			serverInWareHouseReplyMessage.setServiceType(message.getServiceType());
			serverInWareHouseReplyMessage.setMessageBody(serverInWareHouseReplyBody);
			
			String serverInWareReplyJson = JSONObject.toJSONString(serverInWareHouseReplyMessage);
			
			if(unhandlerReceiveMessageHashMap.containsKey("WebOutQueue")) {
				tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get("WebOutQueue");
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), serverInWareReplyJson);
			unSendReplyMessageHashMap.put("WebOutQueue", tempUnSendReplyMessageMap);
			
			flag = true;
		}else {
			flag = false;
		}
		
		return false;
	}

}
