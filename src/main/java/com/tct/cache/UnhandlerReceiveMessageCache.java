package com.tct.cache;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class UnhandlerReceiveMessageCache {
	private static final UnhandlerReceiveMessageCache instance = new UnhandlerReceiveMessageCache();
	
	private static ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap=new ConcurrentHashMap<>();
				
	private UnhandlerReceiveMessageCache() {}
	
	public static UnhandlerReceiveMessageCache getInstance() {
		return instance;
	}
	
	public static synchronized ConcurrentHashMap<String, Hashtable<String, Object>> getUnSendReplyMessageMap(){
		return unhandlerReceiveMessageHashMap;
	}
}
