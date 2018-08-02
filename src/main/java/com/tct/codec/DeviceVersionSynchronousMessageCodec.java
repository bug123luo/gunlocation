package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.DeviceVersionSynchronousBody;
import com.tct.codec.pojo.DeviceVersionSynchronousMessage;

public class DeviceVersionSynchronousMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		DeviceVersionSynchronousMessage deviceVersionSynchronousMessage =  new DeviceVersionSynchronousMessage();
		
		deviceVersionSynchronousMessage.setMessageBody(json.getObject("messageBody",DeviceVersionSynchronousBody.class));
		deviceVersionSynchronousMessage.setServiceType(json.getString("serviceType"));
		deviceVersionSynchronousMessage.setFormatVersion(json.getString("formatVersion"));
		deviceVersionSynchronousMessage.setDeviceType(json.getInteger("deviceType"));
		deviceVersionSynchronousMessage.setSerialNumber(json.getString("serialNumber"));
		deviceVersionSynchronousMessage.setMessageType(json.getString("messageType"));
		deviceVersionSynchronousMessage.setSendTime(json.getString("sendTime"));
		
		return deviceVersionSynchronousMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
