package com.tct.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverInWareHouseReplyService")
public class ServerInWareHouseReplyServiceImpl implements ServerInWareHouseReplyService {

	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		boolean flag =false;
		flag = true;
		return flag;
	}

}
