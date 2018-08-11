package com.tct.codec.pojo;

public class ClientInWareHouseBody {

	private String reserve;
	private String bluetoothMac;
	private String warehousingTime;
	private String lo;
	private String la;
	private String failReason;
	private String authCode;
	
	public String getBluetoothMac() {
		return bluetoothMac;
	}
	public void setBluetoothMac(String bluetoothMac) {
		this.bluetoothMac = bluetoothMac;
	}
	public String getWarehousingTime() {
		return warehousingTime;
	}
	public void setWarehousingTime(String warehousingTime) {
		this.warehousingTime = warehousingTime;
	}
	public String getLo() {
		return lo;
	}
	public void setLo(String lo) {
		this.lo = lo;
	}
	public String getLa() {
		return la;
	}
	public void setLa(String la) {
		this.la = la;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	
	
}
