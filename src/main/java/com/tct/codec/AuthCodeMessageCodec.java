package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.AuthCodeMessageBody;
import com.tct.util.CoordinateConvertUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthCodeMessageCodec implements MessageCodec{

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		//log.info(json.toJSONString());
		
		AuthCodeMessage authCodeMessage =  new AuthCodeMessage();
		authCodeMessage.setServiceType(json.getString("serviceType"));
		authCodeMessage.setFormatVersion(json.getString("formatVersion"));
		authCodeMessage.setDeviceType(json.getInteger("deviceType"));
		authCodeMessage.setSerialNumber(json.getString("serialNumber"));
		authCodeMessage.setMessageType(json.getString("messageType"));
		authCodeMessage.setSendTime(json.getString("sendTime"));
		authCodeMessage.setMessageBody(json.getObject("messageBody", AuthCodeMessageBody.class));
/*		double la=Double.parseDouble(authCodeMessage.getMessageBody().getLa());
		double lo=Double.parseDouble(authCodeMessage.getMessageBody().getLo());
		double[] dtemp=CoordinateConvertUtil.wgs2BD09(la,lo);
		authCodeMessage.getMessageBody().setLa(Double.toString(dtemp[0]));
		authCodeMessage.getMessageBody().setLo(Double.toString(dtemp[1]));*/
		authCodeMessage.setSessionToken(json.getString("sessionToken"));
		
		//authCodeMessage.setMessageBody((AuthCodeMessageBody)json.get("messageBody"));
		
		return authCodeMessage;
	}

	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
