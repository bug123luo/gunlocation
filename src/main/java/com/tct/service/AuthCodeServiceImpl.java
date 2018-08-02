package com.tct.service;


import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tct.util.RandomNumber;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.AuthCodeReplyBody;
import com.tct.codec.pojo.AuthCodeReplyMessage;
import com.tct.dao.AuthCodeDao;

@Slf4j
@Service(value="authCodeService")
public class AuthCodeServiceImpl implements AuthCodeService{
	
	@Autowired
	AuthCodeDao authcodeDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
			
		AuthCodeMessage message=(AuthCodeMessage)msg;
		//缓存消息
		//AuthCodeMessage 中的username目前是警员编号
		ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		//创建发送到终端队列的队列名
		Hashtable<String , String> userQueueMap=null;
		if (userOnlineQueueHashMap.containsKey(message.getMessageBody().getUsername())) {
			userQueueMap=userOnlineQueueHashMap.get(message.getMessageBody().getUsername());
		}
		if(userQueueMap==null) {
			userQueueMap=new Hashtable<String,String>();
		}
		userQueueMap.put("sendQueue", message.getMessageBody().getUsername());
		
		userOnlineQueueHashMap.put(message.getMessageBody().getUsername(), userQueueMap);	
		
		//将接收到的消息放在本地的接收消息队列上
		Hashtable<String, Object> messageMap=null;
		if (unhandlerReceiveMessageHashMap.containsKey(message.getMessageBody().getUsername())) {
			messageMap= unhandlerReceiveMessageHashMap.get(message.getMessageBody().getUsername());
		}
		if(messageMap ==null) {
			messageMap=new Hashtable<String,Object>();
		}
		
		messageMap.put(message.getSerialNumber(), message);		
		unhandlerReceiveMessageHashMap.put(message.getMessageBody().getUsername(), messageMap);

		Boolean tempboolean = authcodeDao.findDeviceUserAndUpdateLocation(message);
		if(tempboolean) {
			//构造回应消息
			AuthCodeReplyMessage authCodeReplyMessage =  new AuthCodeReplyMessage();
			AuthCodeReplyBody authCodeReplyBody = new AuthCodeReplyBody();
			
			authCodeReplyBody.setReserve("1");
			authCodeReplyBody.setHeartbeat("2");
			authCodeReplyBody.setLo(message.getMessageBody().getLo());
			authCodeReplyBody.setLa(message.getMessageBody().getLa());
			authCodeReplyBody.setAuthCode(RandomNumber.getRandomNumber());
			authCodeReplyBody.setDia("2");
			
			authCodeReplyMessage.setDeviceType(message.getDeviceType());
			authCodeReplyMessage.setFormatVersion(message.getFormatVersion());
			authCodeReplyMessage.setMessageType(message.getMessageType());
			authCodeReplyMessage.setSendTime(message.getSendTime());
			authCodeReplyMessage.setSerialNumber(message.getSerialNumber());;
			authCodeReplyMessage.setServiceType(message.getServiceType());
			authCodeReplyMessage.setMessageBody(authCodeReplyBody);
				
			String authJson = JSONObject.toJSONString(authCodeReplyMessage);
			//将回应消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unhandlerReceiveMessageHashMap.containsKey(message.getMessageBody().getUsername())) {
				tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(message.getMessageBody().getUsername());
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), authJson);
			unSendReplyMessageHashMap.put(message.getMessageBody().getUsername(), tempUnSendReplyMessageMap);
			
			return true;
		}else {
			log.debug("数据库更新数据失败");
			return false;
		}
		
	}

}
