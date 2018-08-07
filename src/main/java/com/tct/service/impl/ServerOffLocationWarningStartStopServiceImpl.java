package com.tct.service.impl;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UserOnlineQueueCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service(value="serverOffLocationWarningStartStopService")
public class ServerOffLocationWarningStartStopServiceImpl implements SimpleService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		
		ServerOffLocationWarningStartStopMessage message = (ServerOffLocationWarningStartStopMessage)msg;
		
		ConcurrentHashMap<String, Hashtable<String, String>> userOnlineQueueHashMap = UserOnlineQueueCache.getOnlineUserQueueMap();
		ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		//查找枪支的 deviceNo
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		String sessionToken=userOnlineSessionCache.get(deviceGunCustom.getDeviceNo());
		
		if(sessionToken==null) {
			log.info("该用户不在线，无法发送查找启停命令");
			return false;
		}
		
		message.setSessionToken(sessionToken);
				
		SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
		BeanUtils.copyProperties(message, simpleReplyMessage);
		String replyBody = StringConstant.MSG_BODY_PREFIX+message.getMessageBody().getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getBluetoothMac()
				+StringConstant.MSG_BODY_SUFFIX;
		simpleReplyMessage.setMessageBody(replyBody);
		String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");

		String startStopJson = JSONObject.toJSONString(simpleReplyMessage);
		//将回应消息放进消息缓存队列中
		Hashtable<String, Object> tempUnSendReplyMessageMap = null;
		if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
			tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
		}
		if(tempUnSendReplyMessageMap==null) {
			tempUnSendReplyMessageMap = new Hashtable<String, Object>();
		}
		tempUnSendReplyMessageMap.put("s"+message.getSerialNumber(), startStopJson);
		unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);
		return true;
	}

}
