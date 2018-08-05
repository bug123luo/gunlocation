package com.tct.dao;

import com.tct.po.DeviceCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceQueryVo;

public interface AuthCodeDao {
	Boolean updateDeviceLocation(DeviceLocationCustom deviceLocationCustom) throws Exception;
	
	DeviceCustom findByDeviceQueryVo(DeviceQueryVo deviceQueryVo) throws Exception;
}
