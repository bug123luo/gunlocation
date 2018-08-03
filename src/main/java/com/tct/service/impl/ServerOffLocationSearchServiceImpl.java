package com.tct.service.impl;

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
import com.tct.service.ServerOffLocationSearchService;
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
		}else {
			log.info("协助查找人员不在线!");
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
		if(unhandlerReceiveMessageHashMap.containsKey(userQueueMap.get("sendQueue"))) {
			tempUnSendReplyMessageMap = unhandlerReceiveMessageHashMap.get(userQueueMap.get("sendQueue"));
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), searchToClienJson);
		unSendReplyMessageHashMap.put(userQueueMap.get("sendQueue"), tempUnSendReplyMessageMap);
		
		return true;
	}

}
