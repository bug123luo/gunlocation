package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ServerOutWareHouseMessage;
import com.tct.codec.pojo.ServerOutWareHouseReplyBody;
import com.tct.codec.pojo.ServerOutWareHouseReplyMessage;

import lombok.extern.slf4j.Slf4j;

import com.tct.codec.pojo.ServerOutWareHouseBody;
@Slf4j
public class ServerOutWareHouseMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		ServerOutWareHouseMessage serverOutWareHouseMessage = null;
		
		serverOutWareHouseMessage = new ServerOutWareHouseMessage();
		serverOutWareHouseMessage.setServiceType(json.getString("serviceType"));
		serverOutWareHouseMessage.setFormatVersion(json.getString("formatVersion"));
		serverOutWareHouseMessage.setDeviceType(json.getInteger("deviceType"));
		serverOutWareHouseMessage.setSerialNumber(json.getString("serialNumber"));
		serverOutWareHouseMessage.setMessageType(json.getString("messageType"));
		serverOutWareHouseMessage.setSendTime(json.getString("sendTime"));
		serverOutWareHouseMessage.setMessageBody(json.getObject("messageBody",ServerOutWareHouseBody.class));

		log.info(json.toJSONString());
		return serverOutWareHouseMessage;
	
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
