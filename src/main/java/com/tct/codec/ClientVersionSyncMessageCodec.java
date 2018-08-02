package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.ClientVersionSyncBody;
import com.tct.codec.pojo.ClientVersionSyncMessage;


public class ClientVersionSyncMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		ClientVersionSyncMessage clientVersionSyncMessage =  new ClientVersionSyncMessage();
		clientVersionSyncMessage.setServiceType(json.getString("serviceType"));
		clientVersionSyncMessage.setFormatVersion(json.getString("formatVersion"));
		clientVersionSyncMessage.setDeviceType(json.getInteger("deviceType"));
		clientVersionSyncMessage.setSerialNumber(json.getString("serialNumber"));
		clientVersionSyncMessage.setMessageType(json.getString("messageType"));
		clientVersionSyncMessage.setSendTime(json.getString("sendTime"));
		clientVersionSyncMessage.setMessageBody(json.getObject("messageBody", ClientVersionSyncBody.class));
		
		//authCodeMessage.setMessageBody((AuthCodeMessageBody)json.get("messageBody"));
		
		return clientVersionSyncMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
