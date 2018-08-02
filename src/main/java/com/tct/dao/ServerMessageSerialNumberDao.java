package com.tct.dao;

import com.tct.po.ServerMessageSerialnumberCustom;

public interface ServerMessageSerialNumberDao {
	ServerMessageSerialnumberCustom selectMaxIdAndSerialNumber() throws Exception;
}
