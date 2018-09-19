package com.tct.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sun.javafx.collections.MappingChange.Map;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerOutWareHouseReplyMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOutWareHouseReplyService")
public class ServerOutWareHouseReplyServiceImpl implements SimpleService {

	@Autowired
	@Qualifier("jedisTemplate")
	private RedisTemplate<String,Map<String, ?>> jedisTemplate;
	
	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		boolean flag = false;
		
		ServerOutWareHouseReplyMessage message=(ServerOutWareHouseReplyMessage)msg;
		
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		//String deviceNo = (String)StringUtil.getKey(userOnlineSessionCache, message.getMessageBody().getAuthCode());
		String deviceNo = (String)jedisTemplate.opsForHash().get(StringConstant.SESSION_DEVICE_HASH, message.getMessageBody().getAuthCode());
		if(deviceNo==null) {
			log.info("用户未登陆，对接收到04号报文不处理");
			return flag;
		}
		
		if(message.getMessageBody().getReserve().equals("0")) {
/*			DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
			deviceGunCustom.setDeviceNo(deviceNo);
			deviceGunCustomMapper.deleteBydeviceNo(deviceGunCustom);*/
		}
		
		flag = true;
		return flag;
	}

}
