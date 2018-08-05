package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ClientVersionSyncMessage;
import com.tct.codec.pojo.ClientVersionSyncReplyBody;
import com.tct.codec.pojo.ClientVersionSyncReplyMessage;
import com.tct.mapper.DeviceCustomMapper;
import com.tct.mapper.SoftwareVersionCustomMapper;
import com.tct.mapper.WatchDeviceCustomMapper;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceQueryVo;
import com.tct.po.SoftwareVersionCustom;
import com.tct.po.SoftwareVersionQueryVo;
import com.tct.po.WatchDeviceCustom;
import com.tct.po.WatchDeviceQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringUtil;

@Service(value="clientVersionSyncService")
public class ClientVersionSyncServiceImpl implements SimpleService {

	@Autowired
	SoftwareVersionCustomMapper softwareVersionCustomMapper;
	
	@Autowired
	DeviceCustomMapper deviceCustomMapper;
	
	@Autowired
	WatchDeviceCustomMapper watchDeviceCustomMapper;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		
		ClientVersionSyncMessage message = (ClientVersionSyncMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		String currentVersion = message.getMessageBody().getCurrentVersion();
		
		SoftwareVersionQueryVo softwareVersionQueryVo =  new SoftwareVersionQueryVo();
		SoftwareVersionCustom softwareVersionCustom = new SoftwareVersionCustom();
		softwareVersionQueryVo.setSoftwareVersionCustom(softwareVersionCustom);
		softwareVersionCustom=softwareVersionCustomMapper.selectBySoftwareVersionQueryVo(softwareVersionQueryVo);
		
		String deviceNo= userOnlineSessionCache.get(message.getSessionToken());
		DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
		DeviceCustom deviceCustom = new DeviceCustom();
		deviceCustom.setDeviceNo(deviceNo);
		deviceQueryVo.setDeviceCustom(deviceCustom);
		DeviceCustom deviceCustom2 = deviceCustomMapper.selectByDeviceQueryVo(deviceQueryVo);
		WatchDeviceCustom watchDeviceCustom2=null;
		if(deviceCustom2!=null && deviceCustom2.getDeviceName()!=null) {

		}else {
			WatchDeviceQueryVo watchDeviceQueryVo=  new WatchDeviceQueryVo();
			WatchDeviceCustom watchDeviceCustom = new WatchDeviceCustom();
			watchDeviceCustom.setDeviceNo(deviceNo);
			watchDeviceQueryVo.setWatchDeviceCustom(watchDeviceCustom);
			watchDeviceCustom2=watchDeviceCustomMapper.selectByWatchDeviceQueryVo(watchDeviceQueryVo);
		}
		
		if(!currentVersion.equals(softwareVersionCustom.getLastversion())) {
			ClientVersionSyncReplyMessage clientVersionSyncReplyMessage = new ClientVersionSyncReplyMessage();
			ClientVersionSyncReplyBody clientVersionSyncReplyBody =  new ClientVersionSyncReplyBody();
			clientVersionSyncReplyBody.setCommand(watchDeviceCustom2.getPassword());
			clientVersionSyncReplyBody.setDownloadUrl(softwareVersionCustom.getDownloadurl());
			clientVersionSyncReplyBody.setReserve(Integer.toString(1));
			clientVersionSyncReplyBody.setUsername(watchDeviceCustom2.getDeviceName());
			clientVersionSyncReplyMessage.setDeviceType(message.getDeviceType());
			clientVersionSyncReplyMessage.setFormatVersion(message.getFormatVersion());
			clientVersionSyncReplyMessage.setMessageBody(clientVersionSyncReplyBody);
			clientVersionSyncReplyMessage.setMessageType(message.getMessageType());
			clientVersionSyncReplyMessage.setSendTime(StringUtil.getDateString());
			clientVersionSyncReplyMessage.setSerialNumber(message.getSerialNumber());
			clientVersionSyncReplyMessage.setServiceType(message.getServiceType());
			clientVersionSyncReplyMessage.setSessionToken(message.getSessionToken());
			
			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");			
			String clientsyncJson = JSONObject.toJSONString(clientVersionSyncReplyMessage);
			//将回应消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), clientsyncJson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
		}else {
			return false;
		}
		
		
		return true;
	}

}
