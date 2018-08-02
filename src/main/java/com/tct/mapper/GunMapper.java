package com.tct.mapper;

import com.tct.po.Gun;
import com.tct.po.GunExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GunMapper {
    long countByExample(GunExample example);

    int deleteByExample(GunExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Gun record);

    int insertSelective(Gun record);

    List<Gun> selectByExample(GunExample example);

    Gun selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Gun record, @Param("example") GunExample example);

    int updateByExample(@Param("record") Gun record, @Param("example") GunExample example);

    int updateByPrimaryKeySelective(Gun record);

    int updateByPrimaryKey(Gun record);
}