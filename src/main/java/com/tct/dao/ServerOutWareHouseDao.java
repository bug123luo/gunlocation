package com.tct.dao;

import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;

public interface ServerOutWareHouseDao {
	DeviceGunCustom selectByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
}
