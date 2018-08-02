package com.tct.dao;

import com.tct.po.DeviceLocationCustom;
import com.tct.po.GunCustom;
import com.tct.po.SosMessageCustom;

public interface ClientOffLocationWarningDao {
	boolean updateClientOffLocationWaring(DeviceLocationCustom deviceLocationCustom,GunCustom gunCustom,SosMessageCustom sosMessageCustom)throws Exception;
}
