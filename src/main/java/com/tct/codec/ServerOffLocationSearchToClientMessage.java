package com.tct.codec;

import com.tct.codec.pojo.ServerOffLocationSearchToClientBody;
import com.tct.codec.pojo.SimpleMessage;

public class ServerOffLocationSearchToClientMessage extends SimpleMessage {
	private ServerOffLocationSearchToClientBody messageBody;

	public ServerOffLocationSearchToClientBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerOffLocationSearchToClientBody messageBody) {
		this.messageBody = messageBody;
	}
	
	
}
