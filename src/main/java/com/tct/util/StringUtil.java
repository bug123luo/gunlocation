package com.tct.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {
	private static final StringUtil instance = new StringUtil();
	
	private StringUtil() {};
	
	public static StringUtil getInstance() {
		return instance;
	}
	
	public static synchronized Date getDate(String msg) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=null;
		try {
			String tempdate=msg;
			String pString = tempdate.substring(0, 4)+"-"+tempdate.substring(4,6)+"-"+tempdate.substring(6, 8)+" "+
							tempdate.substring(8, 10)+":"+tempdate.substring(10,12)+":"+tempdate.substring(12, 14);
			date = simpleDateFormat.parse(pString);
		} catch (ParseException e1) {
			log.debug("日期格式转换错误");
		}
		return date;
	}
	
	public static synchronized String getDateString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		String tempdate=simpleDateFormat.format(new Date());
		String pString = tempdate.substring(0, 4)+tempdate.substring(4,6)+tempdate.substring(6, 8)+
						tempdate.substring(8, 10)+tempdate.substring(10,12)+tempdate.substring(12, 14);
		
		return pString;
	}
}
