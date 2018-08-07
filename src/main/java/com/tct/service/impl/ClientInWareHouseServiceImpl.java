package com.tct.service.impl;

import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ClientInWareHouseMessage;
import com.tct.codec.pojo.ClientInWareHouseReplyBody;
import com.tct.codec.pojo.ClientInWareHouseReplyMessage;
import com.tct.codec.pojo.ServerInWareHouseBody;
import com.tct.codec.pojo.ServerInWareHouseReplyBody;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.dao.ClientInWareHouseDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientInWareHouseService")
public class ClientInWareHouseServiceImpl implements SimpleService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientInWareHouseDao clientInWareHouseDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
		
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientInWareHouseMessage message = (ClientInWareHouseMessage)msg;
		
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
				
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		DeviceGunCustom deviceGunCustom2 = new DeviceGunCustom();
		deviceGunCustom2.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunCustom2.setInWarehouseTime(new Date());
		deviceGunCustom2.setState(1);
		
		boolean flag = clientInWareHouseDao.updateDeviceInWareHouseState(deviceLocationCustom, deviceGunCustom2);
		if (flag) {
			ClientInWareHouseReplyMessage clientInWareHouseReplyMessage =  new ClientInWareHouseReplyMessage();
			ClientInWareHouseReplyBody clientInWareHouseReplyBody = new ClientInWareHouseReplyBody();
			clientInWareHouseReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientInWareHouseReplyBody.setReserve(Integer.toString(1));
			
			clientInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			clientInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			clientInWareHouseReplyMessage.setMessageType("12");
			clientInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			clientInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			clientInWareHouseReplyMessage.setServiceType(message.getServiceType());
			clientInWareHouseReplyMessage.setMessageBody(clientInWareHouseReplyBody);
			clientInWareHouseReplyMessage.setSessionToken(message.getSessionToken());
			
			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientInWareHouseReplyMessage, simpleReplyMessage);
			String replyBody =StringConstant.MSG_BODY_PREFIX+clientInWareHouseReplyBody.getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+clientInWareHouseReplyBody.getAuthCode()
				+StringConstant.MSG_BODY_SUFFIX;
			
			String clientInWareHouseReplyjson = JSONObject.toJSONString(clientInWareHouseReplyMessage);
			//将APP回应消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), clientInWareHouseReplyjson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
			
			
			//将向服务器的发送消息放在缓存队列中
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage =  new ServerInWareHouseReplyMessage();
			ServerInWareHouseReplyBody serverInWareHouseReplyBody = new ServerInWareHouseReplyBody();
			serverInWareHouseReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverInWareHouseReplyBody.setGunTag(gunCustom2.getGunTag());
			serverInWareHouseReplyBody.setState(Integer.toString(1));
			
			serverInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			serverInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			serverInWareHouseReplyMessage.setMessageType("12");
			serverInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			serverInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			serverInWareHouseReplyMessage.setServiceType(message.getServiceType());
			serverInWareHouseReplyMessage.setMessageBody(serverInWareHouseReplyBody);
			serverInWareHouseReplyMessage.setSessionToken(message.getSessionToken());
			
			String serverInWareReplyJson = JSONObject.toJSONString(serverInWareHouseReplyBody);
			
			if(unSendReplyMessageHashMap.containsKey("WebOutQueue")) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get("WebOutQueue");
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), serverInWareReplyJson);
			unSendReplyMessageHashMap.put("WebOutQueue", tempUnSendReplyMessageMap);
			
			flag = true;
		}else {
			ClientInWareHouseReplyMessage clientInWareHouseReplyMessage =  new ClientInWareHouseReplyMessage();
			ClientInWareHouseReplyBody clientInWareHouseReplyBody = new ClientInWareHouseReplyBody();
			clientInWareHouseReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientInWareHouseReplyBody.setReserve(Integer.toString(0));
			
			clientInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			clientInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			clientInWareHouseReplyMessage.setMessageType("12");
			clientInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			clientInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			clientInWareHouseReplyMessage.setServiceType(message.getServiceType());
			clientInWareHouseReplyMessage.setMessageBody(clientInWareHouseReplyBody);
			clientInWareHouseReplyMessage.setSessionToken(message.getSessionToken());
			
			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientInWareHouseReplyMessage, simpleReplyMessage);
			String replyBody =clientInWareHouseReplyBody.getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+clientInWareHouseReplyBody.getAuthCode();
			simpleReplyMessage.setMessageBody(replyBody);
			String clientInWareHouseReplyjson = JSONObject.toJSONString(clientInWareHouseReplyMessage);
			//将APP回应消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), clientInWareHouseReplyjson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
			flag = true;
		}
		
		return false;
	}

}
