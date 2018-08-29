package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientHeartBeatBody;
import com.tct.codec.pojo.ClientHeartBeatMessage;
import com.tct.util.CoordinateConvertUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHeartBeatMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);

		//log.info(json.toJSONString());
		
		ClientHeartBeatMessage clientHeartBeatMessage = new ClientHeartBeatMessage();
		clientHeartBeatMessage.setMessageBody(json.getObject("messageBody",ClientHeartBeatBody.class));
/*		double la=Double.parseDouble(clientHeartBeatMessage.getMessageBody().getLa());
		double lo=Double.parseDouble(clientHeartBeatMessage.getMessageBody().getLo());
		double[] dtemp=CoordinateConvertUtil.wgs2BD09(la,lo);
		clientHeartBeatMessage.getMessageBody().setLa(Double.toString(dtemp[0]));
		clientHeartBeatMessage.getMessageBody().setLo(Double.toString(dtemp[1]));*/
		clientHeartBeatMessage.setDeviceType(json.getInteger("deviceType"));
		clientHeartBeatMessage.setServiceType(json.getString("serviceType"));
		clientHeartBeatMessage.setFormatVersion(json.getString("formatVersion"));
		clientHeartBeatMessage.setSerialNumber(json.getString("serialNumber"));
		clientHeartBeatMessage.setMessageType(json.getString("messageType"));
		clientHeartBeatMessage.setSendTime(json.getString("sendTime"));
		clientHeartBeatMessage.setSessionToken(json.getString("sessionToken"));
		
		return clientHeartBeatMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
