package com.tct.mapper;

import com.tct.po.SoftwareVersionCustom;
import com.tct.po.SoftwareVersionQueryVo;

public interface SoftwareVersionCustomMapper {
	SoftwareVersionCustom selectBySoftwareVersionQueryVo(SoftwareVersionQueryVo softwareVersionQueryVo)throws Exception;
}
