package com.tct.service.impl;


import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tct.util.RandomNumber;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSONObject;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.AuthCodeReplyBody;
import com.tct.codec.pojo.AuthCodeReplyMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.AuthCodeDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.mapper.DeviceCustomMapper;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceQueryVo;
import com.tct.service.SimpleService;

@Slf4j
@Service(value="authCodeService")
public class AuthCodeServiceImpl implements SimpleService{
	
	@Autowired
	AuthCodeDao authcodeDao;
	
	@Resource
	private OutQueueSender outQueueSender;
	
	@Resource
	@Qualifier("outQueueDestination")
	private Destination outQueueDestination;
		
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
			
		AuthCodeMessage message=(AuthCodeMessage)msg;
		//缓存消息
		//AuthCodeMessage 中的username目前是警员编号
		ConcurrentHashMap<String, SimpleMessage> sessionMessageMap= SessionMessageCache.getSessionMessageMessageMap();
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		//根据用户名查询在线队列的人的名称
		DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
		DeviceCustom deviceCustom =  new DeviceCustom();
		deviceCustom.setDeviceName(message.getMessageBody().getUsername());
		deviceCustom.setPassword(message.getMessageBody().getCommand());
		deviceQueryVo.setDeviceCustom(deviceCustom);
		
		DeviceCustom deviceCustom2 = null;
		try {
			deviceCustom2 = authcodeDao.findByDeviceQueryVo(deviceQueryVo);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("---用户不存在请重新注册或者在数据库中添加----");
			return false;
		}
		
		if(deviceCustom2==null || deviceCustom2.getDeviceNo()==null) {
			log.info("用户不存在请重新注册或者在数据库中添加");
			return false;
		}
		
		SimpleMessage simpleMessage = new SimpleMessage();
		BeanUtils.copyProperties(message, simpleMessage);
		userOnlineSessionCache.put(deviceCustom2.getDeviceNo(), message.getSessionToken());
		sessionMessageMap.put(message.getSessionToken(), simpleMessage);
		
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceCustom2.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		Boolean tempboolean = authcodeDao.updateDeviceLocation(deviceLocationCustom);
		
		deviceCustom2.setState(0);
		boolean flag = authcodeDao.updateDevice(deviceCustom2);
		if(tempboolean) {
			//构造回应消息
			AuthCodeReplyMessage authCodeReplyMessage =  new AuthCodeReplyMessage();
			AuthCodeReplyBody authCodeReplyBody = new AuthCodeReplyBody();
			
			authCodeReplyBody.setReserve("1");
			authCodeReplyBody.setHeartbeat("2");
			authCodeReplyBody.setLo(message.getMessageBody().getLo());
			authCodeReplyBody.setLa(message.getMessageBody().getLa());
			authCodeReplyBody.setAuthCode(RandomNumber.getRandomNumber());
			authCodeReplyBody.setDia("2");
			
			authCodeReplyMessage.setDeviceType(message.getDeviceType());
			authCodeReplyMessage.setFormatVersion(message.getFormatVersion());
			authCodeReplyMessage.setMessageType("02");
			authCodeReplyMessage.setSendTime(message.getSendTime());
			authCodeReplyMessage.setSerialNumber(message.getSerialNumber());;
			authCodeReplyMessage.setServiceType(message.getServiceType());
			authCodeReplyMessage.setMessageBody(authCodeReplyBody);
			authCodeReplyMessage.setSessionToken(message.getSessionToken());
							
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(authCodeReplyMessage, simpleReplyMessage);
			String replyBody = StringConstant.MSG_BODY_PREFIX+authCodeReplyBody.getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+authCodeReplyBody.getAuthCode()
					+StringConstant.MSG_BODY_SEPARATOR+StringConstant.IP
					+StringConstant.MSG_BODY_SEPARATOR+StringConstant.PORT
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			
			String authJson = JSONObject.toJSONString(simpleReplyMessage);
			//将回应消息放进消息缓存队列中
/*			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), authJson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);*/
			
			outQueueSender.sendMessage(outQueueDestination, authJson);
			return true;
		}else {
			log.debug("数据库更新数据失败");
			return false;
		}
		
	}

}
