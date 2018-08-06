package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerOutWareHouseMessage;
import com.tct.dao.ServerOutWareHouseDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.service.SimpleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOutWareHouseService")
public class ServerOutWareHouseServiceImpl implements SimpleService {
		
	@Autowired
	ServerOutWareHouseDao serverOutWareHouseDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ServerOutWareHouseMessage message = (ServerOutWareHouseMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();

		//获取需要通知的终端的 deviceNo信息
		DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		DeviceGunCustom deviceGunCustom2 = serverOutWareHouseDao.selectByDeviceGunQueryVo(deviceGunQueryVo);
			
		String sessionToken = userOnlineSessionCache.get(deviceGunCustom2.getDeviceNo());
		if(sessionToken == null) {
			log.info("申请人员不在线，请选择另外一个人来发送");
			return false;
		}
		
		//发送到producer处理队列上
		message.setSessionToken(sessionToken);
		
		String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");

		String outWareJson = JSONObject.toJSONString(message);

		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
			tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), outWareJson);
		unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
		
		return true;
	}

}
