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
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ClientVersionSyncMessage;
import com.tct.codec.pojo.ClientVersionSyncReplyBody;
import com.tct.codec.pojo.ClientVersionSyncReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
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
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

@Service(value="clientVersionSyncService")
public class ClientVersionSyncServiceImpl implements SimpleService {

	@Autowired
	SoftwareVersionCustomMapper softwareVersionCustomMapper;
	
	@Autowired
	DeviceCustomMapper deviceCustomMapper;
	
	@Autowired
	WatchDeviceCustomMapper watchDeviceCustomMapper;
	
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
		
		ClientVersionSyncMessage message = (ClientVersionSyncMessage)msg;
		
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
						
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientVersionSyncReplyMessage, simpleReplyMessage);
			String replyBody=StringConstant.MSG_BODY_PREFIX+clientVersionSyncReplyMessage.getMessageBody().getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getLastVersion()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getDownloadUrl()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getUsername()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getCommand()
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String clientsyncJson = JSONObject.toJSONString(simpleReplyMessage);
			outQueueSender.sendMessage(outQueueDestination, clientsyncJson);

		}else {
			ClientVersionSyncReplyMessage clientVersionSyncReplyMessage = new ClientVersionSyncReplyMessage();
			ClientVersionSyncReplyBody clientVersionSyncReplyBody =  new ClientVersionSyncReplyBody();
			clientVersionSyncReplyBody.setCommand("");
			clientVersionSyncReplyBody.setDownloadUrl("");
			clientVersionSyncReplyBody.setReserve(Integer.toString(0));
			clientVersionSyncReplyBody.setUsername("");
			clientVersionSyncReplyMessage.setDeviceType(message.getDeviceType());
			clientVersionSyncReplyMessage.setFormatVersion(message.getFormatVersion());
			clientVersionSyncReplyMessage.setMessageBody(clientVersionSyncReplyBody);
			clientVersionSyncReplyMessage.setMessageType(message.getMessageType());
			clientVersionSyncReplyMessage.setSendTime(StringUtil.getDateString());
			clientVersionSyncReplyMessage.setSerialNumber(message.getSerialNumber());
			clientVersionSyncReplyMessage.setServiceType(message.getServiceType());
			clientVersionSyncReplyMessage.setSessionToken(message.getSessionToken());
						
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientVersionSyncReplyMessage, simpleReplyMessage);
			String replyBody=StringConstant.MSG_BODY_PREFIX+clientVersionSyncReplyMessage.getMessageBody().getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getLastVersion()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getDownloadUrl()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getUsername()
					+StringConstant.MSG_BODY_SEPARATOR+clientVersionSyncReplyMessage.getMessageBody().getCommand()
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String clientsyncJson = JSONObject.toJSONString(simpleReplyMessage);
			outQueueSender.sendMessage(outQueueDestination, clientsyncJson);
		}
		
		
		return true;
	}

}
