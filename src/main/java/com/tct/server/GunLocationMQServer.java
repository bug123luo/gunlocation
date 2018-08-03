package com.tct.server;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tct.cache.UserOnlineQueueCache;
import com.tct.thread.GateWayConsumerThread;
import com.tct.thread.ProducerThread;
import com.tct.thread.WebConsumerThread;

public class GunLocationMQServer {

	public static void main(String[] args) {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath:applicationContext-dao.xml","classpath:applicationContext-transaction.xml"});
		
		/*ConnectionFactory cf= new ActiveMQConnectionFactory("tcp://112.74.51.194:61616");*/
		ConnectionFactory cf= new ActiveMQConnectionFactory("tcp://120.76.156.120:6160");
		
		Hashtable<String, String> messagemap = new Hashtable<String,String>();
		messagemap.put("sendQueue", "WebOutQueue");
		UserOnlineQueueCache.getOnlineUserQueueMap().put("WebOutQueue", messagemap);
		
		
		HashMap<String, Object> paraMap = new HashMap<>();
		paraMap.put("connectionFactory", cf);
		paraMap.put("nettyRecQue", "InputQueue");
		
		messagemap.put("nettyRecQue", "InputQueue");
		messagemap.put("nettySendQue", "OutQueue");
		UserOnlineQueueCache.getOnlineUserQueueMap().put("NettyServer", messagemap);
		/*paraMap.put("queneName", "ubo");*/
		
		ProducerThread producerThread =new ProducerThread(paraMap,"producer");
		producerThread.start();
		
		GateWayConsumerThread gateWayConsumerThread=new GateWayConsumerThread(paraMap,"gateWayConsumer");
		gateWayConsumerThread.start();
		
		paraMap.put("queneName","WebInQueue");
		WebConsumerThread webConsumerThread=new WebConsumerThread(paraMap,"webConsumer");
		webConsumerThread.start();
	}

}
