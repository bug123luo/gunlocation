package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientDeviceBindingBody;
import com.tct.codec.pojo.ClientDeviceBindingMessage;

public class ClientDeviceBindingMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		ClientDeviceBindingMessage clientDeviceBindingMessage = new ClientDeviceBindingMessage();
		clientDeviceBindingMessage.setDeviceType(json.getInteger("deviceType"));
		clientDeviceBindingMessage.setFormatVersion(json.getString("formatVersion"));
		clientDeviceBindingMessage.setMessageType(json.getString("messageType"));
		clientDeviceBindingMessage.setSendTime(json.getString("sendTime"));
		clientDeviceBindingMessage.setSerialNumber(json.getString("serialNumber"));
		clientDeviceBindingMessage.setServiceType(json.getString("serviceType"));
		clientDeviceBindingMessage.setMessageBody(json.getObject("messageBody",ClientDeviceBindingBody.class));
		return clientDeviceBindingMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
