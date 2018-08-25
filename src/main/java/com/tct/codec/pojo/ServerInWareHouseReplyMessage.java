package com.tct.codec.pojo;

public class ServerInWareHouseReplyMessage extends SimpleMessage {
	
	
	private String userName;//web用户名
	private ServerInWareHouseReplyBody messageBody;

	public ServerInWareHouseReplyBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerInWareHouseReplyBody messageBody) {
		this.messageBody = messageBody;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}



}
