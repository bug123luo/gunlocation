package com.tct.dao;

import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;

public interface ClientHeartBeatDao {
	DeviceGunCustom selectDeviceNoByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	boolean updateDeviceLocation(DeviceLocationCustom deviceLocationCustom,GunCustom gunCustom)throws Exception;
	
}
