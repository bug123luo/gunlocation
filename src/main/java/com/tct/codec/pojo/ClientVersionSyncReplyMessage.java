package com.tct.codec.pojo;

public class ClientVersionSyncReplyMessage extends SimpleMessage {
	private ClientVersionSyncReplyBody messageBody;

	public ClientVersionSyncReplyBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ClientVersionSyncReplyBody messageBody) {
		this.messageBody = messageBody;
	}
	
	
}
