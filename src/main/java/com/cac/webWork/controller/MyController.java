package com.cac.webWork.controller;

import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cac.webWork.annotation.PermissionAnnotation;
import com.cac.webWork.config.MyConfig;
import com.cac.webWork.model.CurriculumTbl;
import com.cac.webWork.model.UserInfoTbl;
import com.cac.webWork.service.Impl.MyServiceImpl;
import com.cac.webWork.utility.MailService;

/**
 * 
 * @ClassName: MyController
 * @Description: 接收页面请求controller
 * @author JinWH
 * @date 2020年3月19日
 *
 */
@RestController
@RequestMapping(value = "/my")
public class MyController {
	private static Logger logger = LoggerFactory.getLogger(MyController.class);
	
	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private MyConfig myConfig;
	
	@Autowired
	private MyServiceImpl myService;
	
//	@Autowired
//	private MqSender mqSender;
	
//	@Autowired
//	private MqReceiver myReceiver;
	
	@Autowired
	private MailService mailService;

	/**
	 * 
	 * @Title: shiroLogin
	 * @Description: shiro 登录验证
	 * @param @param name
	 * @param @param password
	 * @param @return 参数
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String shiroLogin(@RequestParam("name") String name, @RequestParam("password") String password) {
		logger.info(MessageFormat.format("用户：{0}，进行登录验证中....", name));
		// 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        //通过shiro的session，拿到会话对象
        Session session = subject.getSession();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        // 执行认证登陆
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            return "未知账户";
        } catch (IncorrectCredentialsException e) {
            return "用户名或密码不正确";
        } catch (LockedAccountException e) {
            return "账户已锁定";
        } catch (ExcessiveAttemptsException e) {
            return "用户名或密码错误次数过多";
        } catch (AuthenticationException e) {
            return "用户名或密码不正确！";
        }
        if (subject.isAuthenticated()) {
            return "OK";
        } else {
            token.clear();
            return "登录失败";
        }
	}
	
	@RequestMapping(value = "/unauth", method = RequestMethod.GET)
	public String unauth() {
		return "未登录";
	}
	@RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
	public String unauthorized() {
		return "该用户没有权限";
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
		return "已退出";
	}
	/**
	 * 
	 * @Title: createUser
	 * @Description: 验证shiro权限，新增权限
	 * @param @param userInfo
	 * @param @return 参数
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/createUser", method = RequestMethod.PUT)
	public String createUser(@RequestBody UserInfoTbl userInfo) {
		logger.info("收到新增用户请求...");
		return myService.createUser(userInfo);
	}
	
	/**
	 * 
	 * @Title: showUser
	 * @Description: 验证shiro权限，查看权限
	 * @param @param userInfo
	 * @param @return 参数
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/showUser", method = RequestMethod.GET)
	public List<UserInfoTbl> showUser(@RequestBody UserInfoTbl userInfo) {
		logger.info("收到查询用户请求...");
		return myService.showUser(userInfo);
	}
	
	/**
	 * 
	* @Function: helloWorld
	* @Description: 获取配置文件参数
	*
	* @param: @return
	* @return：String
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年4月10日 下午6:05:44 
	 */
	@RequestMapping(value = "/getMyConfig")
	@PermissionAnnotation(permissionName = "user:u")
	public String helloWorld(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String name) {
		logger.info(myConfig.getT1T2() + name);
		return myConfig.getT1T2();
	}
	
	/**
	 * 
	* @Function: dataSource
	* @Description: 获取数据源
	*
	* @param: @return
	* @return：String
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年4月10日 下午6:10:29 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年4月10日     JinWH           v1.0.0               修改原因
	 */
	@RequestMapping(value = "/datasource")
	public String dataSource() {
		
		logger.info("dataSource:" + dataSource.getClass().toString());
		return dataSource.getClass().toString();
	}
	
	@RequestMapping(value = "/")
	public String hello() {
		logger.info("logback");
		logger.debug("dfafda");
		return "index.html";
	}
	
	/**
	 * 
	* @Function: getCacheContent
	* @Description: 获取缓存信息
	*
	* @param: @param request
	* @param: @param response
	* @param: @param name
	* @param: @return
	* @return：String
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年4月10日 下午7:12:27 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年4月10日     JinWH           v1.0.0               修改原因
	 */
	@RequestMapping(value = "/getCacheContent")
	public String getCacheContent(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) Long userId, @RequestParam(required = false) String userName) {
		return myService.getUser(userId,userName).getName();
	}
	
	/**
	 * 
	* @Function: setCacheContent
	* @Description: 设置缓存内容
	*
	* @param: @param request
	* @param: @param response
	* @param: @param userId
	* @param: @param userName
	* @param: @return
	* @return：String
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年4月18日 下午6:38:18 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年4月18日     JinWH           v1.0.0               修改原因
	 */
	@RequestMapping(value = "/setCacheContent")
	public boolean setCacheContent(HttpServletRequest request, HttpServletResponse response, Long userId, String userName) {
		return myService.setUser(userId, userName);
	}
	
	/**
	 * 
	* @Function: getCurriculumBySlaveDataSource
	* @Description: 通过切换数据源，获取不同数据库的数据
	* 切换数据源是通过自定义注解触发aop进行的。
	*
	* @param: @param request
	* @param: @param response
	* @param: @param id
	* @param: @return
	* @return：CurriculumTbl
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年5月6日 下午6:52:18 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年5月6日     JinWH           v1.0.0               修改原因
	 */
	@RequestMapping(value = "/getCurriculumBySlaveDataSource")
	public CurriculumTbl getCurriculumBySlaveDataSource(HttpServletRequest request, HttpServletResponse response, int id) {
		return myService.getCurriculumBySlaveDataSource(id);
	}
	
	/**
	 * 
	* @Function: putMq
	* @Description: 该函数的功能描述
	*
	* @param: @param request
	* @param: @param response
	* @param: @return
	* @return：String
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年5月9日 下午5:30:01 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年5月9日     JinWH           v1.0.0               修改原因
	 */
//	@RequestMapping(value = "/putMq")
//	public String putMq(HttpServletRequest request, HttpServletResponse response, String message) {
//		mqSender.publishMqMessage(message);
//		return message;
//	}
	
	
	

	/**
	 * 
	* @Function: sendMail
	* @Description: 发送邮件
	*
	* @param: @param request
	* @param: @param response
	* @param: @param to
	* @param: @param subject
	* @param: @param content
	* @param: @return
	* @return：String
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年5月9日 下午7:48:30 
	 */
	@RequestMapping(value = "/sendMail")
	public String sendMail(HttpServletRequest request, HttpServletResponse response, String to, String subject, String content) {
		mailService.sendSimpleMail(to, subject, content);
		return content;
	}
}
