package com.tct.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


public class QueueReceiver {

	public static void main(String[] args) throws JMSException {
		ConnectionFactory cf= new ActiveMQConnectionFactory("tcp://112.74.51.194:61616");
		Connection connection =  cf.createConnection();
		connection.start();
		
		final Session session =  connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		Destination destination =  session.createQueue("my-queue");
		
		MessageConsumer consumer =  session.createConsumer(destination);
		int i=0;
		while(i<3) {
			i++;
			TextMessage message = (TextMessage) consumer.receive();
			session.commit();
			System.out.println("收到消息:"+message.getText());
		}
				
		session.close();
		connection.close();

	}

}
