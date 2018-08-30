package com.tct.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.val;


@Component("bluetoothPara")
@Scope("singleton")
@Data
public class BluetoothPara {
	@Value("${powerAlarmLevel}")
	private String powerAlarmLevel;//电量告警级别
	@Value("${transmittingPower}")
	private String transmittingPower;//发射功率
	@Value("${broadcastInterval}")
	private String broadcastInterval;//广播间隔
	@Value("${conncetionInterval}")
	private String conncetionInterval;//连接间隔
	@Value("${connectionTimeout}")
	private String connectionTimeout;//连接超时
	@Value("${softwareversion}")
	private String softwareversion;//软硬件版本
	@Value("${heartbeat}")
	private String heartbeat;//周期上报数据间隔
	@Value("${powerSampling}")
	private String powerSampling;//电量采样间隔
	@Value("${matchTime}")	
	private String matchTime;//随行设备匹配最大时间(绑定超时)
	@Value("${safeCode}")
	private String safeCode;//安全字
}
