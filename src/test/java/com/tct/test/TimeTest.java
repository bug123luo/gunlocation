package com.tct.test;

public class TimeTest {

	public static void main(String[] args) {
		String serialNumber = new String();
		//从数据库查询获取serialNumber
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		java.util.Date currentTime = new java.util.Date();//得到当前系统时间

		String str_date1 = formatter.format(currentTime); //将日期时间格式化
		String str_date2 = str_date1.toString();   
		
	    serialNumber = str_date2.replaceAll("-", "").replaceAll(" ","").replaceAll(":", "")+"0000";
	    
/*		Integer tempint=Integer.valueOf(serialNumber.substring(10));
		tempint++;
		serialNumber = serialNumber.substring(0, 10);*/
	    
		System.out.println(serialNumber.substring(14));
			    
		System.out.println(str_date1);
		
		System.out.println(serialNumber);
	}
}
