package com.tct.service.impl;

import java.util.Date;
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
import com.tct.cache.SessionMessageCache;
import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.codec.pojo.ClientHeartBeatMessage;
import com.tct.codec.pojo.ClientHeartBeatReplyBody;
import com.tct.codec.pojo.ClientHeartBeatReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="clientHeartBeatService")
public class ClientHeartBeatServiceImpl implements SimpleService {

	@Autowired
	ClientHeartBeatDao clientHeartBeatDao;
	
	@Autowired
	ClientDeviceBindingDao clientDeviceBindingDao;
		
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		ClientHeartBeatMessage message = (ClientHeartBeatMessage)msg;
		
		if(message.getMessageBody().getBluetoothMac()==null || message.getMessageBody().getBluetoothMac().length()<0) {
			log.info("心跳报文 参数blueMac为空，请网关检查心跳报文数据是否正常 ");
			return false;
		}
		
		//查找枪支的 deviceNo
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(message.getMessageBody().getBluetoothMac());
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= clientHeartBeatDao.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		
		if(deviceGunCustom==null) {
			log.info("There is no record in device gun!");
			return false;
		}
		
		//将位置信息插入在device_location表中，在将gun表中小区代码字段更新
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		GunCustom gunCustom = new GunCustom();
		Date date = StringUtil.getDate(message.getSendTime());
		deviceLocationCustom.setCreateTime(date);
		deviceLocationCustom.setDeviceNo(deviceGunCustom.getDeviceNo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setUpdateTime(date);
		gunCustom.setBluetoothMac(deviceGunCustom.getGunMac());
		boolean flag=clientHeartBeatDao.updateDeviceLocation(deviceLocationCustom, gunCustom);
		
		return false;
	}

}
