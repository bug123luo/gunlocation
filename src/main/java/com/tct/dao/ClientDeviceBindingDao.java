package com.tct.dao;

import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;

public interface ClientDeviceBindingDao {
	boolean updateDeviceBindingState(DeviceLocationCustom deviceLocationCustom,GunCustom gunCustom)throws Exception;
	
	GunCustom selectBybluetoothMac(GunQueryVo gunQueryVo)throws Exception;
}
