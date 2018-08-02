package com.tct.mapper;

import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;

public interface DeviceGunCustomMapper {
	
	DeviceGunCustom selectByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	DeviceGunCustom selectDeviceNoByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
}
