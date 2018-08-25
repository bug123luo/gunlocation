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
import com.tct.codec.pojo.DeviceBulletCountMessage;
import com.tct.codec.pojo.DeviceBulletCountReplyBody;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.DeviceGun;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceLocationQueryVo;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="deviceBulletCountService")
public class DeviceBulletCountServiceImpl implements SimpleService {

	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
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
		
		DeviceBulletCountMessage message = (DeviceBulletCountMessage)msg;
		
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		String sessionToken = message.getSessionToken();
		
		String deviceNo= (String) StringUtil.getKey(userOnlineSessionCache, sessionToken);
		
		if (deviceNo!=null) {
			log.info("该用户不是登录用户，不允许发送弹射计数");
			return false;
		}
		
		//更新 枪支位置信息表
		DeviceLocationQueryVo deviceLocationQueryVo = new DeviceLocationQueryVo();
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceNo);
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getOccurTime()));
		deviceLocationQueryVo.setDeviceLocationCustom(deviceLocationCustom);
		int i = deviceLocationCustomMapper.insertSelective(deviceLocationCustom);
		
		//更新枪支信息表
		DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setDeviceNo(deviceNo);
		DeviceGunCustom deviceGunCustom2 = deviceGunCustomMapper.selectByDeviceNo(deviceGunQueryVo);
		
		if(deviceGunCustom2!=null) {
			GunCustom gunCustom = new GunCustom();
			gunCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getOccurTime()));
			gunCustom.setBluetoothMac(deviceGunCustom2.getGunMac());
			
			GunQueryVo gunQueryVo =  new GunQueryVo();
			gunQueryVo.setGunCustom(gunCustom);
			GunCustom gunCustom2 =  gunCustomMapper.selectBybluetoothMac(gunQueryVo);
			gunCustom.setBulletNumber(gunCustom2.getBulletNumber()+Integer.getInteger(message.getMessageBody().getBulletNumber()));
			gunCustomMapper.updateSelective(gunCustom);
			
			DeviceBulletCountReplyBody deviceBulletCountReplyBody = new DeviceBulletCountReplyBody();
			deviceBulletCountReplyBody.setReserve(Integer.toString(0));
			deviceBulletCountReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(message, simpleReplyMessage);
			String replyBody = StringConstant.MSG_BODY_PREFIX+deviceBulletCountReplyBody.getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+deviceBulletCountReplyBody.getAuthCode()
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setSendTime(StringUtil.getDateString());
			simpleReplyMessage.setMessageBody(replyBody);
			
			String strJson = JSONObject.toJSONString(simpleReplyMessage);
			outQueueSender.sendMessage(outQueueDestination, strJson);			
		}else {
			DeviceBulletCountReplyBody deviceBulletCountReplyBody = new DeviceBulletCountReplyBody();
			deviceBulletCountReplyBody.setReserve(Integer.toString(1));
			deviceBulletCountReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(message, simpleReplyMessage);
			String replyBody = StringConstant.MSG_BODY_PREFIX+deviceBulletCountReplyBody.getReserve()
					+StringConstant.MSG_BODY_SEPARATOR+deviceBulletCountReplyBody.getAuthCode()
					+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setSendTime(StringUtil.getDateString());
			simpleReplyMessage.setMessageBody(replyBody);
			
			
			String strJson = JSONObject.toJSONString(simpleReplyMessage);
			outQueueSender.sendMessage(outQueueDestination, strJson);

		}
		
		
		
		return false;
	}

}
