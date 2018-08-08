package com.tct.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tct.dao.ClientInWareHouseDao;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;

import lombok.extern.slf4j.Slf4j;


@Component
public class ClientInWareHouseDaoImpl implements ClientInWareHouseDao {
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Override
	@Transactional
	public boolean updateDeviceInWareHouseState(DeviceLocationCustom deviceLocationCustom,
			DeviceGunCustom deviceGunCustom) {
		try {
			deviceLocationCustomMapper.insertSelective(deviceLocationCustom);
			deviceGunCustomMapper.updateByDeviceGunCustom(deviceGunCustom);
			GunCustom gunCustom =  new GunCustom();
			gunCustom.setBluetoothMac(deviceGunCustom.getGunMac());
			gunCustom.setState(1);
			gunCustom.setRealTimeState(1);
			gunCustomMapper.updateSelective(gunCustom);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	}


	
}
