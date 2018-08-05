package com.tct.jms.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Service;

import com.tct.codec.MessageCodec;
import com.tct.codec.MessageCodecSelector;
import com.tct.service.ServiceSelector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsumerMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
	        TextMessage textMessage = (TextMessage) message;

			log.info("接收消息");				
			if(null !=textMessage) {
				//编解码器选择器
				MessageCodecSelector messageCodecSelector = new MessageCodecSelector();			
				MessageCodec messageCodec = null;
				try {
					log.info(textMessage.getText());
					messageCodec = messageCodecSelector.getMessageDecode(textMessage.getText());
				} catch (Exception e2) {
					log.debug(e2+"消息解码器不存在");
				}
				
				//业务处理选择器
				ServiceSelector serviceSelector = new ServiceSelector();
				boolean flag = serviceSelector.handlerService(messageCodec, textMessage);
			}
		}else {
			log.info("收到非TextMessage消息类型");
		}
	}
}
