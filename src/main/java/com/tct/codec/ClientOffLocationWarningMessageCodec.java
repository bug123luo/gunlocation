package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientOffLocationWarningBody;
import com.tct.codec.pojo.ClientOffLocationWarningMessage;
import com.tct.util.CoordinateConvertUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientOffLocationWarningMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		//log.info(json.toJSONString());
		
		ClientOffLocationWarningMessage clientOffLocationWarningMessage = new ClientOffLocationWarningMessage();
		clientOffLocationWarningMessage.setMessageBody(json.getObject("messageBody",ClientOffLocationWarningBody.class));
/*		double la=Double.parseDouble(clientOffLocationWarningMessage.getMessageBody().getLa());
		double lo=Double.parseDouble(clientOffLocationWarningMessage.getMessageBody().getLo());
		double[] dtemp=CoordinateConvertUtil.wgs2BD09(la,lo);
		clientOffLocationWarningMessage.getMessageBody().setLa(Double.toString(dtemp[0]));
		clientOffLocationWarningMessage.getMessageBody().setLo(Double.toString(dtemp[1]));*/
		clientOffLocationWarningMessage.setServiceType(json.getString("serviceType"));
		clientOffLocationWarningMessage.setFormatVersion(json.getString("formatVersion"));
		clientOffLocationWarningMessage.setDeviceType(json.getInteger("deviceType"));
		clientOffLocationWarningMessage.setSerialNumber(json.getString("serialNumber"));
		clientOffLocationWarningMessage.setMessageType(json.getString("messageType"));
		clientOffLocationWarningMessage.setSendTime(json.getString("sendTime"));
		clientOffLocationWarningMessage.setSessionToken(json.getString("sessionToken"));
		
		return clientOffLocationWarningMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
