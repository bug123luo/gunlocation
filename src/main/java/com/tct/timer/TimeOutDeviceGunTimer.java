package com.tct.timer;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerDeviceBindingBody;
import com.tct.codec.pojo.ServerDeviceBindingReplyMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.jms.producer.WebTopicSender;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SpringContextUtil;
import com.tct.service.TimerPara;
import com.tct.util.StringUtil;

public class TimeOutDeviceGunTimer {

	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;

	@Resource
	private WebTopicSender webTopicSender;
	
	@Resource
	@Qualifier("topicDestination")
	private Destination webtopicDestination;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
	
	ConcurrentHashMap<String, String> userSessionMap=UserOnlineSessionCache.getuserSessionMap();
	ConcurrentHashMap<String, SimpleMessage> sessionMessageMap=SessionMessageCache.getSessionMessageMessageMap();
	ConcurrentHashMap<String, String> deviceNoBingingWebUserCache = DeviceNoBingingWebUserCache.getDeviceNoWebUserHashMap();
	
	public void runClearTimer() throws Exception {
		
		TimerPara timerPara = SpringContextUtil.getBean("timerPara");
		long clearTime=Long.parseLong(timerPara.getClearTime());
		
		for(String deviceNo:userSessionMap.keySet()) {
			DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
			deviceGunCustom.setDeviceNo(deviceNo);
			DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
			deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
			DeviceGunCustom deviceGunCustom2=deviceGunCustomMapper.selectByDeviceNo(deviceGunQueryVo);
			
			Date dold=deviceGunCustom2.getCreateTime();
			Date dnew= new Date();
            long diff = dnew.getTime() - dold.getTime();
            long diffSeconds = diff / 1000;
            
            if (diffSeconds>clearTime) {
            	deviceGunCustom2.setState(1);
            	deviceGunCustomMapper.updateByDeviceGunCustom(deviceGunCustom2);
            	
    			GunCustom gunCustom2 = new GunCustom();
    			GunQueryVo gunQueryVo = new GunQueryVo();
    			gunCustom2.setBluetoothMac(deviceGunCustom2.getGunMac());
    			gunQueryVo.setGunCustom(gunCustom2);
    			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
    			
    			SimpleMessage message = sessionMessageMap.get(deviceNo);
    			
    			ServerDeviceBindingReplyMessage serverDeviceBindingReplyMessage = new ServerDeviceBindingReplyMessage();
    			ServerDeviceBindingBody serverDeviceBindingBody =  new ServerDeviceBindingBody();
    			serverDeviceBindingBody.setDeviceNo(deviceGunCustom.getDeviceNo());
    			serverDeviceBindingBody.setGunTag(gunCustom2.getGunTag());
    			serverDeviceBindingBody.setState(Integer.toString(0));
    			serverDeviceBindingReplyMessage.setDeviceType(message.getDeviceType());
    			serverDeviceBindingReplyMessage.setFormatVersion(message.getFormatVersion());
    			serverDeviceBindingReplyMessage.setMessageBody(serverDeviceBindingBody);
    			serverDeviceBindingReplyMessage.setMessageType("08");
    			serverDeviceBindingReplyMessage.setSendTime(StringUtil.getDateString());
    			serverDeviceBindingReplyMessage.setSerialNumber("123456789");
    			serverDeviceBindingReplyMessage.setServiceType(message.getServiceType());
    			serverDeviceBindingReplyMessage.setSessionToken(message.getSessionToken());
    			serverDeviceBindingReplyMessage.setUserName(deviceNoBingingWebUserCache.get(deviceGunCustom.getDeviceNo()));
    			
    			String serverbingJson = JSONObject.toJSONString(serverDeviceBindingReplyMessage);
    			webTopicSender.sendMessage(webtopicDestination, serverbingJson);
    			deviceNoBingingWebUserCache.remove(deviceGunCustom.getDeviceNo());
			}
		}

	}
}
