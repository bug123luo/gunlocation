package com.tct.codec.pojo;

public class ServerOutWareHouseBody{
	//预留
	private String reserve;
	//枪支id
	private String bluetoothMac;
	//枪号
	private String gunTag;
	//领用时间
	private String applyTime;
	//归还截止时间
	private String deadlineTime;
	//电量报警级别
	private String powerAlarmLevel;
	//发射功率
	private String transmittingPower;
	//广播间隔
	private String broadcastInterval;
	//连接间隔
	private String connectionInterval;
	//连接超时
	private String connectionTimeout;
	//软硬件版本
	private String softwareversion;
	//心跳间隔
	private String heartbeat;
	//
	private String powerSampling;
	//系统时间
	private String systemTime;
	//匹配最大时间:随行设备匹配最大时间（绑定超时）
	private String matchTime;
	//安全字
	private String safeCode;
	//警员编号
	private String deviceNo;
	
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	public String getBluetoothMac() {
		return bluetoothMac;
	}
	public void setBluetoothMac(String bluetoothMac) {
		this.bluetoothMac = bluetoothMac;
	}
	public String getGunTag() {
		return gunTag;
	}
	public void setGunTag(String gunTag) {
		this.gunTag = gunTag;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getDeadlineTime() {
		return deadlineTime;
	}
	public void setDeadlineTime(String deadlineTime) {
		this.deadlineTime = deadlineTime;
	}
	public String getPowerAlarmLevel() {
		return powerAlarmLevel;
	}
	public void setPowerAlarmLevel(String powerAlarmLevel) {
		this.powerAlarmLevel = powerAlarmLevel;
	}
	public String getTransmittingPower() {
		return transmittingPower;
	}
	public void setTransmittingPower(String transmittingPower) {
		this.transmittingPower = transmittingPower;
	}
	public String getBroadcastInterval() {
		return broadcastInterval;
	}
	public void setBroadcastInterval(String broadcastInterval) {
		this.broadcastInterval = broadcastInterval;
	}

	public String getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(String connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getHeartbeat() {
		return heartbeat;
	}
	public void setHeartbeat(String heartbeat) {
		this.heartbeat = heartbeat;
	}
	public String getPowerSampling() {
		return powerSampling;
	}
	public void setPowerSampling(String powerSampling) {
		this.powerSampling = powerSampling;
	}
	public String getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getSafeCode() {
		return safeCode;
	}
	public void setSafeCode(String safeCode) {
		this.safeCode = safeCode;
	}
	public String getConnectionInterval() {
		return connectionInterval;
	}
	public void setConnectionInterval(String connectionInterval) {
		this.connectionInterval = connectionInterval;
	}
	public String getSoftwareversion() {
		return softwareversion;
	}
	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	
	
}
