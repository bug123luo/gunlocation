package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientDeviceBindingBody;
import com.tct.codec.pojo.ClientDeviceBindingMessage;
import com.tct.util.CoordinateConvertUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ClientDeviceBindingMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		//log.info(json.toJSONString());
		
		ClientDeviceBindingMessage clientDeviceBindingMessage = new ClientDeviceBindingMessage();
		clientDeviceBindingMessage.setDeviceType(json.getInteger("deviceType"));
		clientDeviceBindingMessage.setFormatVersion(json.getString("formatVersion"));
		clientDeviceBindingMessage.setMessageType(json.getString("messageType"));
		clientDeviceBindingMessage.setSendTime(json.getString("sendTime"));
		clientDeviceBindingMessage.setSerialNumber(json.getString("serialNumber"));
		clientDeviceBindingMessage.setServiceType(json.getString("serviceType"));
		clientDeviceBindingMessage.setMessageBody(json.getObject("messageBody",ClientDeviceBindingBody.class));
/*		double la=Double.parseDouble(clientDeviceBindingMessage.getMessageBody().getLa());
		double lo=Double.parseDouble(clientDeviceBindingMessage.getMessageBody().getLo());
		double[] dtemp=CoordinateConvertUtil.wgs2BD09(la,lo);
		clientDeviceBindingMessage.getMessageBody().setLa(Double.toString(dtemp[0]));
		clientDeviceBindingMessage.getMessageBody().setLo(Double.toString(dtemp[1]));*/
		clientDeviceBindingMessage.setSessionToken(json.getString("sessionToken"));
		
		return clientDeviceBindingMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
