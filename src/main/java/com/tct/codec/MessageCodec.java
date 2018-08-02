package com.tct.codec;

public interface MessageCodec {
	public Object decode(String inMsg) throws Exception;
	public String encode(Object outMsg) throws Exception;
}
