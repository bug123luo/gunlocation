package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.MessageCodec;

public class MessageCodecSelector {	
	public MessageCodec getMessageDecode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		MessageCodec msgCodec = null;
		
		if (json.getString("messageType").equals("01")) {
			msgCodec = new AuthCodeMessageCodec();
		}else if(json.getString("messageType").equals("03")) {
			msgCodec = new ServerOutWareHouseMessageCodec();
		}else if(json.getString("messageType").equals("04")) {
			msgCodec = new ServerOutWareHouseReplyMessageCodec();
		}else if(json.getString("messageType").equals("05")) {
			msgCodec = new ClientOutWareHouseMessageCodec();
		}else if(json.getString("messageType").equals("07")) {
			msgCodec = new ClientDeviceBindingMessageCodec();
		}else if(json.getString("messageType").equals("09")){
		    msgCodec = new ServerInWareHouseMessageCodec();	
		}else if(json.getString("messageType").equals("10")) {
			msgCodec =  new ServerInWareHouseMessageReplyCodec();
		}else if(json.getString("messageType").equals("11")) {
			msgCodec = new ClientInWareHouseMessageCodec();
		}else if(json.getString("messageType").equals("13")) {
			msgCodec = new ClientHeartBeatMessageCodec();
		}else if(json.getString("messageType").equals("15")){
			msgCodec = new ClientOffLocationWarningMessageCodec();
		}else if(json.getString("messageType").equals("17")) {
			msgCodec = new ServerOffLocationWarningStartStopMessageCodec();
		}else if(json.getString("messageType").equals("19")) {
			msgCodec = new ServerOffLocationSearchMessageCodec();
		}else if(json.getString("messageType").equals("21")) {
			msgCodec = new DeviceVersionSynchronousMessageCodec();
		}else if(json.getString("messageType").equals("23")) {
			msgCodec = new DeviceBulletCountMessageCodec();
		}else {
			
		}
				
		return msgCodec;
	}

/*	public MessageCodec encode(String outMsg) throws Exception {
		
		return null;
	}*/

}
