package com.tct.service.impl;

import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.apache.activemq.console.command.store.proto.MapEntryPB.Bean;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.SessionMessageCache;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ClientInWareHouseMessage;
import com.tct.codec.pojo.ClientInWareHouseReplyBody;
import com.tct.codec.pojo.ClientInWareHouseReplyMessage;
import com.tct.codec.pojo.ServerInWareHouseBody;
import com.tct.codec.pojo.ServerInWareHouseReplyBody;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.codec.pojo.SimpleMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.dao.ClientInWareHouseDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.jms.producer.WebTopicSender;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceQueryVo;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientInWareHouseService")
public class ClientInWareHouseServiceImpl implements SimpleService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientInWareHouseDao clientInWareHouseDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
		
	@Resource
	private OutQueueSender outQueueSender;
	
	@Resource
	private WebOutQueueSender webOutQueueSender;
	
	@Resource
	private WebTopicSender webTopicSender;
	
	@Resource
	@Qualifier("topicDestination")
	private Destination webtopicDestination;
	
	@Resource
	@Qualifier("outQueueDestination")
	private Destination outQueueDestination;
	
	@Resource
	@Qualifier("webOutQueueDestination")
	private Destination webOutQueueDestination;
		
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientInWareHouseMessage message = (ClientInWareHouseMessage)msg;
		boolean flag=false;
		ConcurrentHashMap<String, String> deviceNoBingingWebUserCache = DeviceNoBingingWebUserCache.getDeviceNoWebUserHashMap();
		
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		if (deviceGunCustom==null) {
			log.info("枪支未出库，无法在device_gun表中找到相应的记录");
			return false;
		}
					
		if (Integer.parseInt(message.getMessageBody().getReserve())==1) {
			DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
			deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
			deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
			deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
			deviceLocationCustom.setCreateTime(StringUtil.getDate(message.getSendTime()));
			deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getSendTime()));
			deviceLocationCustom.setState(1);
			DeviceGunCustom deviceGunCustom2 = new DeviceGunCustom();
			deviceGunCustom2.setGunMac(message.getMessageBody().getBluetoothMac());
			deviceGunCustom2.setInWarehouseTime(new Date());
			deviceGunCustom2.setState(1);
			DeviceCustom deviceCustom =  new DeviceCustom();
			deviceCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
			deviceCustom.setState(1);
			DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
			deviceQueryVo.setDeviceCustom(deviceCustom);
			
			flag = clientInWareHouseDao.updateDeviceInWareHouseState(deviceLocationCustom, deviceGunCustom2,deviceQueryVo);
			
			ClientInWareHouseReplyMessage clientInWareHouseReplyMessage =  new ClientInWareHouseReplyMessage();
			ClientInWareHouseReplyBody clientInWareHouseReplyBody = new ClientInWareHouseReplyBody();
			clientInWareHouseReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientInWareHouseReplyBody.setReserve(Integer.toString(1));
			
			clientInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			clientInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			clientInWareHouseReplyMessage.setMessageType("12");
			clientInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			clientInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			clientInWareHouseReplyMessage.setServiceType(message.getServiceType());
			clientInWareHouseReplyMessage.setMessageBody(clientInWareHouseReplyBody);
			clientInWareHouseReplyMessage.setSessionToken(message.getSessionToken());
			
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientInWareHouseReplyMessage, simpleReplyMessage);
			String replyBody =StringConstant.MSG_BODY_PREFIX+clientInWareHouseReplyBody.getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+clientInWareHouseReplyBody.getAuthCode()
				+StringConstant.MSG_BODY_SUFFIX;
			simpleReplyMessage.setMessageBody(replyBody);
			String clientInWareHouseReplyjson = JSONObject.toJSONString(simpleReplyMessage);
			log.info("Client In WareHouse Reply send to {}",deviceGunCustom.getDeviceNo());
			outQueueSender.sendMessage(outQueueDestination, clientInWareHouseReplyjson);
			//将APP回应消息放进消息缓存队列中
/*			String toClientQue = userOnlineQueueHashMap.get("NettyServer").get("nettySendQue");
			Hashtable<String, Object> tempUnSendReplyMessageMap = null;
			if(unSendReplyMessageHashMap.containsKey(toClientQue)) {
				tempUnSendReplyMessageMap = unSendReplyMessageHashMap.get(toClientQue);
			}
			if(tempUnSendReplyMessageMap==null) {
				tempUnSendReplyMessageMap = new Hashtable<String, Object>();
			}
			tempUnSendReplyMessageMap.put(message.getSerialNumber(), clientInWareHouseReplyjson);
			unSendReplyMessageHashMap.put(toClientQue, tempUnSendReplyMessageMap);*/
			
			
			//将向服务器的发送消息放在缓存队列中
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage =  new ServerInWareHouseReplyMessage();
			ServerInWareHouseReplyBody serverInWareHouseReplyBody = new ServerInWareHouseReplyBody();
			serverInWareHouseReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverInWareHouseReplyBody.setGunTag(gunCustom2.getGunTag());
			serverInWareHouseReplyBody.setState(Integer.toString(1));
			
			serverInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			serverInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			serverInWareHouseReplyMessage.setMessageType("12");
			serverInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			serverInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());
			serverInWareHouseReplyMessage.setServiceType(message.getServiceType());
			serverInWareHouseReplyMessage.setMessageBody(serverInWareHouseReplyBody);
			serverInWareHouseReplyMessage.setSessionToken(message.getSessionToken());
			if(deviceNoBingingWebUserCache.get(deviceGunCustom.getDeviceNo())==null){
				serverInWareHouseReplyMessage.setUserName("1");
			}else {
				serverInWareHouseReplyMessage.setUserName(deviceNoBingingWebUserCache.get(deviceGunCustom.getDeviceNo()));
			}			
			String serverInWareReplyJson = JSONObject.toJSONString(serverInWareHouseReplyMessage);
			log.info("The {} Client In WareHouse Reply send to WebServer",deviceGunCustom.getDeviceNo());
			webTopicSender.sendMessage(webtopicDestination, serverInWareReplyJson);
			deviceNoBingingWebUserCache.remove(deviceGunCustom.getDeviceNo());

			//webOutQueueSender.sendMessage(webOutQueueDestination, serverInWareReplyJson);
			flag = true;
		}else {
			ClientInWareHouseReplyMessage clientInWareHouseReplyMessage =  new ClientInWareHouseReplyMessage();
			ClientInWareHouseReplyBody clientInWareHouseReplyBody = new ClientInWareHouseReplyBody();
			clientInWareHouseReplyBody.setAuthCode(message.getMessageBody().getAuthCode());
			clientInWareHouseReplyBody.setReserve(Integer.toString(0));
			
			clientInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			clientInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			clientInWareHouseReplyMessage.setMessageType("12");
			clientInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			clientInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			clientInWareHouseReplyMessage.setServiceType(message.getServiceType());
			clientInWareHouseReplyMessage.setMessageBody(clientInWareHouseReplyBody);
			clientInWareHouseReplyMessage.setSessionToken(message.getSessionToken());
			
			SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
			BeanUtils.copyProperties(clientInWareHouseReplyMessage, simpleReplyMessage);
			String replyBody =StringConstant.MSG_BODY_PREFIX+clientInWareHouseReplyBody.getReserve()
				+StringConstant.MSG_BODY_SEPARATOR+clientInWareHouseReplyBody.getAuthCode()
				+StringConstant.MSG_BODY_SUFFIX;
			
			simpleReplyMessage.setMessageBody(replyBody);
			String clientInWareHouseReplyjson = JSONObject.toJSONString(simpleReplyMessage);
			log.info("Client In WareHouse Reply send to {}",deviceGunCustom.getDeviceNo());
			outQueueSender.sendMessage(outQueueDestination, clientInWareHouseReplyjson);			
			//将向服务器的发送消息放在缓存队列中
			GunCustom gunCustom2 = new GunCustom();
			GunQueryVo gunQueryVo = new GunQueryVo();
			gunCustom2.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			gunQueryVo.setGunCustom(gunCustom2);
			gunCustom2 = clientDeviceBindingDao.selectBybluetoothMac(gunQueryVo);
			
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage =  new ServerInWareHouseReplyMessage();
			ServerInWareHouseReplyBody serverInWareHouseReplyBody = new ServerInWareHouseReplyBody();
			serverInWareHouseReplyBody.setDeviceNo(deviceGunCustom.getDeviceNo());
			serverInWareHouseReplyBody.setGunTag(gunCustom2.getGunTag());
			serverInWareHouseReplyBody.setState(Integer.toString(0));
			
			serverInWareHouseReplyMessage.setDeviceType(message.getDeviceType());
			serverInWareHouseReplyMessage.setFormatVersion(message.getFormatVersion());
			serverInWareHouseReplyMessage.setMessageType("12");
			serverInWareHouseReplyMessage.setSendTime(StringUtil.getDateString());
			serverInWareHouseReplyMessage.setSerialNumber(message.getSerialNumber());;
			serverInWareHouseReplyMessage.setServiceType(message.getServiceType());
			serverInWareHouseReplyMessage.setMessageBody(serverInWareHouseReplyBody);
			serverInWareHouseReplyMessage.setSessionToken(message.getSessionToken());
			if(deviceNoBingingWebUserCache.get(deviceGunCustom.getDeviceNo())==null){
				serverInWareHouseReplyMessage.setUserName("1");
			}else {
				serverInWareHouseReplyMessage.setUserName(deviceNoBingingWebUserCache.get(deviceGunCustom.getDeviceNo()));
			}
			
			String serverInWareReplyJson = JSONObject.toJSONString(serverInWareHouseReplyMessage);
			log.info("The {} Client In WareHouse Reply send to WebServer",deviceGunCustom.getDeviceNo());
			webTopicSender.sendMessage(webtopicDestination, serverInWareReplyJson);
			deviceNoBingingWebUserCache.remove(deviceGunCustom.getDeviceNo());
			//webOutQueueSender.sendMessage(webOutQueueDestination, serverInWareReplyJson);			
			flag = true;
		}
		
		return false;
	}

}
