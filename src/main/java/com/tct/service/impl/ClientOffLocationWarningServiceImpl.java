package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.ResourceTransactionManager;

import com.alibaba.fastjson.JSONObject;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.codec.pojo.ClientOffLocationWarningMessage;
import com.tct.codec.pojo.ClientOffLocationWarningReplyBody;
import com.tct.codec.pojo.ClientOffLocationWarningReplyMessage;
import com.tct.codec.pojo.ServerInWareHouseReplyBody;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.codec.pojo.ServerOffLocationWarningReplyBody;
import com.tct.codec.pojo.ServerOffLocationWarningReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.dao.ClientOffLocationWarningDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.jms.producer.WebTopicSender;
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
	private WebTopicSender webTopicSender;
	
	@Resource
	@Qualifier("topicDestination")
	private Destination webtopicDestination;
	
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
				
		if (deviceGunCustom==null) {
			log.info("ClientOffLocationWarning 离位告警消息枪支没有出库");
			return false;
		}
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
			log.info("Client off location Message send to {}",deviceGunCustom.getDeviceNo());
			outQueueSender.sendMessage(outQueueDestination, msgJson);
			
			ServerOffLocationWarningReplyMessage serverReplyMessage= new ServerOffLocationWarningReplyMessage();
			ServerOffLocationWarningReplyBody serverReplyBody= new  ServerOffLocationWarningReplyBody();
			serverReplyBody.setAreaCode(message.getMessageBody().getAreaCode());
			serverReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverReplyBody.setGunTag(deviceGunCustom.getGunMac());
			serverReplyBody.setLa(message.getMessageBody().getLa());
			serverReplyBody.setLo(message.getMessageBody().getLo());
			serverReplyBody.setState(Integer.toString(0));
			BeanUtils.copyProperties(message, serverReplyMessage);
			serverReplyMessage.setMessageBody(serverReplyBody);
			String serverJson =JSONObject.toJSONString(message);
			log.info("The {} Client off location Message send to WebServer",deviceGunCustom.getDeviceNo());
			webTopicSender.sendMessage(webtopicDestination, serverJson);
			//webOutQueueSender.sendMessage(webOutQueueDestination, serverJson);
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
			log.info("Client off location Message send to {}",deviceGunCustom.getDeviceNo());
			outQueueSender.sendMessage(outQueueDestination, msgJson);
			
			ServerOffLocationWarningReplyMessage serverReplyMessage= new ServerOffLocationWarningReplyMessage();
			ServerOffLocationWarningReplyBody serverReplyBody= new  ServerOffLocationWarningReplyBody();
			serverReplyBody.setAreaCode(message.getMessageBody().getAreaCode());
			serverReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverReplyBody.setGunTag(deviceGunCustom.getGunMac());
			serverReplyBody.setLa(message.getMessageBody().getLa());
			serverReplyBody.setLo(message.getMessageBody().getLo());
			serverReplyBody.setState(Integer.toString(1));
			BeanUtils.copyProperties(message, serverReplyMessage);
			serverReplyMessage.setMessageBody(serverReplyBody);
			String serverJson =JSONObject.toJSONString(message);
			log.info("The {} Client off location Message send to WebServer",deviceGunCustom.getDeviceNo());
			webTopicSender.sendMessage(webtopicDestination, serverJson);
		}
		return false;
	}

}
