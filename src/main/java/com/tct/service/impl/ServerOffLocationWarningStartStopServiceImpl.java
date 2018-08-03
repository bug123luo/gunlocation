package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopMessage;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.service.ServerOffLocationWarningStartStopService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service(value="serverOffLocationWarningStartStopService")
public class ServerOffLocationWarningStartStopServiceImpl implements ServerOffLocationWarningStartStopService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		
		ServerOffLocationWarningStartStopMessage message = (ServerOffLocationWarningStartStopMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		//查找枪支的 deviceNo
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		//创建发送到终端队列的队列名
		Hashtable<String , String> userQueueMap=null;
		if (userOnlineQueueHashMap.containsKey(deviceGunCustom.getDeviceNo())) {
			userQueueMap=userOnlineQueueHashMap.get(deviceGunCustom.getDeviceNo());
		}else {
			log.info("需要停止用户不在线");
			return false;
		}
		
		//将接收到的消息放在本地的接收消息队列上
		Hashtable<String, Object> messageMap=null;
		if (unhandlerReceiveMessageHashMap.containsKey(userQueueMap.get("sendQueue"))) {
			messageMap= unhandlerReceiveMessageHashMap.get(userQueueMap.get("sendQueue"));
		}
		if(messageMap ==null) {
			messageMap=new Hashtable<String,Object>();
		}
		
		messageMap.put("s"+message.getSerialNumber(), message);		
		unhandlerReceiveMessageHashMap.put(userQueueMap.get("sendQueue"), messageMap);
				
		String startStopJson = JSONObject.toJSONString(message);
		//将回应消息放进消息缓存队列中
		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unhandlerReceiveMessageHashMap.containsKey(userQueueMap.get("sendQueue"))) {
			tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(userQueueMap.get("sendQueue"));
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), startStopJson);
		unSendReplyMessageHashMap.put(userQueueMap.get("sendQueue"), tempUnSendReplyMessageMap);
		return true;
	}

}
