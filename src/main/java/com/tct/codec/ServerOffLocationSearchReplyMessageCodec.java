package com.tct.codec;

import com.alibaba.fastjson.JSONObject;

import com.tct.codec.pojo.ServerOffLocationSearchBody;
import com.tct.codec.pojo.ServerOffLocationSearchReplyMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerOffLocationSearchReplyMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		 
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		log.info(json.toJSONString());
		
		ServerOffLocationSearchReplyMessage serverOffLocationSearchReplyMessage =  new ServerOffLocationSearchReplyMessage();
		serverOffLocationSearchReplyMessage.setServiceType(json.getString("serviceType"));
		serverOffLocationSearchReplyMessage.setFormatVersion(json.getString("formatVersion"));
		serverOffLocationSearchReplyMessage.setDeviceType(json.getInteger("deviceType"));
		serverOffLocationSearchReplyMessage.setSerialNumber(json.getString("serialNumber"));
		serverOffLocationSearchReplyMessage.setMessageType(json.getString("messageType"));
		serverOffLocationSearchReplyMessage.setSendTime(json.getString("sendTime"));
		serverOffLocationSearchReplyMessage.setMessageBody(json.getObject("messageBody", ServerOffLocationSearchBody.class));
		serverOffLocationSearchReplyMessage.setSessionToken(json.getString("sessionToken"));
		
		//authCodeMessage.setMessageBody((AuthCodeMessageBody)json.get("messageBody"));
		
		return serverOffLocationSearchReplyMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
