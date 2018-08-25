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
	private JmsTemplate template;
	
	public void sendMessage(Destination destination,final String message) {
		log.info("TopicSender 发送消息："+message);
		//设置topic为持久化
		template.setPubSubDomain(true);
		template.setDeliveryMode(2);
		template.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage(message);
			}
		});
	}

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}
	
	
	
	
}
