package com.tct.service.impl;

import org.springframework.stereotype.Service;

import com.tct.service.SimpleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="serverOutWareHouseReplyService")
public class ServerOutWareHouseReplyServiceImpl implements SimpleService {

	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		boolean flag = false;
		flag = true;
		return flag;
	}

}
