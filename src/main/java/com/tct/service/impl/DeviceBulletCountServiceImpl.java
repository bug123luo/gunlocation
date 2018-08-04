package com.tct.service.impl;

import org.springframework.stereotype.Service;

import com.tct.service.SimpleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value="deviceBulletCountService")
public class DeviceBulletCountServiceImpl implements SimpleService {

	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
