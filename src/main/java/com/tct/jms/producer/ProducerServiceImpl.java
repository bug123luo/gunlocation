package com.tct.jms.producer;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service(value="producerService")
public class ProducerServiceImpl implements ProducerService {

	@Autowired
    JmsTemplate jmsTemplate;

    @Resource(name = "webOutQueueDestination")
    Destination destination;
	
	@Override
	public void sendMessage(String message) {
		
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                System.out.println("发送消息 = [" + textMessage.getText() + "]");
                return textMessage;
			}
        });	    
	}

}
