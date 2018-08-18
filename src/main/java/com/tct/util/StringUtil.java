package com.tct.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		String pString = tempdate.replaceAll("-", "");
		String bString = pString.replaceAll(":", "");
		String tString = bString.replaceAll(" ", "");
		
		return tString;
	}
	
	public static synchronized Object getKey(Map map, Object value){
	    Set set = map.entrySet(); //通过entrySet()方法把map中的每个键值对变成对应成Set集合中的一个对象
	    Iterator<Map.Entry<Object, Object>> iterator = set.iterator();
	    ArrayList<Object> arrayList = new ArrayList();
	    while(iterator.hasNext()){
	        //Map.Entry是一种类型，指向map中的一个键值对组成的对象
	        Map.Entry<Object, Object> entry = iterator.next();
	        if(entry.getValue().equals(value)){
	            arrayList.add(entry.getKey());
	        }
	    }
	    return arrayList;
	}
}
