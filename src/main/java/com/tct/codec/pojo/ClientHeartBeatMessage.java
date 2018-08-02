package com.tct.codec.pojo;

public class ClientHeartBeatMessage extends SimpleMessage {
	
	private ClientInWareHouseBody messageBody;

	public ClientInWareHouseBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ClientInWareHouseBody messageBody) {
		this.messageBody = messageBody;
	}


	
	
}
