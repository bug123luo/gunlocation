package com.tct.cache;

import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import com.tct.codec.pojo.SimpleMessage;

public class OnlineUserLastHBTimeCache {
	
	private static final OnlineUserLastHBTimeCache instance = new OnlineUserLastHBTimeCache();
	
	private static ConcurrentHashMap<String, Date> onlineUserLastHBTimeMap=new ConcurrentHashMap<>();
				
	private OnlineUserLastHBTimeCache() {}
	
	public static OnlineUserLastHBTimeCache getInstance() {
		return instance;
	}

	public static ConcurrentHashMap<String, Date> getOnlineUserLastHBTimeMap() {
		return onlineUserLastHBTimeMap;
	}

	public static void setOnlineUserLastHBTimeMap(ConcurrentHashMap<String, Date> onlineUserLastHBTimeMap) {
		OnlineUserLastHBTimeCache.onlineUserLastHBTimeMap = onlineUserLastHBTimeMap;
	}
	

}
