package com.tct.util;

import java.util.Random;

public class RandomNumber {
	private static final RandomNumber instance = new RandomNumber();
	
	private RandomNumber() {}
	
	public static RandomNumber getInstance() {
		return instance;
	}
	
	public static synchronized String getRandomNumber() {
		Random rand = new Random();
		StringBuffer sb=new StringBuffer();
		for (int i=1;i<=32;i++){
		    int randNum = rand.nextInt(9)+1;
		    String num=randNum+"";
		    sb=sb.append(num);
		}
		String random=String.valueOf(sb);
		return random;
	}
}
