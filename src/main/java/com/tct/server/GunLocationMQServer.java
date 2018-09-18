package com.tct.server;

import java.util.HashMap;
import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tct.cache.DeviceNoBingingWebUserCache;
import com.tct.thread.GateWayConsumerThread;
import com.tct.thread.ProducerThread;
import com.tct.thread.WebConsumerThread;

public class GunLocationMQServer {

	public static void main(String[] args) {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{
				"classpath:applicationContext-base.xml",
				"classpath:applicationContext-dao.xml",
				"classpath:applicationContext-transaction.xml",
				"classpath:applicationContext-jms.xml",
				"classpath:applicationContext-timer.xml",
				"classpath:applicationContext-redis.xml"});
		
		//ConnectionFactory cf= new ActiveMQConnectionFactory("failover:(tcp://112.74.51.194:61616)?initialReconnectDelay=1000&maxReconnectDelay=30000");
		//ConnectionFactory cf= new ActiveMQConnectionFactory("tcp://120.76.156.120:6160?useInactivityMonitor=true&useKeepAlive=true");
		//ConnectionFactory cf= new ActiveMQConnectionFactory("tcp://120.76.156.120:6160?useKeepAlive=true");
			
		/*HashMap<String, Object> paraMap = new HashMap<>();*/
		//paraMap.put("connectionFactory", cf);

/*		Hashtable<String, String> webQueueMap = new Hashtable<String,String>();
		webQueueMap.put("webSendQueue", "WebOutQueue");
		webQueueMap.put("webRecQueue","WebInQueue");
		DeviceNoBingingWebUserCache.getOnlineUserQueueMap().put("WebServer", webQueueMap);
		
		Hashtable<String, String> nettyQueuemap = new Hashtable<String,String>();
		nettyQueuemap.put("nettyRecQue", "InputQueue");
		nettyQueuemap.put("nettySendQue", "OutQueue");
		DeviceNoBingingWebUserCache.getOnlineUserQueueMap().put("NettyServer", nettyQueuemap);*/
		
/*		ProducerThread producerThread =new ProducerThread(paraMap,"producer");
		producerThread.start();*/
		
/*		GateWayConsumerThread gateWayConsumerThread=new GateWayConsumerThread(paraMap,"gateWayConsumer");
		gateWayConsumerThread.start();
		
		paraMap.put("queneName","WebInQueue");
		WebConsumerThread webConsumerThread=new WebConsumerThread(paraMap,"webConsumer");
		webConsumerThread.start();*/
	}

}
