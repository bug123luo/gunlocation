package com.tct.mapper;

import com.tct.po.SosMessage;
import com.tct.po.SosMessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SosMessageMapper {
    long countByExample(SosMessageExample example);

    int deleteByExample(SosMessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SosMessage record);

    int insertSelective(SosMessage record);

    List<SosMessage> selectByExample(SosMessageExample example);

    SosMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SosMessage record, @Param("example") SosMessageExample example);

    int updateByExample(@Param("record") SosMessage record, @Param("example") SosMessageExample example);

    int updateByPrimaryKeySelective(SosMessage record);

    int updateByPrimaryKey(SosMessage record);
}