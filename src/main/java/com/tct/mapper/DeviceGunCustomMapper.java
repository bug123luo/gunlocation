package com.tct.mapper;

import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.WatchDeviceCustom;

public interface DeviceGunCustomMapper {
	
	DeviceGunCustom selectByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	DeviceGunCustom selectDeviceNoByDeviceGunQueryVo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	DeviceGunCustom selectByDeviceNo(DeviceGunQueryVo deviceGunQueryVo) throws Exception;
	
	int selectCount(WatchDeviceCustom watchDeviceCustom) throws Exception;
	
	int updateByDeviceGunCustom(DeviceGunCustom deviceGunCustom) throws Exception;
	
	int insertSelective(DeviceGunCustom deviceGunCustom) throws Exception;
}
