package com.tct.mapper;

import com.tct.po.SoftwareVersion;
import com.tct.po.SoftwareVersionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SoftwareVersionMapper {
    int countByExample(SoftwareVersionExample example);

    int deleteByExample(SoftwareVersionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SoftwareVersion record);

    int insertSelective(SoftwareVersion record);

    List<SoftwareVersion> selectByExample(SoftwareVersionExample example);

    SoftwareVersion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SoftwareVersion record, @Param("example") SoftwareVersionExample example);

    int updateByExample(@Param("record") SoftwareVersion record, @Param("example") SoftwareVersionExample example);

    int updateByPrimaryKeySelective(SoftwareVersion record);

    int updateByPrimaryKey(SoftwareVersion record);
}