package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.codec.pojo.ClientOffLocationWarningMessage;
import com.tct.codec.pojo.ClientOffLocationWarningReplyBody;
import com.tct.codec.pojo.ClientOffLocationWarningReplyMessage;
import com.tct.codec.pojo.ServerInWareHouseReplyBody;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.dao.ClientOffLocationWarningDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.po.SosMessageCustom;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientOffLocationWarningService")
public class ClientOffLocationWarningServiceImpl implements SimpleService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientOffLocationWarningDao clientOffLocationWarningDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
	
	@Resource
	private OutQueueSender outQueueSender;
	
	@Resource
	private WebOutQueueSender webOutQueueSender;
	
	@Resource
	@Qualifier("outQueueDestination")
	private Destination outQueueDestination;
	
	@Resource
	@Qualifier("webOutQueueDestination")
	private Destination webOutQueueDestination;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientOffLocationWarningMessage message = (ClientOffLocationWarningMessage)msg;
		
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		
		
		//插入device_location表，插入sos_message表，更新 gun表状态
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		
		GunCustom gunCustom = new GunCustom();
		gunCustom.setBluetoothMac(message.getMessageBody().getBluetoothMac());
		gunCustom.setRealTimeState(2);
		gunCustom.setWarehouseId(Integer.valueOf(message.getMessageBody().getAreaCode()));
		
		SosMessageCustom sosMessageCustom =  new SosMessageCustom();
		sosMessageCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		sosMessageCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		sosMessageCustom.setLatitude(message.getMessageBody().getLa());
		sosMessageCustom.setLongitude(message.getMessageBody().getLo());
		sosMessageCustom.setSosTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setFinallyTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		sosMessageCustom.setState(1);
		
		boolean flag=clientOffLocationWarningDao.updateClientOffLocationWaring(deviceLocationCustom, gunCustom, sosMessageCustom);
		
		if(flag) {
			ClientOffLocationWarningReplyMessage clientOffLocationWarningReplyMessage = new ClientOffLocationWarningReplyMessage();
			ClientOffLocationWarningReplyBody clientOffLocationWarningReplyBody = new ClientOffLocationWarningReplyBody();
			clientOffLocationWarningReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientOffLocationWarningReplyBody.setReserve(Integer.toString(0));
			clientOffLocationWarningReplyMessage.setDeviceType(message.getDeviceType());
			clientOffLocationWarningReplyMessage.setFormatVersion(message.getFormatVersion());
			clientOffLocationWarningReplyMessage.setMessageBody(clientOffLocationWarningReplyBody);
			clientOffLocationWarningReplyMessage.setMessageType("16");
			clientOffLocationWarningReplyMessage.setSendTime(StringUtil.getDateString());
			clientOffLocationWarningReplyMessage.setSerialNumber(message.getSerialNumber());
			clientOffLocationWarningReplyMessage.setServiceType(message.getServiceType());
			clientOffLocationWarningReplyMessage.setSessionToken(message.getSessionToken());
			
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientOffLocationWarningReplyMessage, simpleReplyMessage);
			String replyBody=StringConstant.MSG_BODY_PREFIX+clientOffLocationWarningReplyBody.getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+clientOffLocationWarningReplyBody.getAuthCode()
				+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String msgJson = JSONObject.toJSONString(simpleReplyMessage);
			outQueueSender.sendMessage(outQueueDestination, msgJson);
			//将APP回应消息放进消息缓存队列中
			/*String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), msgJson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);*/
			
			//将web端回应消息放进消息缓存队列
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage = new ServerInWareHouseReplyMessage();
			ServerInWareHouseReplyBody serverInWareHouseReplyBody =  new ServerInWareHouseReplyBody();
			serverInWareHouseReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverInWareHouseReplyBody.setGunTag(gunCustom2.getGunTag());
			serverInWareHouseReplyBody.setState(Integer.toString(2));

			serverInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			serverInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			serverInWareHouseReplyMessage.setMessageBody(serverInWareHouseReplyBody);
			serverInWareHouseReplyMessage.setMessageType("16");
			serverInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			serverInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());
			serverInWareHouseReplyMessage.setServiceType(message.getServiceType());
			
			String serverInWareHouseReplyJson = JSONObject.toJSONString(serverInWareHouseReplyMessage);
			webOutQueueSender.sendMessage(webOutQueueDestination, serverInWareHouseReplyJson);
/*			if(unSendReplyMessageHashMap.containsKey("WebOutQueue")) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get("WebOutQueue");
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), serverInWareHouseReplyJson);
			unSendReplyMessageHashMap.put("WebOutQueue", tempUnSendReplyMessageMap);*/
			flag = true;
		}else {
			ClientOffLocationWarningReplyMessage clientOffLocationWarningReplyMessage = new ClientOffLocationWarningReplyMessage();
			ClientOffLocationWarningReplyBody clientOffLocationWarningReplyBody = new ClientOffLocationWarningReplyBody();
			clientOffLocationWarningReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientOffLocationWarningReplyBody.setReserve(Integer.toString(1));
			clientOffLocationWarningReplyMessage.setDeviceType(message.getDeviceType());
			clientOffLocationWarningReplyMessage.setFormatVersion(message.getFormatVersion());
			clientOffLocationWarningReplyMessage.setMessageBody(clientOffLocationWarningReplyBody);
			clientOffLocationWarningReplyMessage.setMessageType("16");
			clientOffLocationWarningReplyMessage.setSendTime(StringUtil.getDateString());
			clientOffLocationWarningReplyMessage.setSerialNumber(message.getSerialNumber());
			clientOffLocationWarningReplyMessage.setServiceType(message.getServiceType());
			clientOffLocationWarningReplyMessage.setSessionToken(message.getSessionToken());
			
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientOffLocationWarningReplyMessage, simpleReplyMessage);
			String replyBody=StringConstant.MSG_BODY_PREFIX+clientOffLocationWarningReplyBody.getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+clientOffLocationWarningReplyBody.getAuthCode()
				+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String msgJson = JSONObject.toJSONString(simpleReplyMessage);
			outQueueSender.sendMessage(outQueueDestination, msgJson);
			//将APP回应消息放进消息缓存队列中
/*			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), msgJson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);*/
			
			//将web端回应消息放进消息缓存队列
/*			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage = new ServerInWareHouseReplyMessage();
			ServerInWareHouseReplyBody serverInWareHouseReplyBody =  new ServerInWareHouseReplyBody();
			serverInWareHouseReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverInWareHouseReplyBody.setGunTag(gunCustom2.getGunTag());
			serverInWareHouseReplyBody.setState(Integer.toString(2));

			serverInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			serverInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			serverInWareHouseReplyMessage.setMessageBody(serverInWareHouseReplyBody);
			serverInWareHouseReplyMessage.setMessageType("16");
			serverInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			serverInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());
			serverInWareHouseReplyMessage.setServiceType(message.getServiceType());
			
			String serverInWareHouseReplyJson = JSONObject.toJSONString(serverInWareHouseReplyMessage);
						
			if(unSendReplyMessageHashMap.containsKey("WebOutQueue")) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get("WebOutQueue");
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), serverInWareHouseReplyJson);
			unSendReplyMessageHashMap.put("WebOutQueue", tempUnSendReplyMessageMap);*/
		}
		return false;
	}

}
