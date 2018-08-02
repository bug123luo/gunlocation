package com.tct.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tct.dao.ClientHeartBeatDao;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Transactional
public class ClientHeartBeatDaoImpl implements ClientHeartBeatDao {

	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Override
	public DeviceGunCustom selectDeviceNoByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception {
		DeviceGunCustom deviceGunCustom = deviceGunCustomMapper.selectDeviceNoByDeviceGunQueryVo(deviceGunQueryVo);
		return deviceGunCustom;
	}

	@Override
	public boolean updateDeviceLocation(DeviceLocationCustom deviceLocationCustom, GunCustom gunCustom)
			throws Exception {
		deviceLocationCustomMapper.insertSelective(deviceLocationCustom);
		gunCustomMapper.updateSelective(gunCustom);
		return true;
	}





}
