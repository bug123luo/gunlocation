package com.tct.service;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.ServerOffLocationSearchToClientMessage;
import com.tct.codec.pojo.ServerOffLocationSearchMessage;
import com.tct.codec.pojo.ServerOffLocationSearchReplyBody;
import com.tct.codec.pojo.ServerOffLocationSearchReplyMessage;
import com.tct.codec.pojo.ServerOffLocationSearchToClientBody;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.util.RandomNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOffLocationSearchService")
public class ServerOffLocationSearchServiceImpl implements ServerOffLocationSearchService {

	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		
		ServerOffLocationSearchMessage message= (ServerOffLocationSearchMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		GunQueryVo gunQueryVo = new GunQueryVo();
		GunCustom gunCustom = new GunCustom();
		gunCustom.setGunTag(message.getMessageBody().getId());
		gunCustom=gunCustomMapper.selectByGunTag(gunQueryVo);
		//创建发送到终端队列的队列名
		Hashtable<String , String> userQueueMap=null;
		if (userOnlineQueueHashMap.containsKey(message.getMessageBody().getAssDeviceNo())) {
			userQueueMap=userOnlineQueueHashMap.get(message.getMessageBody().getAssDeviceNo());
		}
		if(userQueueMap==null) {
			userQueueMap=new Hashtable<String,String>();
		}
		userQueueMap.put("sendQueue", message.getMessageBody().getAssDeviceNo());
		
		userOnlineQueueHashMap.put(message.getMessageBody().getAssDeviceNo(), userQueueMap);	
		
		//将接收到的消息放在本地的接收消息队列上
		Hashtable<String, Object> messageMap=null;
		if (unhandlerReceiveMessageHashMap.containsKey(message.getMessageBody().getAssDeviceNo())) {
			messageMap= unhandlerReceiveMessageHashMap.get(message.getMessageBody().getAssDeviceNo());
		}
		if(messageMap ==null) {
			messageMap=new Hashtable<String,Object>();
		}
		
		messageMap.put("s"+message.getSerialNumber(), message);		
		unhandlerReceiveMessageHashMap.put(message.getMessageBody().getAssDeviceNo(), messageMap);
		
		ServerOffLocationSearchToClientMessage serverOffLocationSearchToClientMessage = new ServerOffLocationSearchToClientMessage();
		ServerOffLocationSearchToClientBody searchToClientBody = new ServerOffLocationSearchToClientBody();
		searchToClientBody.setAuthCode(RandomNumber.getRandomNumber());
		searchToClientBody.setBluetoothMac(gunCustom.getBluetoothMac());
		searchToClientBody.setLa(message.getMessageBody().getLa());
		searchToClientBody.setLo(message.getMessageBody().getLo());
		searchToClientBody.setLostTime(message.getMessageBody().getLostTime());
		searchToClientBody.setReserve("");
		
		serverOffLocationSearchToClientMessage.setDeviceType(message.getDeviceType());
		serverOffLocationSearchToClientMessage.setFormatVersion(message.getFormatVersion());
		serverOffLocationSearchToClientMessage.setMessageBody(searchToClientBody);
		serverOffLocationSearchToClientMessage.setMessageType(message.getMessageType());
		serverOffLocationSearchToClientMessage.setSendTime(message.getSendTime());
		serverOffLocationSearchToClientMessage.setSerialNumber(message.getSerialNumber());
		serverOffLocationSearchToClientMessage.setServiceType(message.getServiceType());
		
		String searchToClienJson = JSONObject.toJSONString(serverOffLocationSearchToClientMessage);
		
		
		//将回应消息放进消息缓存队列中
		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unhandlerReceiveMessageHashMap.containsKey(message.getMessageBody().getAssDeviceNo())) {
			tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(message.getMessageBody().getAssDeviceNo());
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), searchToClienJson);
		unSendReplyMessageHashMap.put(message.getMessageBody().getAssDeviceNo(), tempUnSendReplyMessageMap);
		
		return true;
	}

}
