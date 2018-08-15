package com.tct.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.AuthCodeMessageBody;
import com.tct.codec.pojo.ClientInWareHouseBody;
import com.tct.codec.pojo.ClientInWareHouseMessage;
import com.tct.codec.pojo.ServerInWareHouseBody;
import com.tct.codec.pojo.ServerInWareHouseMessage;
import com.tct.codec.pojo.ServerOutWareHouseBody;
import com.tct.codec.pojo.ServerOutWareHouseMessage;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueueSender {

	public static void main(String[] args) throws JMSException, InterruptedException {
		//ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory("tcp://112.74.51.194:61616");
		ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory("tcp://120.76.156.120:6160");
		//ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory("tcp://14.116.149.237:6160");
		Connection connection =  connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("InputQueue");
		
		MessageProducer producer =  session.createProducer(destination);
		for(int i=0;i<1;i++) {
			
			AuthCodeMessage authCodeMessageBody = new AuthCodeMessage();
			
			AuthCodeMessageBody authSunBody = new AuthCodeMessageBody();
			
			authSunBody.setCommand("admin123");
			authSunBody.setLa("234234");
			authSunBody.setLo("234234");
			authSunBody.setUsername("yunbao20");
			
			authCodeMessageBody.setDeviceType(1);
			authCodeMessageBody.setFormatVersion("1.0");
			authCodeMessageBody.setMessageBody(authSunBody);
			authCodeMessageBody.setMessageType("01");
			authCodeMessageBody.setSendTime("20180725121212");
			authCodeMessageBody.setSerialNumber(StringUtil.getDateString());
			authCodeMessageBody.setServiceType("aafafasfsaffsfsfsfs");
			authCodeMessageBody.setSessionToken("00000165053debcd");
			TextMessage message =  session.createTextMessage(JSONObject.toJSONString(authCodeMessageBody));

			System.out.println(JSONObject.toJSONString(authCodeMessageBody));
			
/*			ServerInWareHouseMessage serverInWareHouseMessage = new ServerInWareHouseMessage();
			ServerInWareHouseBody serverInWareHouseBody = new ServerInWareHouseBody();
			serverInWareHouseBody.setAuthCode("61368567623976934421652194399384");
			serverInWareHouseBody.setBluetoothMac("poiuyt");
			serverInWareHouseBody.setDeviceNo("qwerty");
			serverInWareHouseMessage.setMessageBody(serverInWareHouseBody);
			serverInWareHouseMessage.setDeviceType(1);
			serverInWareHouseMessage.setFormatVersion("1.0");
			serverInWareHouseMessage.setMessageType("09");
			serverInWareHouseMessage.setSendTime("20180725121212");
			serverInWareHouseMessage.setSerialNumber("1234567894564621");
			serverInWareHouseMessage.setServiceType("aafafasfsaffsfsfsfs");
			
			log.info(JSONObject.toJSONString(serverInWareHouseMessage));			
			TextMessage message =  session.createTextMessage(JSONObject.toJSONString(serverInWareHouseMessage));*/
/*			ClientInWareHouseMessage clientInWareHouseMessage = new ClientInWareHouseMessage();
			ClientInWareHouseBody clientInWareHouseBody = new ClientInWareHouseBody();
			
			clientInWareHouseBody.setAuthCode("sdfsfluouoojll");
			clientInWareHouseBody.setBluetoothMac("25:23:3t:6g:55:8c");
			clientInWareHouseBody.setWarehousingTime(StringUtil.getDateString());
			clientInWareHouseBody.setLa("23");
			clientInWareHouseBody.setLo("123");
			clientInWareHouseMessage.setMessageBody(clientInWareHouseBody);
			clientInWareHouseMessage.setDeviceType(1);
			clientInWareHouseMessage.setFormatVersion("1.0");
			clientInWareHouseMessage.setMessageType("09");
			clientInWareHouseMessage.setSendTime("20180725121212");
			clientInWareHouseMessage.setSerialNumber("1234567894564621");
			clientInWareHouseMessage.setServiceType("aafafasfsaffsfsfsfs");
			TextMessage message =  session.createTextMessage(JSONObject.toJSONString(clientInWareHouseMessage));*/
			
			Thread.sleep(1000);
			
			producer.send(message);
		}
		
		session.commit();
		session.close();
		connection.close();
	}
}
