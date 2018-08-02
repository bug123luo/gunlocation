package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ServerInWareHouseBody;
import com.tct.codec.pojo.ServerInWareHouseMessage;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ServerInWareHouseMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ServerInWareHouseMessage serverInWareHouseMessage = new ServerInWareHouseMessage();
		serverInWareHouseMessage.setServiceType(json.getString("serviceType"));
		serverInWareHouseMessage.setFormatVersion(json.getString("formatVersion"));
		serverInWareHouseMessage.setDeviceType(json.getInteger("deviceType"));
		serverInWareHouseMessage.setSerialNumber(json.getString("serialNumber"));
		serverInWareHouseMessage.setMessageType(json.getString("messageType"));
		serverInWareHouseMessage.setSendTime(json.getString("sendTime"));
		serverInWareHouseMessage.setMessageBody(json.getObject("messageBody",ServerInWareHouseBody.class));

		log.info(json.toJSONString());
		return serverInWareHouseMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
