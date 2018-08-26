package com.tct.jms.producer;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebTopicSender {

	@Resource
	private JmsTemplate jmsTopicTemplate;
	
	public void sendMessage(Destination destination,final String message) {
		log.info("TopicSender 发送消息："+message);
		//设置topic为持久化
		/*jmsTopicTemplate.setPubSubDomain(true);
		jmsTopicTemplate.setDeliveryMode(2);*/
		jmsTopicTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage(message);
			}
		});
	}

	public JmsTemplate getJmsTopicTemplate() {
		return jmsTopicTemplate;
	}

	public void setJmsTopicTemplate(JmsTemplate jmsTopicTemplate) {
		this.jmsTopicTemplate = jmsTopicTemplate;
	}

	

}
