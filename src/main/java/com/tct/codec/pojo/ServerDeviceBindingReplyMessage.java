package com.tct.codec.pojo;

public class ServerDeviceBindingReplyMessage extends SimpleMessage {
	
	private ServerDeviceBindingBody messageBody;

	public ServerDeviceBindingBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerDeviceBindingBody messageBody) {
		this.messageBody = messageBody;
	}
	
	
}
