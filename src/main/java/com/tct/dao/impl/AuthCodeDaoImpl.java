package com.tct.dao.impl;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.dao.AuthCodeDao;
import com.tct.mapper.DeviceCustomMapper;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceLocationQueryVo;
import com.tct.po.DeviceQueryVo;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component

public class AuthCodeDaoImpl implements AuthCodeDao{

	@Autowired
	DeviceCustomMapper deviceCustomMapper;
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Transactional
	public Boolean findDeviceUserAndUpdateLocation(Object obj,String deviceNo) {
		
		AuthCodeMessage message = (AuthCodeMessage)obj;
		String tempDeviceNo = deviceNo;
		Boolean flag=false;
		
		DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
		DeviceCustom deviceCustom = new DeviceCustom(); 
		Integer deviceId = null;
		
		Date date=StringUtil.getDate(message.getSendTime());

		deviceCustom.setDeviceNo(tempDeviceNo);
		//deviceCustom.setDeviceName(message.getMessageBody().getUsername());
		deviceQueryVo.setDeviceCustom(deviceCustom);
		
		try {
			deviceId = (Integer) deviceCustomMapper.selectByDeviceQueryVo(deviceQueryVo);
			if (deviceId==null) {
				log.debug("用户不存在");
				return flag;
			}	
		} catch (Exception e1) {
			e1.printStackTrace();
			return flag;
			
		}
		
		
		DeviceLocationQueryVo deviceLocationQueryVo = new DeviceLocationQueryVo();
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		DeviceLocationCustom deviceLocationCustom2= null;
		deviceLocationCustom.setDeviceNo(deviceCustom.getDeviceNo());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setCreateTime(date);
		deviceLocationQueryVo.setDeviceLocationCustom(deviceLocationCustom);
			
		try {
			deviceLocationCustom2 = deviceLocationCustomMapper.selectByDeviceLocationQueryVo(deviceLocationQueryVo);
		} catch (Exception e2) { 
			log.debug("device_location表中没有该用户的记录");
			e2.printStackTrace();
			return flag;
		}
		
		if (deviceLocationCustom2 !=null) {
			try {
				deviceLocationCustomMapper.updateByPrimaryKeySelective(deviceLocationCustom);
			} catch (Exception e3) {
				log.debug("更新device_location表失败");
				flag = false;
			}
			flag = true;
		}else {
			try {
				deviceLocationCustomMapper.insertSelective(deviceLocationCustom);
			} catch (Exception e4) {
				log.debug("插入device_location表失败");	
				e4.printStackTrace();
				flag = false;
			}
			flag = true;
		}
		
		return flag;
	}

	@Override
	public DeviceCustom findByDeviceQueryVo(DeviceQueryVo deviceQueryVo) throws Exception {
		DeviceCustom deviceCustom = deviceCustomMapper.selectDeviceByDeviceQueryVo(deviceQueryVo);
		return deviceCustom;
	}
	
}
