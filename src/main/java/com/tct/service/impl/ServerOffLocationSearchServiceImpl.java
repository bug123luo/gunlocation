package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerOffLocationSearchMessage;
import com.tct.codec.pojo.ServerOffLocationSearchToClientBody;
import com.tct.codec.pojo.ServerOffLocationSearchToClientMessage;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.mapper.DeviceCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceQueryVo;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.RandomNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOffLocationSearchService")
public class ServerOffLocationSearchServiceImpl implements SimpleService {

	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	DeviceCustomMapper deviceCustomMapper;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		
		ServerOffLocationSearchMessage message= (ServerOffLocationSearchMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		GunQueryVo gunQueryVo = new GunQueryVo();
		GunCustom gunCustom = new GunCustom();
		gunCustom.setGunTag(message.getMessageBody().getId());
		gunCustom=gunCustomMapper.selectByGunTag(gunQueryVo);
				
		String sessionToken = userOnlineSessionCache.get(message.getMessageBody().getAssDeviceNo());
		if(sessionToken==null) {
			log.info("协助查找人员不在线，请另找高明");
			return false;
		}
		
		//将离位查找人员状态改为3表示正在协助查找
		DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
		DeviceCustom deviceCustom = new DeviceCustom();
		deviceCustom.setDeviceNo(message.getMessageBody().getAssDeviceNo());
		deviceCustom.setState(3);
		deviceQueryVo.setDeviceCustom(deviceCustom);
		deviceCustomMapper.updateByDeviceQueryVo(deviceQueryVo);
		
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
		serverOffLocationSearchToClientMessage.setSessionToken(sessionToken);
		
		
		String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");

		String searchToClienJson = JSONObject.toJSONString(serverOffLocationSearchToClientMessage);
		
		//将回应消息放进消息缓存队列中
		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
			tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), searchToClienJson);
		unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
		
		return true;
	}

}