package com.tct.thread;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.tct.cache.UnSendReplyMessageCache;
import com.tct.cache.UnhandlerReceiveMessageCache;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ProducerThread extends Thread {

	private ConnectionFactory cf=null;
	private String queueName = null;
	private String userName = null;
	
	public ProducerThread(HashMap<String, Object> paraMap,String threadName) {
		super(threadName);
		this.cf =  (ConnectionFactory) paraMap.get("connectionFactory");
	}
	
	public void run() {
		
		Connection connection=null;
		Session session=null;
		try {
			connection = cf.createConnection();
			connection.start();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			while(true) {
				
				//从未发消息缓存队列中获取消息，并且构造json消息，将消息发送出去
				ConcurrentHashMap<String, Hashtable<String, Object>> unhandlerReceiveMessageHashMap = UnhandlerReceiveMessageCache.getUnSendReplyMessageMap();
				ConcurrentHashMap<String, Hashtable<String, Object>> unSendReplyMessageCacheMap = UnSendReplyMessageCache.getUnSendReplyMessageMap();
				
				for(String deviceNo:unSendReplyMessageCacheMap.keySet()) {	
					Hashtable<String, Object> unSendMessageMap =unSendReplyMessageCacheMap.get(deviceNo);
					Destination destination =  session.createQueue(deviceNo);
					MessageProducer producer =  session.createProducer(destination);
				
					for(String serialNumber:unSendMessageMap.keySet()) {
						String jsonMsg = (String) unSendMessageMap.get(serialNumber);
						TextMessage message = session.createTextMessage(jsonMsg);
						producer.send(message);
						unSendMessageMap.remove(serialNumber);
						
						session.commit();
						
						Hashtable<String, Object> messageMap=new Hashtable<String,Object>();
						if (unhandlerReceiveMessageHashMap.containsKey(deviceNo)){
							messageMap= unhandlerReceiveMessageHashMap.get(deviceNo);
							messageMap.remove(serialNumber);
						}
					}	
				}	
			}
					
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				session.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
		
}
