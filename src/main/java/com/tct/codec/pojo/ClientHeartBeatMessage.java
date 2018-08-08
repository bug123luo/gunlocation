package com.tct.codec.pojo;

public class ClientHeartBeatMessage extends SimpleMessage {
	
	private ClientHeartBeatBody messageBody;

	public ClientHeartBeatBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ClientHeartBeatBody messageBody) {
		this.messageBody = messageBody;
	}




	
	
}
