package com.tct.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ServerInWareHouseMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ServerInWareHouseDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverInWareHouseService")
public class ServerInWareHouseServiceImpl implements SimpleService {

	@Autowired
	ServerInWareHouseDao serverInWareHouseDao;
	
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
		ServerInWareHouseMessage message = (ServerInWareHouseMessage)msg;
		
		ConcurrentHashMap<String, SimpleMessage> sessionMessageMap= SessionMessageCache.getSessionMessageMessageMap();
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		ConcurrentHashMap<String, String> deviceNoBingingWebUserCache = DeviceNoBingingWebUserCache.getDeviceNoWebUserHashMap();
		
		//将要入库的设备编号和web用户绑定
		deviceNoBingingWebUserCache.put(message.getMessageBody().getDeviceNo(), message.getUserName());
		
		//数据库中获取需要通知的终端的 deviceNo信息
		DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom.setDeviceNo(message.getMessageBody().getDeviceNo());
		/*DeviceGunCustom deviceGunCustom2 = serverInWareHouseDao.selectByDeviceGunQueryVo(deviceGunQueryVo);*/
		
		if (deviceGunCustom.getDeviceNo()==null) {
			log.info("系统device_gun表中没有用户的出库信息");
			return false;
		}
		
		//将接收到的消息放在本地的接收消息队列上
		String sessionToken = userOnlineSessionCache.get(deviceGunCustom.getDeviceNo());
		
		if(sessionToken==null) {
			log.info("用户没有登录，不允许出库");
			return false;
		}
		
		message.setSessionToken(sessionToken);
		
		SimpleMessage simpleMessage = sessionMessageMap.get(sessionToken);
		
		//修改为[]格式返回到客户端
		SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
		BeanUtils.copyProperties(simpleMessage, simpleReplyMessage);
		simpleReplyMessage.setMessageType(message.getMessageType());
		simpleReplyMessage.setSerialNumber(message.getSerialNumber());
		simpleReplyMessage.setSendTime(message.getSendTime());
		String replyBody = StringConstant.MSG_BODY_PREFIX+message.getMessageBody().getBluetoothMac()
				+StringConstant.MSG_BODY_SEPARATOR+message.getMessageBody().getAuthCode()
				+StringConstant.MSG_BODY_SUFFIX;
		simpleReplyMessage.setMessageBody(replyBody);
		//发送到producer处理队列上
		String strJson = JSONObject.toJSONString(simpleReplyMessage);
		outQueueSender.sendMessage(outQueueDestination, strJson);
		return true;
	}

}
