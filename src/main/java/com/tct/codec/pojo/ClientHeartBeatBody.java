package com.tct.codec.pojo;

import lombok.Data;

@Data
public class ClientHeartBeatBody{
	
	private String realTimeState;//随行设备与蓝牙绑定状态 0未绑定枪支id,1绑定枪支ID，2绑定枪支ID已离位告警
	private String state;//出入库状态 0 出库 1入库
	private String bluetoothMac;//枪支id
	private String lo;//经度
	private String la;//纬度 
	private String areaCode;//小区代码
	private String batteryPower;//随行设备电量
	private String deviceBatteryPower;//离位置告警设备电量
	private String exception;//异常事件
	private String authCode;//授权码
		
}
