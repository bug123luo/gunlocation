package com.tct.jms.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Service;

@Service(value="consumerMessageListener")
public class ConsumerMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
	        TextMessage textMessage = (TextMessage) message;
	        try {
	            System.out.println("接收message: " + textMessage.getText());
	        } catch (JMSException e) {
	            e.printStackTrace();
	        }
		}

	}

}
