package com.tct.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tct.dao.ServerInWareHouseDao;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Transactional
public class ServerInWareHouseDaoImpl implements ServerInWareHouseDao {

	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Override
	public DeviceGunCustom selectByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception {
		DeviceGunCustom deviceGunCustom = deviceGunCustomMapper.selectByDeviceGunQueryVo(deviceGunQueryVo);
		return deviceGunCustom;
	}
}
