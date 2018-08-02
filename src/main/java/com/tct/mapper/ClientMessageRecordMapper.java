package com.tct.mapper;

import com.tct.po.ClientMessageRecord;
import com.tct.po.ClientMessageRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClientMessageRecordMapper {
    long countByExample(ClientMessageRecordExample example);

    int deleteByExample(ClientMessageRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClientMessageRecord record);

    int insertSelective(ClientMessageRecord record);

    List<ClientMessageRecord> selectByExampleWithBLOBs(ClientMessageRecordExample example);

    List<ClientMessageRecord> selectByExample(ClientMessageRecordExample example);

    ClientMessageRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClientMessageRecord record, @Param("example") ClientMessageRecordExample example);

    int updateByExampleWithBLOBs(@Param("record") ClientMessageRecord record, @Param("example") ClientMessageRecordExample example);

    int updateByExample(@Param("record") ClientMessageRecord record, @Param("example") ClientMessageRecordExample example);

    int updateByPrimaryKeySelective(ClientMessageRecord record);

    int updateByPrimaryKeyWithBLOBs(ClientMessageRecord record);

    int updateByPrimaryKey(ClientMessageRecord record);
}