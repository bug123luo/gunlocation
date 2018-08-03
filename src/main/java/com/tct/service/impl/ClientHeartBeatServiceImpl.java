package com.tct.service.impl;

import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ClientHeartBeatMessage;
import com.tct.codec.pojo.ClientHeartBeatReplyBody;
import com.tct.codec.pojo.ClientHeartBeatReplyMessage;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.service.ClientHeartBeatService;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientHeartBeatService")
public class ClientHeartBeatServiceImpl implements ClientHeartBeatService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientHeartBeatMessage message = (ClientHeartBeatMessage)msg;
		
		//查找枪支的 deviceNo
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		//缓存消息
		//AuthCodeMessage 中的username目前是警员编号
		ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		//创建发送到终端队列的队列名
		Hashtable<String, Object> messageMap=null;
		String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
		
		//将位置信息插入在device_location表中，在将gun表中小区代码字段更新
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		GunCustom gunCustom = new GunCustom();
		Date date = StringUtil.getDate(message.getSendTime());
		deviceLocationCustom.setCreateTime(date);
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setUpdateTime(date);
		gunCustom.setBluetoothMac(deviceGunCustom.getGunMac());
		boolean flag=clientHeartBeatDao.updateDeviceLocation(deviceLocationCustom, gunCustom);
		
		if(flag) {
			ClientHeartBeatReplyMessage clientHeartBeatReplyMessage = new ClientHeartBeatReplyMessage();
			ClientHeartBeatReplyBody clientHeartBeatReplyBody = new ClientHeartBeatReplyBody();
			clientHeartBeatReplyBody.setReserve("1");
			clientHeartBeatReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientHeartBeatReplyMessage.setDeviceType(message.getDeviceType());
			clientHeartBeatReplyMessage.setFormatVersion(message.getFormatVersion());
			clientHeartBeatReplyMessage.setMessageType("14");
			clientHeartBeatReplyMessage.setMessageBody(clientHeartBeatReplyBody);
			clientHeartBeatReplyMessage.setSendTime(StringUtil.getDateString());
			clientHeartBeatReplyMessage.setSerialNumber(message.getSerialNumber());
			clientHeartBeatReplyMessage.setServiceType(message.getServiceType());
			clientHeartBeatReplyMessage.setSessionToken(message.getSessionToken());
			
			String heartBeatJson = JSONObject.toJSONString(clientHeartBeatReplyMessage);
			//将回应消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), heartBeatJson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
			
			return true;
		}
		
		return false;
	}

}
