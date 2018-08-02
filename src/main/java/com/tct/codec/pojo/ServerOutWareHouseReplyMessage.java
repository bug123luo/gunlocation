package com.tct.codec.pojo;

public class ServerOutWareHouseReplyMessage extends SimpleMessage{
	
	private ServerOutWareHouseReplyBody messageBody;

	public ServerOutWareHouseReplyBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerOutWareHouseReplyBody messageBody) {
		this.messageBody = messageBody;
	}
}
