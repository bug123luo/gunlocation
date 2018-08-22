/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  DeviceBulletNumGetReplyMessage.java   
 * @Package com.tct.service.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年8月21日 上午11:25:03   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.apache.ibatis.type.IntegerTypeHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.cache.UserOnlineSessionCache;
import com.tct.codec.pojo.DeviceBulletNumGetReplyMessage;
import com.tct.codec.pojo.SimpleReplyMessage;
import com.tct.jms.producer.OutQueueSender;
import com.tct.jms.producer.WebOutQueueSender;
import com.tct.mapper.DeviceGunCustomMapper;
import com.tct.mapper.DeviceLocationCustomMapper;
import com.tct.mapper.GunCustomMapper;
import com.tct.po.DeviceGunCustom;
import com.tct.po.DeviceGunQueryVo;
import com.tct.po.DeviceLocationCustom;
import com.tct.po.DeviceLocationQueryVo;
import com.tct.po.GunCustom;
import com.tct.po.GunQueryVo;
import com.tct.service.SimpleService;
import com.tct.util.StringConstant;
import com.tct.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName:  DeviceBulletNumGetReplyMessage   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年8月21日 上午11:25:03   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@Slf4j
@Service(value="deviceBulletNumGetReplyService")
public class DeviceBulletNumGetReplyServiceImpl implements SimpleService {

	@Autowired
	GunCustomMapper gunCustomMapper;
	
	@Autowired
	DeviceLocationCustomMapper deviceLocationCustomMapper;
	
	@Autowired
	DeviceGunCustomMapper deviceGunCustomMapper;
	
	@Resource
	private OutQueueSender outQueueSender;
	
	@Resource
	private WebOutQueueSender webOutQueueSender;
	
	@Resource
	@Qualifier("outQueueDestination")
	private Destination outQueueDestination;
	
	@Resource
	@Qualifier("webOutQueueDestination")
	private Destination webOutQueueDestination;
	
	/**   
	 * <p>Title: handleCodeMsg</p>   
	 * <p>Description: </p>   
	 * @param msg
	 * @return
	 * @throws Exception   
	 * @see com.tct.service.SimpleService#handleCodeMsg(java.lang.Object)   
	 */
	@Override
	public boolean handleCodeMsg(Object msg) throws Exception {
		DeviceBulletNumGetReplyMessage message=(DeviceBulletNumGetReplyMessage)msg;
		
		ConcurrentHashMap<String, String> userOnlineSessionCache = UserOnlineSessionCache.getuserSessionMap();
		
		String sessionToken = message.getSessionToken();
		
		String deviceNo= (String) StringUtil.getKey(userOnlineSessionCache, sessionToken);
		
		if (deviceNo!=null) {
			log.info("该用户不是登录用户，不允许发送弹射计数");
			return false;
		}
		
		//更新 枪支位置信息表
		DeviceLocationQueryVo deviceLocationQueryVo = new DeviceLocationQueryVo();
		DeviceLocationCustom deviceLocationCustom = new DeviceLocationCustom();
		deviceLocationCustom.setDeviceNo(deviceNo);
		deviceLocationCustom.setLatitude(message.getMessageBody().getLa());
		deviceLocationCustom.setLongitude(message.getMessageBody().getLo());
		deviceLocationCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getNowTime()));
		deviceLocationQueryVo.setDeviceLocationCustom(deviceLocationCustom);
		int i = deviceLocationCustomMapper.updateByPrimaryKeySelective(deviceLocationCustom);
		
		//更新枪支信息表
		DeviceGunQueryVo deviceGunQueryVo = new DeviceGunQueryVo();
		DeviceGunCustom deviceGunCustom = new DeviceGunCustom();
		deviceGunCustom.setDeviceNo(deviceNo);
		DeviceGunCustom deviceGunCustom2 = deviceGunCustomMapper.selectByDeviceNo(deviceGunQueryVo);
		
		if(deviceGunCustom2!=null) {
			GunCustom gunCustom = new GunCustom();
			gunCustom.setUpdateTime(StringUtil.getDate(message.getMessageBody().getNowTime()));
			gunCustom.setBluetoothMac(deviceGunCustom2.getGunMac());
			gunCustom.setTotalBulletNumber(Integer.valueOf(message.getMessageBody().getBulletNumber()));
			gunCustomMapper.updateSelective(gunCustom);
				
			String searchToClienJson = JSONObject.toJSONString(message);
			webOutQueueSender.sendMessage(webOutQueueDestination, searchToClienJson);
		}else {
			return false;
		}
		return true;
	}

}
