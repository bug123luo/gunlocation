package com.tct.mapper;

import com.tct.po.DeviceQueryVo;

public interface DeviceCustomMapper {
    int selectByDeviceQueryVo(DeviceQueryVo deviceQueryVo) throws Exception;
}