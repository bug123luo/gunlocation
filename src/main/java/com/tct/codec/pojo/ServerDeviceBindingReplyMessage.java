package com.tct.codec.pojo;

public class ServerDeviceBindingReplyMessage extends SimpleMessage {
	
	private String userName;//web用户名
	
	private ServerDeviceBindingBody messageBody;

	public ServerDeviceBindingBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerDeviceBindingBody messageBody) {
		this.messageBody = messageBody;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
