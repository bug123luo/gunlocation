package com.tct.mapper;

import com.tct.po.DeviceCustom;
import com.tct.po.DeviceQueryVo;

public interface DeviceCustomMapper {
	
	DeviceCustom selectByDeviceQueryVo(DeviceQueryVo deviceQueryVo) throws Exception;
    
    int updateByDeviceQueryVo(DeviceQueryVo deviceQueryVo) throws Exception;
    
    int insertSelective(DeviceCustom deviceCustom) throws Exception;
}