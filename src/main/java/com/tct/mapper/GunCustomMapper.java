package com.tct.mapper;

import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;

public interface GunCustomMapper {
	int updateSelective(GunCustom gunCustom)throws Exception;
	GunCustom selectBybluetoothMac(GunQueryVo gunQueryVo)throws Exception;
	GunCustom selectByGunTag(GunQueryVo gunQueryVo)throws Exception;
}
