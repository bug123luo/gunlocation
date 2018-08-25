package com.tct.codec.pojo;

public class ServerOutWareHouseMessage extends SimpleMessage{
	private String userName;//webUserName web用户名
	private ServerOutWareHouseBody messageBody;

	public ServerOutWareHouseBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ServerOutWareHouseBody messageBody) {
		this.messageBody = messageBody;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	
	
}
