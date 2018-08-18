package com.tct.cache;

import java.util.concurrent.ConcurrentHashMap;

public class UserOnlineSessionCache {

	private static final UserOnlineSessionCache instance = new UserOnlineSessionCache();
	
	private static ConcurrentHashMap<String, String> userSessionMap=new ConcurrentHashMap<>();
	
	private UserOnlineSessionCache() {}
	
	public static UserOnlineSessionCache getInstance() {
		return instance;
	}

	public static synchronized ConcurrentHashMap<String, String> getuserSessionMap() {
		return userSessionMap;
	}
}
