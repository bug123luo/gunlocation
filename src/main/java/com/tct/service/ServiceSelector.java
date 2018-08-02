package com.tct.service;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import com.tct.codec.AuthCodeMessageCodec;
import com.tct.codec.ClientDeviceBindingMessageCodec;
import com.tct.codec.ClientHeartBeatMessageCodec;
import com.tct.codec.ClientHeartBeatReplyMessageCodec;
import com.tct.codec.ClientOffLocationWarningMessageCodec;
import com.tct.codec.ClientOutWareHouseMessageCodec;
import com.tct.codec.DeviceBulletCountMessageCodec;
import com.tct.codec.MessageCodec;
import com.tct.codec.ServerInWareHouseMessageCodec;
import com.tct.codec.ServerInWareHouseMessageReplyCodec;
import com.tct.codec.ServerOutWareHouseMessageCodec;
import com.tct.codec.ServerOutWareHouseReplyMessageCodec;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.ClientDeviceBindingMessage;
import com.tct.codec.pojo.ClientHeartBeatMessage;
import com.tct.codec.pojo.ClientHeartBeatReplyMessage;
import com.tct.codec.pojo.ClientOffLocationWarningMessage;
import com.tct.codec.pojo.ClientOutWareHouseMessage;
import com.tct.codec.pojo.DeviceBulletCountMessage;
import com.tct.codec.pojo.ServerInWareHouseMessage;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.codec.pojo.ServerOutWareHouseMessage;
import com.tct.codec.pojo.ServerOutWareHouseReplyMessage;


public class ServiceSelector {
	
	public boolean handlerService(MessageCodec messageCodec,TextMessage textMessage) {
		
		boolean flag = false;
		
		if(messageCodec instanceof AuthCodeMessageCodec) {
			AuthCodeMessage authCodeMessage = null;
			try {
				authCodeMessage = (AuthCodeMessage) messageCodec.decode(textMessage.getText());
				AuthCodeService authCodeService = SpringContextUtil.getBean("authCodeService");
				authCodeService.handleCodeMsg(authCodeMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ServerOutWareHouseMessageCodec) {
			ServerOutWareHouseMessage serverOutWareHouseMessage = null;
			try {
				serverOutWareHouseMessage = (ServerOutWareHouseMessage) messageCodec.decode(textMessage.getText());
				ServerOutWareHouseService serverOutWareHouseService = SpringContextUtil.getBean("serverOutWareHouseService");
				serverOutWareHouseService.handleCodeMsg(serverOutWareHouseMessage);
				flag = true;
			} catch (JMSException e) {
				e.printStackTrace();
				flag = false;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ServerOutWareHouseReplyMessageCodec) {
			ServerOutWareHouseReplyMessage serverOutWareHouseReplyMessage = null;
			try {
				serverOutWareHouseReplyMessage = (ServerOutWareHouseReplyMessage) messageCodec.decode(textMessage.getText());
				ServerOutWareHouseService serverOutWareHouseReplyService = SpringContextUtil.getBean("serverOutWareHouseReplyService");
				serverOutWareHouseReplyService.handleCodeMsg(serverOutWareHouseReplyMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ServerInWareHouseMessageCodec){
			ServerInWareHouseMessage serverInWareHouseMessage = null;
			try {
				serverInWareHouseMessage =(ServerInWareHouseMessage) messageCodec.decode(textMessage.getText());
				ServerInWareHouseService serverInWareHouseService = SpringContextUtil.getBean("serverInWareHouseService");
				serverInWareHouseService.handleCodeMsg(serverInWareHouseMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ServerInWareHouseMessageReplyCodec) {
			ServerInWareHouseReplyMessage serverInWareHouseReplyMessage = null;
			try {
				serverInWareHouseReplyMessage = (ServerInWareHouseReplyMessage) messageCodec.decode(textMessage.getText());
				ServerInWareHouseReplyService serverInWareHouseReplyService =  SpringContextUtil.getBean("serverInWareHouseReplyService");
				serverInWareHouseReplyService.handleCodeMsg(serverInWareHouseReplyMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ClientHeartBeatMessageCodec) {
			try {
				ClientHeartBeatMessage clientHeartBeatMessage = new ClientHeartBeatMessage();
				ClientHeartBeatService clientHeartBeatService =  SpringContextUtil.getBean("clientHeartBeatService");
				clientHeartBeatService.handleCodeMsg(clientHeartBeatMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ClientOffLocationWarningMessageCodec) {
			try {
				ClientOffLocationWarningMessage clientOffLocationWarningMessage = new ClientOffLocationWarningMessage();
				ClientOffLocationWarningService clientOffLocationWarningService = SpringContextUtil.getBean("clientOffLocationWarningService");
				clientOffLocationWarningService.handleCodeMsg(clientOffLocationWarningMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if (messageCodec instanceof ClientDeviceBindingMessageCodec) {
			try {
				ClientDeviceBindingMessage clientDeviceBindingMessage = new ClientDeviceBindingMessage();
				ClientDeviceBindingService clientDeviceBindingService = SpringContextUtil.getBean("clientDeviceBindingService");
				clientDeviceBindingService.handleCodeMsg(clientDeviceBindingMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = true;
			}
		}else if (messageCodec instanceof ClientOutWareHouseMessageCodec) {
			try {
				ClientOutWareHouseMessage clientOutWareHouseMessage =  new ClientOutWareHouseMessage();
				ClientOutWareHouseService clientOutWareHouseService =  SpringContextUtil.getBean("clientOutWareHouseService");
				clientOutWareHouseService.handleCodeMsg(clientOutWareHouseMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = true;
			}
			
		}else if (messageCodec instanceof DeviceBulletCountMessageCodec) {
			try {
				DeviceBulletCountMessage deviceBulletCountMessage =  new DeviceBulletCountMessage();
				DeviceBulletCountService deviceBulletCountService =  SpringContextUtil.getBean("deviceBulletCountService");
				deviceBulletCountService.handleCodeMsg(deviceBulletCountMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = true;
			}
		}
		return flag;
	}
}
