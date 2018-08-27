package com.tct.mapper;

import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;

public interface DeviceGunCustomMapper {
	
	DeviceGunCustom selectByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	DeviceGunCustom selectDeviceNoByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	DeviceGunCustom selectByDeviceNo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	int selectCount(String gunMac) throws Exception;
	
	int updateByDeviceGunCustom(DeviceGunCustom deviceGunCustom) throws Exception;
	
	int insertSelective(DeviceGunCustom deviceGunCustom) throws Exception;
}
