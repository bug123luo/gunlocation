package com.tct.service.impl;


import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ClientDeviceBindingMessage;
import com.tct.codec.pojo.ClientDeviceBindingReplyBody;
import com.tct.codec.pojo.ClientDeviceBindingReplyMessage;
import com.tct.codec.pojo.ServerDeviceBindingBody;
import com.tct.codec.pojo.ServerDeviceBindingReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.ClientDeviceBindingService;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientDeviceBindingService")
public class ClientDeviceBindingServiceImpl implements ClientDeviceBindingService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientDeviceBindingMessage message = (ClientDeviceBindingMessage)msg;
		
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
		userQueueMap.put("sendQueue", deviceGunCustom.getDeviceNo());
		
		userOnlineQueueHashMap.put(deviceGunCustom.getDeviceNo(), userQueueMap);	
		
		//将接收到的消息放在本地的接收消息队列上
		Hashtable<String, Object> messageMap=null;
		if (unhandlerReceiveMessageHashMap.containsKey(deviceGunCustom.getDeviceNo())) {
			messageMap= unhandlerReceiveMessageHashMap.get(deviceGunCustom.getDeviceNo());
		}
		if(messageMap ==null) {
			messageMap=new Hashtable<String,Object>();
		}
		
		messageMap.put("s"+message.getSerialNumber(), message);		
		unhandlerReceiveMessageHashMap.put(deviceGunCustom.getDeviceNo(), messageMap);
		
		
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		
		GunCustom gunCustom = new GunCustom();
		gunCustom.setBluetoothMac(message.getMessageBody().getBluetoothMac());
		gunCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getBindTime()));
		gunCustom.setRealTimeState(Integer.valueOf(0));
		boolean flag = clientDeviceBindingDao.updateDeviceBindingState(deviceLocationCustom, gunCustom);
		
		if (flag) {
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
			
			String bingJson = JSONObject.toJSONString(clientDeviceBindingReplyMessage);
			//将回应APP消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unhandlerReceiveMessageHashMap.containsKey(deviceGunCustom.getDeviceNo())) {
				tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(deviceGunCustom.getDeviceNo());
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), bingJson);
			unSendReplyMessageHashMap.put(deviceGunCustom.getDeviceNo(), tempUnSendReplyMessageMap);
			
			//将回应消息发送到Web前端队列
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
			
			String serverbingJson = JSONObject.toJSONString(clientDeviceBindingReplyMessage);
			
			if(unhandlerReceiveMessageHashMap.containsKey("WebOutQueue")) {
				tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get("WebOutQueue");
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), serverbingJson);
			unSendReplyMessageHashMap.put("WebOutQueue", tempUnSendReplyMessageMap);
			
			flag = true;
		}else {
			flag =  false;
		}
		return flag;
	}

}
