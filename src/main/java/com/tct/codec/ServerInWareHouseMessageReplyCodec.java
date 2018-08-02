package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ServerInWareHouseReplyBody;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ServerInWareHouseMessageReplyCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ServerInWareHouseReplyMessage serverInWareHouseReplyMessage = new ServerInWareHouseReplyMessage();
		serverInWareHouseReplyMessage.setServiceType(json.getString("serviceType"));
		serverInWareHouseReplyMessage.setFormatVersion(json.getString("formatVersion"));
		serverInWareHouseReplyMessage.setDeviceType(json.getInteger("deviceType"));
		serverInWareHouseReplyMessage.setSerialNumber(json.getString("serialNumber"));
		serverInWareHouseReplyMessage.setMessageType(json.getString("messageType"));
		serverInWareHouseReplyMessage.setSendTime(json.getString("sendTime"));
		serverInWareHouseReplyMessage.setMessageBody(json.getObject("messageBody",ServerInWareHouseReplyBody.class));

		log.info(json.toJSONString());

		return serverInWareHouseReplyMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
