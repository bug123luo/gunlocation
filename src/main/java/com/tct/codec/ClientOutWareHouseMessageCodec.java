package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientOutWareHouseBody;
import com.tct.codec.pojo.ClientOutWareHouseMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientOutWareHouseMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		log.info(json.toJSONString());
		
		ClientOutWareHouseMessage clientOutWareHouseMessage = new ClientOutWareHouseMessage();
		clientOutWareHouseMessage.setMessageBody(json.getObject("messageBody",ClientOutWareHouseBody.class));
		clientOutWareHouseMessage.setServiceType(json.getString("serviceType"));
		clientOutWareHouseMessage.setFormatVersion(json.getString("formatVersion"));
		clientOutWareHouseMessage.setDeviceType(json.getInteger("deviceType"));
		clientOutWareHouseMessage.setSerialNumber(json.getString("serialNumber"));
		clientOutWareHouseMessage.setMessageType(json.getString("messageType"));
		clientOutWareHouseMessage.setSendTime(json.getString("sendTime"));
		clientOutWareHouseMessage.setSessionToken(json.getString("sessionToken"));
		
		return clientOutWareHouseMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
