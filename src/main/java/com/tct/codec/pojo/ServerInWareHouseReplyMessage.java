package com.tct.codec.pojo;

public class ServerInWareHouseReplyMessage extends SimpleMessage {
	
	private ServerInWareHouseReplyBody messageBody;

	public ServerInWareHouseReplyBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerInWareHouseReplyBody messageBody) {
		this.messageBody = messageBody;
	}



}
