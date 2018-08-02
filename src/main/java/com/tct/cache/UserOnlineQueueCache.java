package com.tct.cache;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class UserOnlineQueueCache {
	private static final UserOnlineQueueCache instance= new UserOnlineQueueCache();
	
	private static ConcurrentHashMap<String, Hashtable<String, String>> onlineUserQueueHashMap=new ConcurrentHashMap<>();
	
	private UserOnlineQueueCache() {}
	
	public static UserOnlineQueueCache getInstance() {
		return instance;
	}
	
	public static synchronized ConcurrentHashMap<String, Hashtable<String, String>> getOnlineUserQueueMap() {
		return onlineUserQueueHashMap;
	}
}
