package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopBody;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopReplyBody;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopReplyMessage;

public class ServerOffLocationWarningStartStopReplyMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ServerOffLocationWarningStartStopReplyMessage serverOffLocationWarningStartStopReplyMessage =  new ServerOffLocationWarningStartStopReplyMessage();
		serverOffLocationWarningStartStopReplyMessage.setServiceType(json.getString("serviceType"));
		serverOffLocationWarningStartStopReplyMessage.setFormatVersion(json.getString("formatVersion"));
		serverOffLocationWarningStartStopReplyMessage.setDeviceType(json.getInteger("deviceType"));
		serverOffLocationWarningStartStopReplyMessage.setSerialNumber(json.getString("serialNumber"));
		serverOffLocationWarningStartStopReplyMessage.setMessageType(json.getString("messageType"));
		serverOffLocationWarningStartStopReplyMessage.setSendTime(json.getString("sendTime"));
		serverOffLocationWarningStartStopReplyMessage.setMessageBody(json.getObject("messageBody", ServerOffLocationWarningStartStopReplyBody.class));
		
		return serverOffLocationWarningStartStopReplyMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
