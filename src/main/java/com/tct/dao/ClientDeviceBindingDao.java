package com.tct.dao;

import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;

public interface ClientDeviceBindingDao {
	public boolean updateDeviceBindingState(DeviceLocationCustom deviceLocationCustom,GunCustom gunCustom)throws Exception;
}
