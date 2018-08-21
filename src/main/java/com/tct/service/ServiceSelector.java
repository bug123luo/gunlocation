package com.tct.service;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import com.tct.codec.AuthCodeMessageCodec;
import com.tct.codec.ClientDeviceBindingMessageCodec;
import com.tct.codec.ClientHeartBeatMessageCodec;
import com.tct.codec.ClientInWareHouseMessageCodec;
import com.tct.codec.ClientOffLocationWarningMessageCodec;
import com.tct.codec.ClientOutWareHouseMessageCodec;
import com.tct.codec.ClientVersionSyncMessageCodec;
import com.tct.codec.DeviceBulletCountMessageCodec;
import com.tct.codec.DeviceBulletNumGetMessageCodec;
import com.tct.codec.DeviceBulletNumGetReplyMessageCodec;
import com.tct.codec.MessageCodec;
import com.tct.codec.ServerInWareHouseMessageCodec;
import com.tct.codec.ServerInWareHouseMessageReplyCodec;
import com.tct.codec.ServerOffLocationSearchMessageCodec;
import com.tct.codec.ServerOffLocationSearchReplyMessageCodec;
import com.tct.codec.ServerOffLocationWarningStartStopMessageCodec;
import com.tct.codec.ServerOffLocationWarningStartStopReplyMessageCodec;
import com.tct.codec.ServerOutWareHouseMessageCodec;
import com.tct.codec.ServerOutWareHouseReplyMessageCodec;
import com.tct.codec.pojo.AuthCodeMessage;
import com.tct.codec.pojo.ClientDeviceBindingMessage;
import com.tct.codec.pojo.ClientHeartBeatMessage;
import com.tct.codec.pojo.ClientInWareHouseMessage;
import com.tct.codec.pojo.ClientOffLocationWarningMessage;
import com.tct.codec.pojo.ClientOutWareHouseMessage;
import com.tct.codec.pojo.ClientVersionSyncMessage;
import com.tct.codec.pojo.DeviceBulletCountMessage;
import com.tct.codec.pojo.DeviceBulletNumGetMessage;
import com.tct.codec.pojo.DeviceBulletNumGetReplyMessage;
import com.tct.codec.pojo.ServerInWareHouseMessage;
import com.tct.codec.pojo.ServerInWareHouseReplyMessage;
import com.tct.codec.pojo.ServerOffLocationSearchMessage;
import com.tct.codec.pojo.ServerOffLocationSearchReplyMessage;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopMessage;
import com.tct.codec.pojo.ServerOffLocationWarningStartStopReplyMessage;
import com.tct.codec.pojo.ServerOutWareHouseMessage;
import com.tct.codec.pojo.ServerOutWareHouseReplyMessage;


public class ServiceSelector {
	
	public boolean handlerService(MessageCodec messageCodec,TextMessage textMessage) {
		
		boolean flag = false;
		
		if(messageCodec instanceof AuthCodeMessageCodec) {
			AuthCodeMessage authCodeMessage = null;
			try {
				authCodeMessage = (AuthCodeMessage) messageCodec.decode(textMessage.getText());
				SimpleService authCodeService = SpringContextUtil.getBean("authCodeService");
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
				SimpleService serverOutWareHouseService = SpringContextUtil.getBean("serverOutWareHouseService");
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
				SimpleService serverOutWareHouseReplyService = SpringContextUtil.getBean("serverOutWareHouseReplyService");
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
				SimpleService serverInWareHouseService = SpringContextUtil.getBean("serverInWareHouseService");
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
				SimpleService serverInWareHouseReplyService =  SpringContextUtil.getBean("serverInWareHouseReplyService");
				serverInWareHouseReplyService.handleCodeMsg(serverInWareHouseReplyMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ClientHeartBeatMessageCodec) {
			try {
				ClientHeartBeatMessage clientHeartBeatMessage = (ClientHeartBeatMessage)messageCodec.decode(textMessage.getText());
				SimpleService clientHeartBeatService =  SpringContextUtil.getBean("clientHeartBeatService");
				clientHeartBeatService.handleCodeMsg(clientHeartBeatMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ClientOffLocationWarningMessageCodec) {
			try {
				ClientOffLocationWarningMessage clientOffLocationWarningMessage = (ClientOffLocationWarningMessage)messageCodec.decode(textMessage.getText());
				SimpleService clientOffLocationWarningService = SpringContextUtil.getBean("clientOffLocationWarningService");
				clientOffLocationWarningService.handleCodeMsg(clientOffLocationWarningMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if (messageCodec instanceof ClientDeviceBindingMessageCodec) {
			try {
				ClientDeviceBindingMessage clientDeviceBindingMessage = (ClientDeviceBindingMessage)messageCodec.decode(textMessage.getText());
				SimpleService clientDeviceBindingService = SpringContextUtil.getBean("clientDeviceBindingService");
				clientDeviceBindingService.handleCodeMsg(clientDeviceBindingMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if (messageCodec instanceof ClientOutWareHouseMessageCodec) {
			try {
				ClientOutWareHouseMessage clientOutWareHouseMessage =  (ClientOutWareHouseMessage)messageCodec.decode(textMessage.getText());
				SimpleService clientOutWareHouseService =  SpringContextUtil.getBean("clientOutWareHouseService");
				clientOutWareHouseService.handleCodeMsg(clientOutWareHouseMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
			
		}else if (messageCodec instanceof DeviceBulletCountMessageCodec) {
			try {
				DeviceBulletCountMessage deviceBulletCountMessage = (DeviceBulletCountMessage)messageCodec.decode(textMessage.getText());
				SimpleService deviceBulletCountService =  SpringContextUtil.getBean("deviceBulletCountService");
				deviceBulletCountService.handleCodeMsg(deviceBulletCountMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ClientInWareHouseMessageCodec){
			try {
				ClientInWareHouseMessage clientInWareHouseMessage = (ClientInWareHouseMessage)messageCodec.decode(textMessage.getText());
				SimpleService clientInWareHouseService =  SpringContextUtil.getBean("clientInWareHouseService");
				clientInWareHouseService.handleCodeMsg(clientInWareHouseMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if(messageCodec instanceof ServerOffLocationSearchMessageCodec) {
			try {
				ServerOffLocationSearchMessage serverOffLocationSearchMessage = (ServerOffLocationSearchMessage)messageCodec.decode(textMessage.getText());
				SimpleService serverOffLocationSearchService = SpringContextUtil.getBean("serverOffLocationSearchService");
				serverOffLocationSearchService.handleCodeMsg(serverOffLocationSearchMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if (messageCodec instanceof ServerOffLocationSearchReplyMessageCodec) {
			try {
				ServerOffLocationSearchReplyMessage serverOffLocationSearchReplyMessage =(ServerOffLocationSearchReplyMessage)messageCodec.decode(textMessage.getText());
				SimpleService serverOffLocationSearchService = SpringContextUtil.getBean("serverOffLocationSearchService");
				serverOffLocationSearchService.handleCodeMsg(serverOffLocationSearchReplyMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		else if (messageCodec instanceof ServerOffLocationWarningStartStopReplyMessageCodec) {
			try {
				ServerOffLocationWarningStartStopReplyMessage serverOffLocationWarningStartStopReplyMessage = (ServerOffLocationWarningStartStopReplyMessage)messageCodec.decode(textMessage.getText());
				SimpleService serverOffLocationWarningStartStopService = SpringContextUtil.getBean("serverOffLocationWarningStartStopService");
				serverOffLocationWarningStartStopService.handleCodeMsg(serverOffLocationWarningStartStopReplyMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		else if(messageCodec instanceof ServerOffLocationWarningStartStopMessageCodec) {
			try {
				ServerOffLocationWarningStartStopMessage serverOffLocationWarningStartStopMessage = (ServerOffLocationWarningStartStopMessage)messageCodec.decode(textMessage.getText());
				SimpleService serverOffLocationWarningStartStopService = SpringContextUtil.getBean("serverOffLocationWarningStartStopService");
				serverOffLocationWarningStartStopService.handleCodeMsg(serverOffLocationWarningStartStopMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if (messageCodec instanceof ClientVersionSyncMessageCodec) {
			try {
				ClientVersionSyncMessage clientVersionSyncMessage =  (ClientVersionSyncMessage)messageCodec.decode(textMessage.getText());
				SimpleService clientVersionSyncService = SpringContextUtil.getBean("clientVersionSyncService");
				clientVersionSyncService.handleCodeMsg(clientVersionSyncMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if (messageCodec instanceof DeviceBulletNumGetMessageCodec) {
			try {
				DeviceBulletNumGetMessage deviceBulletNumGetMessage = (DeviceBulletNumGetMessage)messageCodec.decode(textMessage.getText());
				SimpleService deviceBulletNumGetService = SpringContextUtil.getBean("deviceBulletNumGetService");
				deviceBulletNumGetService.handleCodeMsg(deviceBulletNumGetMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}else if (messageCodec instanceof DeviceBulletNumGetReplyMessageCodec) {
			try {
				DeviceBulletNumGetReplyMessage deviceBulletNumGetReplyMessage = (DeviceBulletNumGetReplyMessage)messageCodec.decode(textMessage.getText());
				SimpleService deviceBulletNumGetReplyService = SpringContextUtil.getBean("deviceBulletNumGetReplyService");
				deviceBulletNumGetReplyService.handleCodeMsg(deviceBulletNumGetReplyMessage);
				flag= true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
			
		}
		return flag;
	}
}
