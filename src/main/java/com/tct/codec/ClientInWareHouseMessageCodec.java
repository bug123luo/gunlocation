package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientInWareHouseBody;
import com.tct.codec.pojo.ClientInWareHouseMessage;

public class ClientInWareHouseMessageCodec implements MessageCodec{

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ClientInWareHouseMessage clientInWareHouseMessage= new ClientInWareHouseMessage();
		clientInWareHouseMessage.setMessageBody(json.getObject("messageBody",ClientInWareHouseBody.class));
		clientInWareHouseMessage.setServiceType(json.getString("serviceType"));
		clientInWareHouseMessage.setFormatVersion(json.getString("formatVersion"));;
		clientInWareHouseMessage.setDeviceType(json.getInteger("deviceType"));
		clientInWareHouseMessage.setSerialNumber(json.getString("serialNumber"));
		clientInWareHouseMessage.setMessageType(json.getString("messageType"));
		clientInWareHouseMessage.setSendTime(json.getString("sendTime"));
		
		return clientInWareHouseMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
