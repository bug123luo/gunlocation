package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.collections.MappingChange.Map;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service(value="serverOffLocationWarningStartStopService")
public class ServerOffLocationWarningStartStopServiceImpl implements SimpleService {

	@Autowired
	@Qualifier("stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	@Qualifier("jedisTemplate")
	private RedisTemplate<String,Map<String, ?>> jedisTemplate;
	
	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
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
		
		ServerOffLocationWarningStartStopMessage message = (ServerOffLocationWarningStartStopMessage)msg;
		
		ConcurrentHashMap<String, SimpleMessage> sessionMessageMap= SessionMessageCache.getSessionMessageMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		//查找枪支的 deviceNo
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		//String sessionToken=userOnlineSessionCache.get(deviceGunCustom.getDeviceNo());
		String sessionToken=stringRedisTemplate.opsForValue().get(deviceGunCustom.getDeviceNo());
		
		if(sessionToken==null) {
			log.info("该用户不在线，无法发送查找启停命令");
			return false;
		}
		
		message.setSessionToken(sessionToken);
				
		//SimpleMessage simpleMessage = sessionMessageMap.get(sessionToken);
		SimpleMessage simpleMessage =(SimpleMessage)jedisTemplate.opsForHash().get(StringConstant.SESSION_MESSAGE_HASH, sessionToken);

		SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
		BeanUtils.copyProperties(simpleMessage, simpleReplyMessage);
		simpleReplyMessage.setMessageType(message.getMessageType());
		simpleReplyMessage.setSerialNumber(message.getSerialNumber());
		simpleReplyMessage.setSendTime(message.getSendTime());
		
		String replyBody = StringConstant.MSG_BODY_PREFIX+message.getMessageBody().getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getBluetoothMac()
				+StringConstant.MSG_BODY_SUFFIX;
		simpleReplyMessage.setMessageBody(replyBody);

		String startStopJson = JSONObject.toJSONString(simpleReplyMessage);
		outQueueSender.sendMessage(outQueueDestination, startStopJson);

		return true;
	}

}
