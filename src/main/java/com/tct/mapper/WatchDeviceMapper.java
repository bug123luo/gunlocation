package com.tct.mapper;

import com.tct.po.WatchDevice;
import com.tct.po.WatchDeviceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WatchDeviceMapper {
    long countByExample(WatchDeviceExample example);

    int deleteByExample(WatchDeviceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WatchDevice record);

    int insertSelective(WatchDevice record);

    List<WatchDevice> selectByExample(WatchDeviceExample example);

    WatchDevice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WatchDevice record, @Param("example") WatchDeviceExample example);

    int updateByExample(@Param("record") WatchDevice record, @Param("example") WatchDeviceExample example);

    int updateByPrimaryKeySelective(WatchDevice record);

    int updateByPrimaryKey(WatchDevice record);
}