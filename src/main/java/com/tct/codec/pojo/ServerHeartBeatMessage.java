/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  ServerHeartBeatMessage.java   
 * @Package com.tct.codec.pojo   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年8月8日 下午7:00:02   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.codec.pojo;


import lombok.Getter;
import lombok.Setter;

/**   
 * @ClassName:  ServerHeartBeatMessage   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年8月8日 下午7:00:02   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

public class ServerHeartBeatMessage extends SimpleMessage{
	@Getter
	@Setter
	private ServerHeartBeatBody messageBody;
}
