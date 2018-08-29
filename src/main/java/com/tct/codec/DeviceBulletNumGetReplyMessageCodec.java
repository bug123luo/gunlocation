/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  DeviceBulletNumGetReplyMessageCodec.java   
 * @Package com.tct.codec   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年8月21日 上午11:17:50   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.codec;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.pojo.DeviceBulletNumGetBody;
import com.tct.codec.pojo.DeviceBulletNumGetMessage;
import com.tct.codec.pojo.DeviceBulletNumGetReplyBody;
import com.tct.codec.pojo.DeviceBulletNumGetReplyMessage;
import com.tct.util.CoordinateConvertUtil;

/**   
 * @ClassName:  DeviceBulletNumGetReplyMessageCodec   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年8月21日 上午11:17:50   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public class DeviceBulletNumGetReplyMessageCodec implements MessageCodec {

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
		DeviceBulletNumGetReplyMessage deviceBulletNumGetReplyMessage = new DeviceBulletNumGetReplyMessage();
		deviceBulletNumGetReplyMessage.setServiceType(json.getString("serviceType"));
		deviceBulletNumGetReplyMessage.setFormatVersion(json.getString("formatVersion"));
		deviceBulletNumGetReplyMessage.setDeviceType(json.getInteger("deviceType"));
		deviceBulletNumGetReplyMessage.setSerialNumber(json.getString("serialNumber"));
		deviceBulletNumGetReplyMessage.setMessageType(json.getString("messageType"));
		deviceBulletNumGetReplyMessage.setSendTime(json.getString("sendTime"));
		deviceBulletNumGetReplyMessage.setMessageBody(json.getObject("messageBody", DeviceBulletNumGetReplyBody.class));
/*		double la=Double.parseDouble(deviceBulletNumGetReplyMessage.getMessageBody().getLa());
		double lo=Double.parseDouble(deviceBulletNumGetReplyMessage.getMessageBody().getLo());
		double[] dtemp=CoordinateConvertUtil.wgs2BD09(la,lo);
		deviceBulletNumGetReplyMessage.getMessageBody().setLa(Double.toString(dtemp[0]));
		deviceBulletNumGetReplyMessage.getMessageBody().setLo(Double.toString(dtemp[1]));*/
		deviceBulletNumGetReplyMessage.setSessionToken(json.getString("sessionToken"));
		
		//authCodeMessage.setMessageBody((AuthCodeMessageBody)json.get("messageBody"));
		
		return deviceBulletNumGetReplyMessage;
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
