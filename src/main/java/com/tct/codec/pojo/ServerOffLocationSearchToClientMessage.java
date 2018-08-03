package com.tct.codec.pojo;

public class ServerOffLocationSearchToClientMessage extends SimpleMessage {
	private ServerOffLocationSearchToClientBody messageBody;

	public ServerOffLocationSearchToClientBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerOffLocationSearchToClientBody messageBody) {
		this.messageBody = messageBody;
	}
	
	
}
