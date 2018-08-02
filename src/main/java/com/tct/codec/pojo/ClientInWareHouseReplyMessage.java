package com.tct.codec.pojo;

public class ClientInWareHouseReplyMessage extends SimpleMessage{
	
	private ClientInWareHouseReplyBody messageBody;

	public ClientInWareHouseReplyBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ClientInWareHouseReplyBody messageBody) {
		this.messageBody = messageBody;
	}


	
}
