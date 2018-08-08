package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientHeartBeatBody;
import com.tct.codec.pojo.ClientHeartBeatMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHeartBeatMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);

		//log.info(json.toJSONString());
		
		ClientHeartBeatMessage clientHeartBeatMessage = new ClientHeartBeatMessage();
		clientHeartBeatMessage.setMessageBody(json.getObject("messageBody",ClientHeartBeatBody.class));
		clientHeartBeatMessage.setDeviceType(json.getInteger("deviceType"));
		clientHeartBeatMessage.setServiceType(json.getString("serviceType"));
		clientHeartBeatMessage.setFormatVersion(json.getString("formatVersion"));
		clientHeartBeatMessage.setSerialNumber(json.getString("serialNumber"));
		clientHeartBeatMessage.setMessageType(json.getString("messageType"));
		clientHeartBeatMessage.setSendTime(json.getString("sendTime"));
		clientHeartBeatMessage.setSessionToken(json.getString("sessionToken"));
		
		return clientHeartBeatMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
