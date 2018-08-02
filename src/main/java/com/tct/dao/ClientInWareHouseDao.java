package com.tct.dao;

import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceLocationCustom;

public interface ClientInWareHouseDao {
	boolean updateDeviceInWareHouseState(DeviceLocationCustom deviceLocationCustom, DeviceGunCustom deviceGunCustom);
}
