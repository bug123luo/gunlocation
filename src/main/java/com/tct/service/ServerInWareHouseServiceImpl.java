package com.tct.service;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ServerInWareHouseMessage;
import com.tct.dao.ServerInWareHouseDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverInWareHouseService")
public class ServerInWareHouseServiceImpl implements ServerInWareHouseService {

	@Autowired
	ServerInWareHouseDao serverInWareHouseDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ServerInWareHouseMessage message = (ServerInWareHouseMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		/*//数据库中获取需要通知的终端的 deviceNo信息
		DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getServerInWareHouseBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		DeviceGunCustom deviceGunCustom2 = serverInWareHouseDao.selectByDeviceGunQueryVo(deviceGunQueryVo);*/
		
		//创建发送到终端队列的队列名
		Hashtable<String , String> userQueueMap=null;
		if (userOnlineQueueHashMap.containsKey(message.getMessageBody().getDeviceNo())) {
			userQueueMap=userOnlineQueueHashMap.get(message.getMessageBody().getDeviceNo());
		}
		if(userQueueMap==null) {
			userQueueMap=new Hashtable<String,String>();
		}
		userQueueMap.put("sendQueue", message.getMessageBody().getDeviceNo());
		
		userOnlineQueueHashMap.put(message.getMessageBody().getDeviceNo(), userQueueMap);	
		
		//将接收到的消息放在本地的接收消息队列上
		Hashtable<String, Object> messageMap=null;
		if (unhandlerReceiveMessageHashMap.containsKey(message.getMessageBody().getDeviceNo())) {
			messageMap= unhandlerReceiveMessageHashMap.get(message.getMessageBody().getDeviceNo());
		}
		if(messageMap ==null) {
			messageMap=new Hashtable<String,Object>();
		}
		
		messageMap.put("s"+message.getSerialNumber(), message);		
		unhandlerReceiveMessageHashMap.put(message.getMessageBody().getDeviceNo(), messageMap);
		
		//发送到producer处理队列上
		String outWareJson = JSONObject.toJSONString(message);

		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unhandlerReceiveMessageHashMap.containsKey(message.getMessageBody().getDeviceNo())) {
			tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(message.getMessageBody().getDeviceNo());
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), outWareJson);
		unSendReplyMessageHashMap.put(message.getMessageBody().getDeviceNo(), tempUnSendReplyMessageMap);

		return true;
	}

}
