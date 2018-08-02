package com.tct.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import com.tct.mapper.ServerMessageSerialnumberCustomMapper;
import com.tct.po.ServerMessageSerialnumberCustom;


public class ServerMessageSerialNumberGen {

	private static final ServerMessageSerialNumberGen instance =  new ServerMessageSerialNumberGen();
	
	protected ServerMessageSerialNumberGen() {}

	public static ServerMessageSerialNumberGen getInstance() {
		return instance;
	}
	
	public static synchronized String getSerialNumber() throws Exception {
		String serialNumber = new String();
		//从数据库查询获取serialNumber.如果数据库返回结果为空则执行一下代码
		ServerMessageSerialnumberCustom serverMessageSerialnumberCustom=null;
		SqlSession sqlSession = null;
		try {
			sqlSession = MyBatisUtil.getSqlSessionFactory().openSession();
			ServerMessageSerialnumberCustomMapper serverMessageSerialnumberCustomMapper = sqlSession.getMapper(ServerMessageSerialnumberCustomMapper.class);
			serverMessageSerialnumberCustom = serverMessageSerialnumberCustomMapper.selectMaxIdAndSerialNumber();		
		} finally {
			sqlSession.close();
		}
		
		serialNumber =  serverMessageSerialnumberCustom.getSerialnumber();
		
		if(serialNumber==null){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentTime = new Date();//得到当前系统时间
			String str_date1 = formatter.format(currentTime); //将日期时间格式化
			String str_date2 = str_date1.toString();   
		    serialNumber = str_date2.replaceAll("-", "").replaceAll(" ","").replaceAll(":", "")+"0000";
		}else {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentTime = new Date();//得到当前系统时间
			String str_date1 = formatter.format(currentTime); //将日期时间格式化
			String str_date2 = str_date1.toString();   
			str_date2 = str_date2.replaceAll("-", "").replaceAll(" ","").replaceAll(":", "");
			if(str_date2.equals(serialNumber.substring(0,14))) {
				Integer tempint=Integer.valueOf(serialNumber.substring(14));
				tempint++;
				serialNumber = serialNumber.substring(0, 14)+tempint;
			}
		    serialNumber = str_date2.replaceAll("-", "").replaceAll(" ","").replaceAll(":", "")+"0000";
		}
		return serialNumber;
	}
			
}
