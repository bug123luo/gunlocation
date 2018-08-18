package com.tct.codec.pojo;

import lombok.Data;

@Data
public class ServerOffLocationWarningReplyBody {
	private String gunTag;
	private String deviceNo;
	private String lo;
	private String la;
	private String areaCode;
	private String authCode;
	private String state;
	
}
