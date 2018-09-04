package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.DeviceBulletCountBody;
import com.tct.codec.pojo.DeviceBulletCountMessage;
import com.tct.util.CoordinateConvertUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceBulletCountMessageCodec implements MessageCodec {

	@Override
	public Object decode(String inMsg) throws Exception {
		
		JSONObject json= JSONObject.parseObject(inMsg);
		
		//log.info(json.toJSONString());
		
		DeviceBulletCountMessage deviceBulletCountMessage =  new DeviceBulletCountMessage();
		deviceBulletCountMessage.setMessageBody(json.getObject("messageBody",DeviceBulletCountBody.class));
/*		double la=Double.parseDouble(deviceBulletCountMessage.getMessageBody().getLa());
		double lo=Double.parseDouble(deviceBulletCountMessage.getMessageBody().getLo());
		double[] dtemp=CoordinateConvertUtil.gcj2BD09(la,lo);
		deviceBulletCountMessage.getMessageBody().setLa(Double.toString(dtemp[0]));
		deviceBulletCountMessage.getMessageBody().setLo(Double.toString(dtemp[1]));*/
		deviceBulletCountMessage.setServiceType(json.getString("serviceType"));
		deviceBulletCountMessage.setFormatVersion(json.getString("formatVersion"));
		deviceBulletCountMessage.setDeviceType(json.getInteger("deviceType"));
		deviceBulletCountMessage.setSerialNumber(json.getString("serialNumber"));
		deviceBulletCountMessage.setMessageType(json.getString("messageType"));
		deviceBulletCountMessage.setSendTime(json.getString("sendTime"));
		deviceBulletCountMessage.setSessionToken(json.getString("sessionToken"));
		
		return deviceBulletCountMessage;
	}

	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
