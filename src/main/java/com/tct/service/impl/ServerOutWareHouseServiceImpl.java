package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ServerOutWareHouseMessage;
import com.tct.dao.ServerOutWareHouseDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.service.ServerOutWareHouseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOutWareHouseService")
public class ServerOutWareHouseServiceImpl implements ServerOutWareHouseService {
		
	@Autowired
	ServerOutWareHouseDao serverOutWareHouseDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ServerOutWareHouseMessage message = (ServerOutWareHouseMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		//获取需要通知的终端的 deviceNo信息
		DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		DeviceGunCustom deviceGunCustom2 = serverOutWareHouseDao.selectByDeviceGunQueryVo(deviceGunQueryVo);
			
		String sessionToken = "";
		if(userOnlineQueueHashMap.contains(deviceGunCustom2.getDeviceNo())) {
			sessionToken = userOnlineQueueHashMap.get(deviceGunCustom2.getDeviceNo()).get("sendQueue");
		}
		
		//将接收到的消息放在本地的接收消息队列上
		Hashtable<String, Object> messageMap=null;
		if (unhandlerReceiveMessageHashMap.containsKey(sessionToken)) {
			messageMap= unhandlerReceiveMessageHashMap.get(sessionToken);
		}
		if(messageMap ==null) {
			messageMap=new Hashtable<String,Object>();
		}
		
		messageMap.put("s"+message.getSerialNumber(), message);		
		unhandlerReceiveMessageHashMap.put(sessionToken, messageMap);
		
		//发送到producer处理队列上
		String outWareJson = JSONObject.toJSONString(message);

		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unhandlerReceiveMessageHashMap.containsKey(sessionToken)) {
			tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(sessionToken);
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), outWareJson);
		unSendReplyMessageHashMap.put(sessionToken, tempUnSendReplyMessageMap);
		
		return true;
	}

}
