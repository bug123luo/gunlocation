package com.tct.timer;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.sun.javafx.collections.MappingChange.Map;
import com.tct.cache.OnlineUserLastHBTimeCache;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.mapper.DeviceCustomMapper;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceLocationQueryVo;
import com.tct.po.DeviceQueryVo;
import com.tct.service.SpringContextUtil;
import com.tct.service.TimerPara;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
import sun.util.logging.resources.logging;

@Slf4j
public class TimeOutUserClearTimer {

	@Autowired
	@Qualifier("stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	@Qualifier("jedisTemplate")
	private RedisTemplate<String,Map<String, ?>> jedisTemplate;
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	DeviceCustomMapper deviceCustomMapper;

	//ConcurrentHashMap<String, Date> onlineUserLastHBTimeMap=OnlineUserLastHBTimeCache.getOnlineUserLastHBTimeMap();
	ConcurrentHashMap<String, String> userSessionMap=UserOnlineSessionCache.getuserSessionMap();
	ConcurrentHashMap<String, SimpleMessage> sessionMessageMap=SessionMessageCache.getSessionMessageMessageMap();
	
	public void runClearTimer() throws Exception {
		
		TimerPara timerPara = SpringContextUtil.getBean("timerPara");
		long clearTime=Long.parseLong(timerPara.getClearTime());
		
/*		for(String deviceNo:userSessionMap.keySet()) {
			log.info("Map is not empty");
			String sessionToken = userSessionMap.get(deviceNo);
			DeviceLocationQueryVo deviceLocationQueryVo = new DeviceLocationQueryVo();
			DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
			deviceLocationCustom.setDeviceNo(deviceNo);
			deviceLocationQueryVo.setDeviceLocationCustom(deviceLocationCustom);
			DeviceLocationCustom deviceLocationCustom2=deviceLocationCustomMapper.selectByDeviceLocationQueryVo(deviceLocationQueryVo);
			if (deviceLocationCustom2!=null) {
				log.info("The Timer select deviceLocationCustom2 is not null");
				Date dold = deviceLocationCustom2.getCreateTime();
				
				Date dnew = new Date();
				log.info("old time is {}",dold);
				log.info("new time is {}",dnew);
	            long diff = dnew.getTime() - dold.getTime();
	            long diffSeconds = diff / 1000;
	            
	            
	            log.info("Timer clearTime is {} seconds",clearTime);
	            if(diffSeconds>clearTime) {
	            	log.info("The user hb is outTime, user is not online user now");
	            	userSessionMap.remove(deviceNo);
	            	sessionMessageMap.remove(sessionToken);
	            	//onlineUserLastHBTimeMap.remove(deviceNo);  	
	            	DeviceCustom deviceCustom = new DeviceCustom();
	            	deviceCustom.setDeviceNo(deviceNo);
	            	deviceCustom.setState(1);
	            	DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
	            	deviceQueryVo.setDeviceCustom(deviceCustom);
	            	deviceCustomMapper.updateByDeviceQueryVo(deviceQueryVo);
	            }
			}
		}*/
		
		//redis缓存处理
		Set<String> deviceNoSet=jedisTemplate.keys("[0-9]*");
		for(String deviceNo:deviceNoSet) {
			log.info("Map is not empty");
			String sessionToken = stringRedisTemplate.opsForValue().get(deviceNo);
			DeviceLocationQueryVo deviceLocationQueryVo = new DeviceLocationQueryVo();
			DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
			deviceLocationCustom.setDeviceNo(deviceNo);
			deviceLocationQueryVo.setDeviceLocationCustom(deviceLocationCustom);
			DeviceLocationCustom deviceLocationCustom2=deviceLocationCustomMapper.selectByDeviceLocationQueryVo(deviceLocationQueryVo);
			if (deviceLocationCustom2!=null) {
				log.info("The Timer select deviceLocationCustom2 is not null");
				Date dold = deviceLocationCustom2.getCreateTime();
				
				Date dnew = new Date();
				log.info("old time is {}",dold);
				log.info("new time is {}",dnew);
	            long diff = dnew.getTime() - dold.getTime();
	            long diffSeconds = diff / 1000;
	              
	            log.info("Timer clearTime is {} seconds",clearTime);
	            if(diffSeconds>clearTime) {
	            	log.info("The user hb is outTime, user is not online user now");
	            	stringRedisTemplate.delete(deviceNo);
	            	jedisTemplate.opsForHash().delete(StringConstant.SESSION_DEVICE_HASH, sessionToken);
	            	jedisTemplate.opsForHash().delete(StringConstant.SESSION_MESSAGE_HASH, sessionToken);  	
	            	DeviceCustom deviceCustom = new DeviceCustom();
	            	deviceCustom.setDeviceNo(deviceNo);
	            	deviceCustom.setState(1);
	            	DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
	            	deviceQueryVo.setDeviceCustom(deviceCustom);
	            	deviceCustomMapper.updateByDeviceQueryVo(deviceQueryVo);
	            }
			}
		}
	}
}
