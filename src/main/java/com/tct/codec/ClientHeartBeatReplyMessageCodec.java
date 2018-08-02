package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientHeartBeatReplyBody;
import com.tct.codec.pojo.ClientHeartBeatReplyMessage;

public class ClientHeartBeatReplyMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ClientHeartBeatReplyMessage clientHeartBeatReplyMessage =  new ClientHeartBeatReplyMessage();
		clientHeartBeatReplyMessage.setMessageBody(json.getObject("message", ClientHeartBeatReplyBody.class));
		clientHeartBeatReplyMessage.setDeviceType(json.getInteger("deviceType"));
		clientHeartBeatReplyMessage.setServiceType(json.getString("serviceType"));
		clientHeartBeatReplyMessage.setFormatVersion(json.getString("formatVersion"));
		clientHeartBeatReplyMessage.setSerialNumber(json.getString("serialNumber"));
		clientHeartBeatReplyMessage.setMessageType(json.getString("messageType"));
		clientHeartBeatReplyMessage.setSendTime(json.getString("sendTime"));
		return clientHeartBeatReplyMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
