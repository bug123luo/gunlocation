/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  OutQueueSender.java   
 * @Package com.tct.jms.producer   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年8月16日 下午3:23:10   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.jms.producer;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName:  OutQueueSender   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年8月16日 下午3:23:10   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Slf4j
public class OutQueueSender {
	
	@Resource
	private JmsTemplate jmsQueueTemplate;
 	
	//发送消息
	public void sendMessage(Destination destination,final String message) { 
		log.info("OutQueue发送消息：");
		log.info(message);
		/*jmsQueueTemplate.setPubSubDomain(false);*/
		jmsQueueTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(message);
			}
		});
		
	}

	public JmsTemplate getJmsQueueTemplate() {
		return jmsQueueTemplate;
	}

	public void setJmsQueueTemplate(JmsTemplate jmsQueueTemplate) {
		this.jmsQueueTemplate = jmsQueueTemplate;
	}
	
	
	
}

