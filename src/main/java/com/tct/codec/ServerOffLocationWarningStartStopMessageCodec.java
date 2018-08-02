package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopBody;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopMessage;

public class ServerOffLocationWarningStartStopMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ServerOffLocationWarningStartStopMessage serverOffLocationWarningStartStopMessage = new ServerOffLocationWarningStartStopMessage();
		
		serverOffLocationWarningStartStopMessage.setMessageBody(json.getObject("messageBody",ServerOffLocationWarningStartStopBody.class));
		serverOffLocationWarningStartStopMessage.setServiceType(json.getString("serviceType"));
		serverOffLocationWarningStartStopMessage.setFormatVersion(json.getString("formatVersion"));
		serverOffLocationWarningStartStopMessage.setDeviceType(json.getInteger("deviceType"));
		serverOffLocationWarningStartStopMessage.setSerialNumber(json.getString("serialNumber"));
		serverOffLocationWarningStartStopMessage.setMessageType(json.getString("messageType"));
		serverOffLocationWarningStartStopMessage.setSendTime(json.getString("sendTime"));
		
		return serverOffLocationWarningStartStopMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
