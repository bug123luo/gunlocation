package com.tct.service.impl;

import java.util.Date;
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
import com.tct.cache.OnlineUserLastHBTimeCache;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.AuthCodeReplyBody;
import com.tct.codec.pojo.AuthCodeReplyMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.AuthCodeDao;
import com.tct.jms.producer.OutQueueSender;
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
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		ConcurrentHashMap<String, Date> onlineUserLastHBTimeMap=OnlineUserLastHBTimeCache.getOnlineUserLastHBTimeMap();

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
			AuthCodeReplyMessage authCodeReplyMessage =  new AuthCodeReplyMessage();
			AuthCodeReplyBody authCodeReplyBody = new AuthCodeReplyBody();
			
			authCodeReplyBody.setReserve("1");
			authCodeReplyBody.setHeartbeat("2");
			authCodeReplyBody.setLo(message.getMessageBody().getLo());
			authCodeReplyBody.setLa(message.getMessageBody().getLa());
			authCodeReplyBody.setAuthCode(message.getSessionToken());
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
			log.info("Login Reply Message send to {}",deviceCustom2.getDeviceNo());
			outQueueSender.sendMessage(outQueueDestination, authJson);
			return false;
		}else {
			log.info("login deviceNo is {}",deviceCustom2.getDeviceNo());
		}
		deviceCustom2.setCreateTime(StringUtil.getDate(message.getSendTime()));
		deviceCustom2.setUpdateTime(StringUtil.getDate(message.getSendTime()));
		authcodeDao.updateDevice(deviceCustom2);
		
		SimpleMessage simpleMessage = new SimpleMessage();
		BeanUtils.copyProperties(message, simpleMessage);
		userOnlineSessionCache.put(deviceCustom2.getDeviceNo(), message.getSessionToken());
		sessionMessageMap.put(message.getSessionToken(), simpleMessage);
		//onlineUserLastHBTimeMap.put(deviceCustom2.getDeviceNo(), StringUtil.getDate(message.getSendTime()));
		
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceCustom2.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
		if (message.getMessageBody().getLa().equals("0.0") || message.getMessageBody().getLa().equals("0.0")) {
			
		} else {
			Boolean tempboolean = authcodeDao.updateDeviceLocation(deviceLocationCustom);
		}
		
		
		deviceCustom2.setState(0);
		boolean flag = authcodeDao.updateDevice(deviceCustom2);
		/*if(tempboolean) {*/
			//构造回应消息
			AuthCodeReplyMessage authCodeReplyMessage =  new AuthCodeReplyMessage();
			AuthCodeReplyBody authCodeReplyBody = new AuthCodeReplyBody();
			
			authCodeReplyBody.setReserve("0");
			authCodeReplyBody.setHeartbeat("2");
			authCodeReplyBody.setLo(message.getMessageBody().getLo());
			authCodeReplyBody.setLa(message.getMessageBody().getLa());
			authCodeReplyBody.setAuthCode(message.getSessionToken());
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
			log.info("Login Reply Message send to {}",deviceCustom2.getDeviceNo());
			outQueueSender.sendMessage(outQueueDestination, authJson);
			return true;
/*		}else {
			log.debug("数据库更新数据失败");
			return false;
		}*/
		
	}

}
