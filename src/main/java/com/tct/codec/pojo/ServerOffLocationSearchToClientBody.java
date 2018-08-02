package com.tct.codec.pojo;

import lombok.Data;

@Data
public class ServerOffLocationSearchToClientBody {
	private String reserve;
	private String id;
	private String bluetoothMac;
	private String lo;
	private String la;
	private String lostTime;
	private String authCode;
}
