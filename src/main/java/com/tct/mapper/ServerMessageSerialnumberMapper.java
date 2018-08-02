package com.tct.mapper;

import com.tct.po.ServerMessageSerialnumber;
import com.tct.po.ServerMessageSerialnumberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServerMessageSerialnumberMapper {
    long countByExample(ServerMessageSerialnumberExample example);

    int deleteByExample(ServerMessageSerialnumberExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServerMessageSerialnumber record);

    int insertSelective(ServerMessageSerialnumber record);

    List<ServerMessageSerialnumber> selectByExample(ServerMessageSerialnumberExample example);

    ServerMessageSerialnumber selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServerMessageSerialnumber record, @Param("example") ServerMessageSerialnumberExample example);

    int updateByExample(@Param("record") ServerMessageSerialnumber record, @Param("example") ServerMessageSerialnumberExample example);

    int updateByPrimaryKeySelective(ServerMessageSerialnumber record);

    int updateByPrimaryKey(ServerMessageSerialnumber record);
}