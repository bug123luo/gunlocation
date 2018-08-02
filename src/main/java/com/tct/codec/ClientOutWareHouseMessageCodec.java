package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientOutWareHouseBody;
import com.tct.codec.pojo.ClientOutWareHouseMessage;

public class ClientOutWareHouseMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ClientOutWareHouseMessage clientOutWareHouseMessage = new ClientOutWareHouseMessage();
		clientOutWareHouseMessage.setMessageBody(json.getObject("messageBody",ClientOutWareHouseBody.class));
		clientOutWareHouseMessage.setServiceType(json.getString("serviceType"));
		clientOutWareHouseMessage.setFormatVersion(json.getString("formatVersion"));
		clientOutWareHouseMessage.setDeviceType(json.getInteger("deviceType"));
		clientOutWareHouseMessage.setSerialNumber(json.getString("serialNumber"));
		clientOutWareHouseMessage.setMessageType(json.getString("messageType"));
		clientOutWareHouseMessage.setSendTime(json.getString("sendTime"));
		
		return clientOutWareHouseMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
