package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientHeartBeatMessage;
import com.tct.codec.pojo.ClientInWareHouseBody;

public class ClientHeartBeatMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);

		ClientHeartBeatMessage clientHeartBeatMessage = new ClientHeartBeatMessage();
		clientHeartBeatMessage.setMessageBody(json.getObject("messageBody",ClientInWareHouseBody.class));
		clientHeartBeatMessage.setDeviceType(json.getInteger("deviceType"));
		clientHeartBeatMessage.setServiceType(json.getString("serviceType"));
		clientHeartBeatMessage.setFormatVersion(json.getString("formatVersion"));
		clientHeartBeatMessage.setSerialNumber(json.getString("serialNumber"));
		clientHeartBeatMessage.setMessageType(json.getString("messageType"));
		clientHeartBeatMessage.setSendTime(json.getString("sendTime"));
		return clientHeartBeatMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
