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
import com.tct.codec.pojo.ServerInWareHouseBody;
import com.tct.codec.pojo.ServerInWareHouseMessage;
import com.tct.codec.pojo.ServerOutWareHouseBody;
import com.tct.codec.pojo.ServerOutWareHouseMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueueSender {

	public static void main(String[] args) throws JMSException, InterruptedException {
		ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory("tcp://112.74.51.194:61616");
		Connection connection =  connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("WebQueue");
		
		MessageProducer producer =  session.createProducer(destination);
		for(int i=0;i<1;i++) {
			
/*			AuthCodeMessage authCodeMessageBody = new AuthCodeMessage();
			
			AuthCodeMessageBody authSunBody = new AuthCodeMessageBody();
			
			authSunBody.setCommand("234223");
			authSunBody.setLa("234234");
			authSunBody.setLo("234234");
			authSunBody.setUsername("123456");
			
			authCodeMessageBody.setDeviceType(1);
			authCodeMessageBody.setFormatVersion("1.0");
			authCodeMessageBody.setMessageBody(authSunBody);
			authCodeMessageBody.setMessageType("01");
			authCodeMessageBody.setSendTime("20180725121212");
			authCodeMessageBody.setSerialNumber("1234567894564621");
			authCodeMessageBody.setServiceType("aafafasfsaffsfsfsfs");
			
			System.out.println(JSONObject.toJSONString(authCodeMessageBody));*/
			
			ServerInWareHouseMessage serverInWareHouseMessage = new ServerInWareHouseMessage();
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
			TextMessage message =  session.createTextMessage(JSONObject.toJSONString(serverInWareHouseMessage));
			Thread.sleep(1000);
			
			producer.send(message);
		}
		
		session.commit();
		session.close();
		connection.close();
	}
}
