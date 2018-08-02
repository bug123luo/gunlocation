package com.tct.mapper;

import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceLocationQueryVo;


public interface DeviceLocationCustomMapper {
						 
	DeviceLocationCustom selectByDeviceLocationQueryVo(DeviceLocationQueryVo deviceLocationQueryVo) throws Exception;
	
	int insertSelective(DeviceLocationCustom deviceLocationCustom) throws Exception;
	
	int updateByPrimaryKeySelective(DeviceLocationCustom deviceLocationCustom) throws Exception;
}