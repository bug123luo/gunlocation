package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.AuthCodeMessageBody;

public class AuthCodeMessageCodec implements MessageCodec{

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		AuthCodeMessage authCodeMessage =  new AuthCodeMessage();
		authCodeMessage.setServiceType(json.getString("serviceType"));
		authCodeMessage.setFormatVersion(json.getString("formatVersion"));
		authCodeMessage.setDeviceType(json.getInteger("deviceType"));
		authCodeMessage.setSerialNumber(json.getString("serialNumber"));
		authCodeMessage.setMessageType(json.getString("messageType"));
		authCodeMessage.setSendTime(json.getString("sendTime"));
		authCodeMessage.setMessageBody(json.getObject("messageBody", AuthCodeMessageBody.class));
		
		//authCodeMessage.setMessageBody((AuthCodeMessageBody)json.get("messageBody"));
		
		return authCodeMessage;
	}

	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
