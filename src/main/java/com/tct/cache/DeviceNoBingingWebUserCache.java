package com.tct.cache;

import java.util.concurrent.ConcurrentHashMap;

public class DeviceNoBingingWebUserCache {
	private static final DeviceNoBingingWebUserCache instance= new DeviceNoBingingWebUserCache();
	
	private static ConcurrentHashMap<String, String> deviceNoWebUserHashMap=new ConcurrentHashMap<String, String>();
	
	private DeviceNoBingingWebUserCache() {}
	
	public static DeviceNoBingingWebUserCache getInstance() {
		return instance;
	}
	
	public static synchronized ConcurrentHashMap<String, String> getDeviceNoWebUserHashMap() {
		return deviceNoWebUserHashMap;
	}
}
