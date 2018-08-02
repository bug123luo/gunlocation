package com.tct.cache;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class UnSendReplyMessageCache {

	private static final UnSendReplyMessageCache instance = new UnSendReplyMessageCache();
			
	private static ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageHashMap=new ConcurrentHashMap<>();
				
	private UnSendReplyMessageCache() {}
	
	public static UnSendReplyMessageCache getInstance() {
		return instance;
	}
	
	public static synchronized ConcurrentHashMap<String, Hashtable<String, Object>> getUnSendReplyMessageMap(){
		return unSendReplyMessageHashMap;
	}
	
}
