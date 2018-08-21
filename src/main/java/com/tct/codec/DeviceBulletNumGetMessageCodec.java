/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  DeviceBulletNumGetMessageCodec.java   
 * @Package com.tct.codec   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年8月21日 上午10:47:20   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.DeviceBulletNumGetBody;
import com.tct.codec.pojo.DeviceBulletNumGetMessage;

/**   
 * @ClassName:  DeviceBulletNumGetMessageCodec   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年8月21日 上午10:47:20   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public class DeviceBulletNumGetMessageCodec implements MessageCodec {

	/**   
	 * <p>Title: decode</p>   
	 * <p>Description: </p>   
	 * @param inMsg
	 * @return
	 * @throws Exception   
	 * @see com.tct.codec.MessageCodec#decode(java.lang.String)   
	 */
	@Override
	public Object decode(String inMsg) throws Exception {
		JSONObject json= JSONObject.parseObject(inMsg);
		
		//log.info(json.toJSONString());
		DeviceBulletNumGetMessage deviceBulletNumGetMessage = new DeviceBulletNumGetMessage();
		deviceBulletNumGetMessage.setServiceType(json.getString("serviceType"));
		deviceBulletNumGetMessage.setFormatVersion(json.getString("formatVersion"));
		deviceBulletNumGetMessage.setDeviceType(json.getInteger("deviceType"));
		deviceBulletNumGetMessage.setSerialNumber(json.getString("serialNumber"));
		deviceBulletNumGetMessage.setMessageType(json.getString("messageType"));
		deviceBulletNumGetMessage.setSendTime(json.getString("sendTime"));
		deviceBulletNumGetMessage.setMessageBody(json.getObject("messageBody",DeviceBulletNumGetBody.class));
		deviceBulletNumGetMessage.setSessionToken(json.getString("sessionToken"));
		
		//authCodeMessage.setMessageBody((AuthCodeMessageBody)json.get("messageBody"));
		
		return deviceBulletNumGetMessage;
	}

	/**   
	 * <p>Title: encode</p>   
	 * <p>Description: </p>   
	 * @param outMsg
	 * @return
	 * @throws Exception   
	 * @see com.tct.codec.MessageCodec#encode(java.lang.Object)   
	 */
	@Override
	public String encode(Object outMsg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
