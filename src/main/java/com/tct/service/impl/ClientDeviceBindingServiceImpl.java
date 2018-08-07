package com.tct.service.impl;


import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ClientDeviceBindingMessage;
import com.tct.codec.pojo.ClientDeviceBindingReplyBody;
import com.tct.codec.pojo.ClientDeviceBindingReplyMessage;
import com.tct.codec.pojo.ServerDeviceBindingBody;
import com.tct.codec.pojo.ServerDeviceBindingReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
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
@Service(value="clientDeviceBindingService")
public class ClientDeviceBindingServiceImpl implements SimpleService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientDeviceBindingMessage message = (ClientDeviceBindingMessage)msg;
		
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
		
		GunCustom gunCustom = new GunCustom();
		gunCustom.setBluetoothMac(message.getMessageBody().getBluetoothMac());
		gunCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getBindTime()));
		gunCustom.setState(Integer.valueOf(0));
		gunCustom.setRealTimeState(Integer.valueOf(0));
		boolean flag = clientDeviceBindingDao.updateDeviceBindingState(deviceLocationCustom, gunCustom);
		
		if (Integer.parseInt(message.getMessageBody().getReserve())==1) {
			//发送返回消息到客户端并且通知web前端绑定成功，枪支出库
			ClientDeviceBindingReplyMessage clientDeviceBindingReplyMessage = new ClientDeviceBindingReplyMessage();
			ClientDeviceBindingReplyBody clientDeviceBindingReplyBody = new ClientDeviceBindingReplyBody();
			clientDeviceBindingReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientDeviceBindingReplyBody.setReserve(Integer.toString(1));
			clientDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
			clientDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
			clientDeviceBindingReplyMessage.setMessageBody(clientDeviceBindingReplyBody);
			clientDeviceBindingReplyMessage.setMessageType("08");
			clientDeviceBindingReplyMessage.setSendTime(StringUtil.getDateString());
			clientDeviceBindingReplyMessage.setSerialNumber(message.getSerialNumber());
			clientDeviceBindingReplyMessage.setServiceType(message.getServiceType());
			clientDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
			
			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientDeviceBindingReplyMessage, simpleReplyMessage);
			String replyBody = clientDeviceBindingReplyBody.getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+clientDeviceBindingReplyBody.getAuthCode();
			simpleReplyMessage.setMessageBody(replyBody);
			
			String bingJson = JSONObject.toJSONString(simpleReplyMessage);
			//将回应APP消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;	
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), bingJson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
			
			//将回应消息发送到Web前端队列
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerDeviceBindingReplyMessage serverDeviceBindingReplyMessage = new ServerDeviceBindingReplyMessage();
			ServerDeviceBindingBody serverDeviceBindingBody =  new ServerDeviceBindingBody();
			serverDeviceBindingBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverDeviceBindingBody.setGunTag(gunCustom2.getGunTag());
			serverDeviceBindingBody.setState(Integer.toString(0));
			serverDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
			serverDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
			serverDeviceBindingReplyMessage.setMessageBody(serverDeviceBindingBody);
			serverDeviceBindingReplyMessage.setMessageType("08");
			serverDeviceBindingReplyMessage.setSendTime(message.getSendTime());
			serverDeviceBindingReplyMessage.setSerialNumber(message.getSerialNumber());
			serverDeviceBindingReplyMessage.setServiceType(message.getServiceType());
			serverDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
			
			String serverbingJson = JSONObject.toJSONString(serverDeviceBindingBody);
			
			Hashtable<String, Object> webUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey("WebOutQueue")) {
				webUnSendReplyMessageMap = unSendReplyMessageHashMap.get("WebOutQueue");
			}
			if(webUnSendReplyMessageMap==null) {
				webUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			webUnSendReplyMessageMap.put(message.getSerialNumber(), serverbingJson);
			unSendReplyMessageHashMap.put("WebOutQueue", webUnSendReplyMessageMap);
			
			flag = true;
		}else {
			//发送返回消息到客户端并且通知web前端绑定成功，枪支出库
			ClientDeviceBindingReplyMessage clientDeviceBindingReplyMessage = new ClientDeviceBindingReplyMessage();
			ClientDeviceBindingReplyBody clientDeviceBindingReplyBody = new ClientDeviceBindingReplyBody();
			clientDeviceBindingReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientDeviceBindingReplyBody.setReserve(Integer.toString(0));
			clientDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
			clientDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
			clientDeviceBindingReplyMessage.setMessageBody(clientDeviceBindingReplyBody);
			clientDeviceBindingReplyMessage.setMessageType("08");
			clientDeviceBindingReplyMessage.setSendTime(StringUtil.getDateString());
			clientDeviceBindingReplyMessage.setSerialNumber(message.getSerialNumber());
			clientDeviceBindingReplyMessage.setServiceType(message.getServiceType());
			clientDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
			
			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientDeviceBindingReplyMessage, simpleReplyMessage);
			String replyBody = StringConstant.MSG_BODY_PREFIX+clientDeviceBindingReplyBody.getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+clientDeviceBindingReplyBody.getAuthCode()
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String bingJson = JSONObject.toJSONString(simpleReplyMessage);
			//将回应APP消息放进消息缓存队列中
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;	
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), bingJson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
		}
		return flag;
	}

}
