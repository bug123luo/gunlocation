package com.tct.timer;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

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

public class TimeOutUserClearTimer {
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	DeviceCustomMapper deviceCustomMapper;

	ConcurrentHashMap<String, Date> onlineUserLastHBTimeMap=OnlineUserLastHBTimeCache.getOnlineUserLastHBTimeMap();
	ConcurrentHashMap<String, String> userSessionMap=UserOnlineSessionCache.getuserSessionMap();
	ConcurrentHashMap<String, SimpleMessage> sessionMessageMap=SessionMessageCache.getSessionMessageMessageMap();
	
	public void runClearTimer() throws Exception {
		
		TimerPara timerPara = SpringContextUtil.getBean("timerPara");
		long clearTime=Long.parseLong(timerPara.getClearTime());
		
		for(String deviceNo:onlineUserLastHBTimeMap.keySet()) {
			DeviceLocationQueryVo deviceLocationQueryVo = new DeviceLocationQueryVo();
			DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
			deviceLocationCustom.setDeviceNo(deviceNo);
			deviceLocationQueryVo.setDeviceLocationCustom(deviceLocationCustom);
			DeviceLocationCustom deviceLocationCustom2=deviceLocationCustomMapper.selectByDeviceLocationQueryVo(deviceLocationQueryVo);
			if (deviceLocationCustom2!=null) {
				Date dold = deviceLocationCustom.getCreateTime();
				Date dnew = (Date)onlineUserLastHBTimeMap.get(deviceNo);
				
	            long diff = dnew.getTime() - dold.getTime();
	            long diffSeconds = diff / 1000;
	            //long diffMinutes = diff / (60 * 1000) % 60;
	            
	            if(diffSeconds>clearTime) {
	            	userSessionMap.remove(deviceNo);
	            	sessionMessageMap.remove(deviceNo);
	            	onlineUserLastHBTimeMap.remove(deviceNo);  	
	            	DeviceCustom deviceCustom = new DeviceCustom();
	            	deviceCustom.setDeviceNo(deviceNo);
	            	deviceCustom.setState(2);
	            	DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
	            	deviceQueryVo.setDeviceCustom(deviceCustom);
	            	deviceCustomMapper.updateByDeviceQueryVo(deviceQueryVo);
	            }
			}
		}
		
		
	}
}
