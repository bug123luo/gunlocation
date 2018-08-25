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
import com.tct.cache.SessionMessageCache;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerOffLocationSearchMessage;
import com.tct.codec.pojo.ServerOffLocationSearchToClientBody;
import com.tct.codec.pojo.ServerOffLocationSearchToClientMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.mapper.DeviceCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceQueryVo;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.RandomNumber;
import com.tct.util.StringConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOffLocationSearchService")
public class ServerOffLocationSearchServiceImpl implements SimpleService {

	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	DeviceCustomMapper deviceCustomMapper;
	
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
		
		ServerOffLocationSearchMessage message= (ServerOffLocationSearchMessage)msg;
		
		ConcurrentHashMap<String, SimpleMessage> sessionMessageMap= SessionMessageCache.getSessionMessageMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		GunQueryVo gunQueryVo = new GunQueryVo();
		GunCustom gunCustom = new GunCustom();
		gunCustom.setGunTag(message.getMessageBody().getLostGunTag());
		gunQueryVo.setGunCustom(gunCustom);
		gunCustom=gunCustomMapper.selectByGunTag(gunQueryVo);
				
		String sessionToken = userOnlineSessionCache.get(message.getMessageBody().getAssDeviceNo());
		if(sessionToken==null) {
			log.info("协助查找人员不在线，请另找高明");
			return false;
		}
		
		//将离位查找人员状态改为3表示正在协助查找
		DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
		DeviceCustom deviceCustom = new DeviceCustom();
		deviceCustom.setDeviceNo(message.getMessageBody().getAssDeviceNo());
		deviceCustom.setState(3);
		deviceQueryVo.setDeviceCustom(deviceCustom);
		deviceCustomMapper.updateByDeviceQueryVo(deviceQueryVo);
		
		ServerOffLocationSearchToClientMessage serverOffLocationSearchToClientMessage = new ServerOffLocationSearchToClientMessage();
		ServerOffLocationSearchToClientBody searchToClientBody = new ServerOffLocationSearchToClientBody();
		searchToClientBody.setAuthCode(RandomNumber.getRandomNumber());
		searchToClientBody.setBluetoothMac(gunCustom.getBluetoothMac());
		searchToClientBody.setLa(message.getMessageBody().getLa());
		searchToClientBody.setLo(message.getMessageBody().getLo());
		searchToClientBody.setLostTime(message.getMessageBody().getLostTime());
		searchToClientBody.setReserve("0");
		searchToClientBody.setLostGunTag(message.getMessageBody().getLostGunTag());
		
		serverOffLocationSearchToClientMessage.setDeviceType(message.getDeviceType());
		serverOffLocationSearchToClientMessage.setFormatVersion(message.getFormatVersion());
		serverOffLocationSearchToClientMessage.setMessageBody(searchToClientBody);
		serverOffLocationSearchToClientMessage.setMessageType(message.getMessageType());
		serverOffLocationSearchToClientMessage.setSendTime(message.getSendTime());
		serverOffLocationSearchToClientMessage.setSerialNumber(message.getSerialNumber());
		serverOffLocationSearchToClientMessage.setServiceType(message.getServiceType());
		serverOffLocationSearchToClientMessage.setSessionToken(sessionToken);
		
		SimpleMessage simpleMessage = sessionMessageMap.get(sessionToken);
		
		//修改为[]格式返回到客户端
		SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
		
		BeanUtils.copyProperties(simpleMessage, simpleReplyMessage);
		simpleReplyMessage.setMessageType(message.getMessageType());
		simpleReplyMessage.setSerialNumber(message.getSerialNumber());
		simpleReplyMessage.setSendTime(message.getSendTime());
		String replyBody = StringConstant.MSG_BODY_PREFIX+searchToClientBody.getReserve()
		  +StringConstant.MSG_BODY_SEPARATOR+searchToClientBody.getBluetoothMac()
		  +StringConstant.MSG_BODY_SEPARATOR+searchToClientBody.getLostGunTag()
		  +StringConstant.MSG_BODY_SEPARATOR+searchToClientBody.getLo()
		  +StringConstant.MSG_BODY_SEPARATOR+searchToClientBody.getLa()
		  +StringConstant.MSG_BODY_SEPARATOR+searchToClientBody.getLostTime()
		  +StringConstant.MSG_BODY_SEPARATOR+searchToClientBody.getAuthCode()
		  +StringConstant.MSG_BODY_SUFFIX;
		simpleReplyMessage.setMessageBody(replyBody);
		
		String searchToClienJson = JSONObject.toJSONString(simpleReplyMessage);
		outQueueSender.sendMessage(outQueueDestination, searchToClienJson);

		
		return true;
	}

}
