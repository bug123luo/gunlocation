package com.tct.thread;

import java.util.HashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.tct.codec.MessageCodec;
import com.tct.codec.MessageCodecSelector;
import com.tct.service.ServiceSelector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebConsumerThread extends Thread {	
	private ConnectionFactory cf=null;
	private String queueName = null;
	
	public WebConsumerThread(HashMap<String, Object> paraMap,String threadName) {
		super(threadName);
		this.cf = (ConnectionFactory) paraMap.get("connectionFactory");
		this.queueName = (String) paraMap.get("queneName");
	}
	
	public void run() {
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageConsumer consumer = null;
		try {
			connection = this.cf.createConnection();
			connection.start();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			
			destination =  session.createQueue(this.queueName);
			consumer =  session.createConsumer(destination);
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {			
			while(true) {
				
				TextMessage textMessage = (TextMessage) consumer.receive();
				
				if(null !=textMessage) {
					//编解码器选择器
					MessageCodecSelector messageCodecSelector = new MessageCodecSelector();
					
					MessageCodec messageCodec = null;
					try {
						messageCodec = messageCodecSelector.getMessageDecode(textMessage.getText());
					} catch (Exception e2) {
						log.debug(e2+"消息解码器不存在");
					}
					
					//业务处理选择器
					ServiceSelector serviceSelector = new ServiceSelector();
					
					serviceSelector.handlerService(messageCodec, textMessage);
			
					try {
						session.commit();
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					continue;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
			try {
				session.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
