package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientOffLocationWarningBody;
import com.tct.codec.pojo.ClientOffLocationWarningMessage;

public class ClientOffLocationWarningMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ClientOffLocationWarningMessage clientOffLocationWarningMessage = new ClientOffLocationWarningMessage();
		clientOffLocationWarningMessage.setMessageBody(json.getObject("messageBody",ClientOffLocationWarningBody.class));
		clientOffLocationWarningMessage.setServiceType(json.getString("serviceType"));
		clientOffLocationWarningMessage.setFormatVersion(json.getString("formatVersion"));
		clientOffLocationWarningMessage.setDeviceType(json.getInteger("deviceType"));
		clientOffLocationWarningMessage.setSerialNumber(json.getString("serialNumber"));
		clientOffLocationWarningMessage.setMessageType(json.getString("messageType"));
		clientOffLocationWarningMessage.setSendTime(json.getString("sendTime"));
		
		return clientOffLocationWarningMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
