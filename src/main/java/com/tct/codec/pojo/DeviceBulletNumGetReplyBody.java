/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  DeviceBulletNumGetReplyBody.java   
 * @Package com.tct.codec.pojo   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年8月21日 上午10:42:05   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.codec.pojo;

import java.util.Date;
import lombok.Data;

/**   
 * @ClassName:  DeviceBulletNumGetReplyBody   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年8月21日 上午10:42:05   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Data
public class DeviceBulletNumGetReplyBody {
	private String bulletNumber;
	private String lo;
	private String la;
	private String nowTime;
	private String authCode;
}
