package com.tct.mapper;

import com.tct.po.ServerMessageRecord;
import com.tct.po.ServerMessageRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServerMessageRecordMapper {
    long countByExample(ServerMessageRecordExample example);

    int deleteByExample(ServerMessageRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServerMessageRecord record);

    int insertSelective(ServerMessageRecord record);

    List<ServerMessageRecord> selectByExampleWithBLOBs(ServerMessageRecordExample example);

    List<ServerMessageRecord> selectByExample(ServerMessageRecordExample example);

    ServerMessageRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServerMessageRecord record, @Param("example") ServerMessageRecordExample example);

    int updateByExampleWithBLOBs(@Param("record") ServerMessageRecord record, @Param("example") ServerMessageRecordExample example);

    int updateByExample(@Param("record") ServerMessageRecord record, @Param("example") ServerMessageRecordExample example);

    int updateByPrimaryKeySelective(ServerMessageRecord record);

    int updateByPrimaryKeyWithBLOBs(ServerMessageRecord record);

    int updateByPrimaryKey(ServerMessageRecord record);
}