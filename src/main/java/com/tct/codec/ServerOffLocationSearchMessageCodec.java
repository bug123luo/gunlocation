package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ServerOffLocationSearchBody;
import com.tct.codec.pojo.ServerOffLocationSearchMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerOffLocationSearchMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		log.info(json.toJSONString());
		
		ServerOffLocationSearchMessage serverOffLocationSearchMessage = new ServerOffLocationSearchMessage();
		serverOffLocationSearchMessage.setMessageBody(json.getObject("messageBody",ServerOffLocationSearchBody.class));
		serverOffLocationSearchMessage.setServiceType(json.getString("serviceType"));
		serverOffLocationSearchMessage.setFormatVersion(json.getString("formatVersion"));
		serverOffLocationSearchMessage.setDeviceType(json.getInteger("deviceType"));
		serverOffLocationSearchMessage.setSerialNumber(json.getString("serialNumber"));
		serverOffLocationSearchMessage.setMessageType(json.getString("messageType"));
		serverOffLocationSearchMessage.setSendTime(json.getString("sendTime"));
		serverOffLocationSearchMessage.setSessionToken(json.getString("sessionToken"));
		
		return serverOffLocationSearchMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
