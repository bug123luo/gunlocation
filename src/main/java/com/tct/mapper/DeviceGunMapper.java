package com.tct.mapper;

import com.tct.po.DeviceGun;
import com.tct.po.DeviceGunExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceGunMapper {
    long countByExample(DeviceGunExample example);

    int deleteByExample(DeviceGunExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DeviceGun record);

    int insertSelective(DeviceGun record);

    List<DeviceGun> selectByExample(DeviceGunExample example);

    DeviceGun selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DeviceGun record, @Param("example") DeviceGunExample example);

    int updateByExample(@Param("record") DeviceGun record, @Param("example") DeviceGunExample example);

    int updateByPrimaryKeySelective(DeviceGun record);

    int updateByPrimaryKey(DeviceGun record);
}