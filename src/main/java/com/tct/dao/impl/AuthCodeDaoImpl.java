package com.tct.dao.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.dao.AuthCodeDao;
import com.tct.mapper.DeviceCustomMapper;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.mapper.WatchDeviceCustomMapper;
import com.tct.po.DeviceCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceLocationQueryVo;
import com.tct.po.DeviceQueryVo;
import com.tct.po.WatchDeviceCustom;
import com.tct.po.WatchDeviceQueryVo;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthCodeDaoImpl implements AuthCodeDao{

	@Autowired
	DeviceCustomMapper deviceCustomMapper;
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	WatchDeviceCustomMapper watchDeviceCustomMapper;
		
	@Transactional
	public Boolean updateDeviceLocation(DeviceLocationCustom deviceLocationCustom) {
		
		Boolean flag=false;
			
		DeviceLocationQueryVo deviceLocationQueryVo = new DeviceLocationQueryVo();
		DeviceLocationCustom deviceLocationCustom3 = new DeviceLocationCustom();
		DeviceLocationCustom deviceLocationCustom2= null;
		BeanUtils.copyProperties(deviceLocationCustom, deviceLocationCustom3);
		deviceLocationQueryVo.setDeviceLocationCustom(deviceLocationCustom3);
			
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
	@Transactional
	public DeviceCustom findByDeviceQueryVo(DeviceQueryVo deviceQueryVo) {
		
		DeviceQueryVo deviceQueryVo2 = deviceQueryVo;
		DeviceCustom deviceCustom=null;
		try {
			deviceCustom = deviceCustomMapper.selectByDeviceQueryVo(deviceQueryVo2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (deviceCustom==null) {
			WatchDeviceQueryVo watchDeviceQueryVo = new WatchDeviceQueryVo();
			WatchDeviceCustom watchDeviceCustom = new WatchDeviceCustom();
			watchDeviceCustom.setDeviceNo(deviceQueryVo.getDeviceCustom().getDeviceNo());
			watchDeviceCustom.setPassword(deviceQueryVo.getDeviceCustom().getPassword());
			WatchDeviceCustom watchDeviceCustom2=null;
			try {
				watchDeviceCustom2 = watchDeviceCustomMapper.selectByWatchDeviceQueryVo(watchDeviceQueryVo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (watchDeviceCustom2!=null) {
				deviceCustom = new DeviceCustom();
				BeanUtils.copyProperties(watchDeviceCustom2, deviceCustom);
				try {
					deviceCustomMapper.insertSelective(deviceCustom);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return deviceCustom;
	}

	/**   
	 * <p>Title: updateDevice</p>   
	 * <p>Description: </p>   
	 * @param deviceCustom
	 * @return
	 * @throws Exception   
	 * @see com.tct.dao.AuthCodeDao#updateDevice(com.tct.po.DeviceCustom)   
	 */
	@Override
	public Boolean updateDevice(DeviceCustom deviceCustom) throws Exception {
		DeviceQueryVo deviceQueryVo = new DeviceQueryVo();
		deviceQueryVo.setDeviceCustom(deviceCustom);
		deviceCustomMapper.updateByDeviceQueryVo(deviceQueryVo);
		return true;
	}
	
}
