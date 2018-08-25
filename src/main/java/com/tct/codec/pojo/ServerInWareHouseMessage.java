package com.tct.codec.pojo;

public class ServerInWareHouseMessage extends SimpleMessage {
	
	private String userName;//webUser web用户名
	private ServerInWareHouseBody messageBody;

	public ServerInWareHouseBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerInWareHouseBody messageBody) {
		this.messageBody = messageBody;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	
	
}
