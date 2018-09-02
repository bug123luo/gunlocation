package com.tct.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.tct.codec.pojo.SimpleMessage;

public class SessionMessageCache {
	private static final SessionMessageCache instance = new SessionMessageCache();
	
	private static ConcurrentHashMap<String, SimpleMessage> sessionMessageMap=new ConcurrentHashMap<>();
				
	private SessionMessageCache() {}
	
	public static SessionMessageCache getInstance() {
		return instance;
	}
	
	public static synchronized ConcurrentHashMap<String, SimpleMessage> getSessionMessageMessageMap(){
		return sessionMessageMap;
	}
}
