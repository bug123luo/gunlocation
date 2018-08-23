package com.tct.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tct.dao.ClientDeviceBindingDao;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;

import lombok.extern.slf4j.Slf4j;


@Component
public class ClientDeviceBindingDaoImpl implements ClientDeviceBindingDao {
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	GunCustomMapper gunCustomMapper;

	@Transactional
	@Override
	public boolean updateDeviceBindingState(DeviceLocationCustom deviceLocationCustom, GunCustom gunCustom)
			throws Exception {
		
		deviceLocationCustomMapper.insertSelective(deviceLocationCustom);
		gunCustomMapper.updateSelective(gunCustom);
		
		return true;

	}

	@Override
	public GunCustom selectBybluetoothMac(GunQueryVo gunQueryVo) throws Exception {
		return gunCustomMapper.selectBybluetoothMac(gunQueryVo);
	}
	
	
	

}
