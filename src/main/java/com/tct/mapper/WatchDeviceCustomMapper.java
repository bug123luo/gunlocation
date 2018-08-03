package com.tct.mapper;

import com.tct.po.WatchDeviceCustom;
import com.tct.po.WatchDeviceQueryVo;

public interface WatchDeviceCustomMapper {
	WatchDeviceCustom selectByWatchDeviceQueryVo(WatchDeviceQueryVo watchDeviceQueryVo) throws Exception;
}
