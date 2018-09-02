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

import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.ClientHeartBeatMessage;
import com.tct.dao.ClientDeviceBindingDao;
import com.tct.dao.ClientHeartBeatDao;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.GunCustomMapper;
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
	
	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
		
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		boolean flag = false;
		ClientHeartBeatMessage message = (ClientHeartBeatMessage)msg;
		
		if(message.getMessageBody().getBluetoothMac()==null || message.getMessageBody().getBluetoothMac().length()<0) {
			log.info("心跳报文 参数blueMac为空，请网关检查心跳报文数据是否正常 ");
			return flag;
		}
		
		//查找枪支的 deviceNo
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		String sessionToken=message.getSessionToken();
		String deviceNo=(String)StringUtil.getKey(userOnlineSessionCache, sessionToken);
		if (deviceNo==null) {
			log.info("session 中没有对应的 deviceNo 信息");
			return flag;
		}
		//将位置信息插入在device_location表中，在将gun表中小区代码字段更新
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		Date date = StringUtil.getDate(message.getSendTime());
		deviceLocationCustom.setCreateTime(date);
		deviceLocationCustom.setDeviceNo(deviceNo);
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setUpdateTime(date);
		
		//查找用户是否绑定枪支出库
		DeviceGunQueryVo deviceGunQueryVo =  new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setGunMac(deviceNo);
		deviceGunQueryVo.setDeviceGunCustom(deviceGunCustom);
		deviceGunCustom= deviceGunCustomMapper.selectByDeviceNo(deviceGunQueryVo);
		
		if(deviceGunCustom==null) {
			log.info("用户并未绑定枪支出库");
			deviceLocationCustom.setState(1);
		}else {
			deviceLocationCustom.setState(0);
		}
		
		int i=clientHeartBeatDao.insertDeviceLocation(deviceLocationCustom);
		
		if (message.getMessageBody().getState().equals("0")) {
			GunCustom gunCustom = new GunCustom();
			gunCustom.setBluetoothMac(message.getMessageBody().getBluetoothMac());
			if(Integer.parseInt(message.getMessageBody().getRealTimeState())==1) {
				log.info("client realTimeState is {}",message.getMessageBody().getRealTimeState());
				gunCustom.setRealTimeState(0);
			}else if (Integer.parseInt(message.getMessageBody().getRealTimeState())==0) {
				log.info("client realTimeState is {}",message.getMessageBody().getRealTimeState());
				gunCustom.setRealTimeState(1);
			}else if(Integer.parseInt(message.getMessageBody().getRealTimeState())==2){
				log.info("client realTimeState is {}",message.getMessageBody().getRealTimeState());
				gunCustom.setRealTimeState(2);
			}else {
				log.info("client realTimeState is {}",message.getMessageBody().getRealTimeState());
			}
		
			int j=gunCustomMapper.updateSelective(gunCustom);
		}
		

		
		if(i>0) {
			flag = true;
		}else {
			flag = false;
		}
		
		return flag;
	}

}
