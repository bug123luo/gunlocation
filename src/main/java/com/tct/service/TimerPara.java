package com.tct.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component("timerPara")
@Scope("singleton")
@Data
public class TimerPara {
	@Value("${timer.seconds}")
	private String clearTime;
}
