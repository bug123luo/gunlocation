package com.tct.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tct.dao.ServerMessageSerialNumberDao;
import com.tct.mapper.ServerMessageSerialnumberCustomMapper;
import com.tct.po.ServerMessageSerialnumberCustom;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Transactional
public class ServerMessageSerialNumberDaoImpl implements ServerMessageSerialNumberDao {

	@Autowired
	ServerMessageSerialnumberCustomMapper serverMessageSerialnumberCustomMapper; 
	
	@Override
	public ServerMessageSerialnumberCustom selectMaxIdAndSerialNumber() throws Exception {
		ServerMessageSerialnumberCustom serialnumberCustom =  serverMessageSerialnumberCustomMapper.selectMaxIdAndSerialNumber();
		return serialnumberCustom;
	}

}
