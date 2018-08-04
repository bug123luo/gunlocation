package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ServerOutWareHouseReplyBody;
import com.tct.codec.pojo.ServerOutWareHouseReplyMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerOutWareHouseReplyMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {

		JSONObject json= JSONObject.parseObject(inMsg);

		log.info(json.toJSONString());
		
		ServerOutWareHouseReplyMessage serverOutWareHouseReplyMessage = new ServerOutWareHouseReplyMessage();
		serverOutWareHouseReplyMessage.setServiceType(json.getString("serviceType"));
		serverOutWareHouseReplyMessage.setFormatVersion(json.getString("formatVersion"));
		serverOutWareHouseReplyMessage.setDeviceType(json.getInteger("deviceType"));
		serverOutWareHouseReplyMessage.setSerialNumber(json.getString("serialNumber"));
		serverOutWareHouseReplyMessage.setMessageType(json.getString("messageType"));
		serverOutWareHouseReplyMessage.setSendTime(json.getString("sendTime"));
		serverOutWareHouseReplyMessage.setMessageBody(json.getObject("messageBody", ServerOutWareHouseReplyBody.class));
		serverOutWareHouseReplyMessage.setSessionToken(json.getString("sessionToken"));

		return serverOutWareHouseReplyMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		return null;
	}

}
