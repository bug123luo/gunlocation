package com.tct.mapper;

import com.tct.po.ServerMessageSerialnumberCustom;

public interface ServerMessageSerialnumberCustomMapper {
	ServerMessageSerialnumberCustom selectMaxIdAndSerialNumber() throws Exception;
}
