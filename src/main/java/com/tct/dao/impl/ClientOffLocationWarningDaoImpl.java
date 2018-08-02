package com.tct.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tct.dao.ClientOffLocationWarningDao;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.mapper.SosMessageCustomMapper;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.SosMessageCustom;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Transactional
public class ClientOffLocationWarningDaoImpl implements ClientOffLocationWarningDao{

	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Autowired
	SosMessageCustomMapper sosMessageCustomMapper;
	
	@Override
	public boolean updateClientOffLocationWaring(DeviceLocationCustom deviceLocationCustom,GunCustom gunCustom,SosMessageCustom sosMessageCustom) throws Exception {
		
		deviceLocationCustomMapper.insertSelective(deviceLocationCustom);
		
		gunCustomMapper.updateSelective(gunCustom);
		
		sosMessageCustomMapper.insertSelective(sosMessageCustom);
		
		return false;
	}

}
