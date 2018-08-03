package com.tct.dao;

import com.tct.po.DeviceCustom;
import com.tct.po.DeviceQueryVo;

public interface AuthCodeDao {
	Boolean findDeviceUserAndUpdateLocation(Object obj) throws Exception;
	
	DeviceCustom findByDeviceQueryVo(DeviceQueryVo deviceQueryVo) throws Exception;
}
