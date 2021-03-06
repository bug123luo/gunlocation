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
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerOutWareHouseMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ServerOutWareHouseDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.mapper.DeviceGunMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOutWareHouseService")
public class ServerOutWareHouseServiceImpl implements SimpleService {
		
	@Autowired
	ServerOutWareHouseDao serverOutWareHouseDao;
	
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
		ServerOutWareHouseMessage message = (ServerOutWareHouseMessage)msg;
		
		ConcurrentHashMap<String, SimpleMessage> sessionMessageMap= SessionMessageCache.getSessionMessageMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();

		//获取需要通知的终端的 deviceNo信息
		DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunCustom.setState(0);
		deviceGunCustom.setDeviceNo(message.getMessageBody().getDeviceNo());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		
		log.info("蓝牙枪支信息是:"+deviceGunCustom.getGunMac());
		
/*		DeviceGunCustom deviceGunCustom2 = null;
		deviceGunCustom2 = serverOutWareHouseDao.selectByDeviceGunQueryVo(deviceGunQueryVo);*/
			
		
		if (deviceGunCustom.getDeviceNo()==null) {
			log.info("枪支不存在");
			return false;
		}
		
		String sessionToken = userOnlineSessionCache.get(deviceGunCustom.getDeviceNo());
		if(sessionToken == null) {
			log.info("申请人员不在线，请选择另外一个人来发送");
			return false;
		}
		
		//发送到producer处理队列上
		message.setSessionToken(sessionToken);
		
		SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
		//BeanUtils.copyProperties(message, simpleReplyMessage);
		SimpleMessage simpleMessage = sessionMessageMap.get(sessionToken);
		BeanUtils.copyProperties(simpleMessage, simpleReplyMessage);
		simpleReplyMessage.setSerialNumber(message.getSerialNumber());
		simpleReplyMessage.setSendTime(message.getSendTime());
		simpleReplyMessage.setMessageType(message.getMessageType());
		String replyBody = StringConstant.MSG_BODY_PREFIX+message.getMessageBody().getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getBluetoothMac()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getGunTag()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getApplyTime()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getDeadlineTime()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getPowerAlarmLevel()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getTransmittingPower()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getBroadcastInterval()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getConnectionInterval()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getConnectionTimeout()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getSoftwareversion()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getHeartbeat()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getPowerSampling()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getSystemTime()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getMatchTime()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getSafeCode()
				+StringConstant.MSG_BODY_SUFFIX;
		simpleReplyMessage.setMessageBody(replyBody);
		
		
		String strJson = JSONObject.toJSONString(simpleReplyMessage);
		outQueueSender.sendMessage(outQueueDestination, strJson);
/*		String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
			tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), strJson);
		unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);*/
		
		return true;
	}

}
