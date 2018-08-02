package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.DeviceBulletCountBody;
import com.tct.codec.pojo.DeviceBulletCountMessage;

public class DeviceBulletCountMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		DeviceBulletCountMessage deviceBulletCountMessage =  new DeviceBulletCountMessage();
		deviceBulletCountMessage.setMessageBody(json.getObject("messageBody",DeviceBulletCountBody.class));
		deviceBulletCountMessage.setServiceType(json.getString("serviceType"));
		deviceBulletCountMessage.setFormatVersion(json.getString("formatVersion"));
		deviceBulletCountMessage.setDeviceType(json.getInteger("deviceType"));
		deviceBulletCountMessage.setSerialNumber(json.getString("serialNumber"));
		deviceBulletCountMessage.setMessageType(json.getString("messageType"));
		deviceBulletCountMessage.setSendTime(json.getString("sendTime"));
		return deviceBulletCountMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
