package com.tct.dao;

import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceQueryVo;

public interface ClientInWareHouseDao {
	boolean updateDeviceInWareHouseState(DeviceLocationCustom deviceLocationCustom, DeviceGunCustom deviceGunCustom, DeviceQueryVo deviceQueryVo);
}
