package com.cac.webWork.service.Impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.cac.webWork.dao.CurriculumTblMapper;
import com.cac.webWork.dao.UserInfoTblMapper;
import com.cac.webWork.model.CurriculumTbl;
import com.cac.webWork.model.MyUser;
import com.cac.webWork.model.UserInfoTbl;
import com.cac.webWork.utility.SnowFlake;
import com.cac.webWork.utility.TargetDataSource;

/**
 * 
* @ClassName: MyServiceImpl
* @Description: 服务模板类
*
* @author: JinWH
* @date: 2019年4月10日 下午7:14:58 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年4月10日     JinWH           v1.0.0               修改原因
*/
@Service
@EnableCaching
public class MyServiceImpl {
	
	private static Logger logger = LoggerFactory.getLogger(MyServiceImpl.class);
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private UserInfoTblMapper userInfoTblMapper ;
	
	@Autowired
	private CurriculumTblMapper curriculumTblMapper;
	
	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private MyServiceImpl self;
	
	@PostConstruct
	public void init() {
		//模拟数据库
//		userMap = new HashMap<Long, MyUser>();
//		userMap.put(1L, new MyUser(1L, "user1"));
//		userMap.put(2L, new MyUser(2L, "user2"));
	}
	
	/**
	 * 
	* @Function: getUser
	* @Description: 获取用户信息，如果缓存中存在则直接读缓存，否则读数据库返回用户信息，并存至缓存
	*
	* @param: @param userId
	* @param: @param userName
	* @param: @return
	* @return：MyUser
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年4月17日 下午6:59:59 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年4月17日     JinWH           v1.0.0               修改原因
	 */
	@Cacheable(value = "getUserById") //注解key属性可以执行缓存对象user(可以理解为一个map)的key
	public MyUser getUser(Long userId, String userName) {
		logger.info(MessageFormat.format("userId:{0} 没有缓存，查询数据库获取用户信息",userId));
		MyUser value = (MyUser) redisTemplate.opsForValue().get(userId.toString());
		UserInfoTbl record = new UserInfoTbl();
		record.setLoginName(userId.toString());
		if(value == null) {
			List<UserInfoTbl> userInfoList = userInfoTblMapper.findSelective(record);
			if(!StringUtils.isEmpty(userInfoList) && userInfoList.size() == 1) {
				value = new MyUser(Long.valueOf(userInfoList.get(0).getLoginName()), userInfoList.get(0).getUserName());
			}
		}
		return value;
	}
	
	/**
	 * 
	* @Function: setUser
	* @Description: 设置用户
	*
	* @param: @param userId
	* @param: @param userName
	* @param: @return
	* @return：boolean
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年4月18日 下午6:43:50 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年4月18日     JinWH           v1.0.0               修改原因
	 */
	public boolean setUser(Long userId, String userName) {
		
		/*
		 * 内存数据库存储
		 */
		MyUser value = (MyUser) redisTemplate.opsForValue().get(userId.toString());
		logger.info(MessageFormat.format("redis中的对应userId:{0}的userName是{1}", userId, value));
		redisTemplate.opsForValue().set(userId.toString(), new MyUser(userId, userName));
		
		/*
		 * 数据库存储
		 */
		UserInfoTbl userInfo = new UserInfoTbl();
		SnowFlake idworker = new SnowFlake(0, 0);
//		if(userId == null)
			userInfo.setLoginName(String.valueOf(idworker.nextId()));
//		else
//			userInfo.setId(String.valueOf(userId));
		userInfo.setUserName("userName");

		
		if(userInfoTblMapper.insert(userInfo) > 0 )
			return true;
		return false;
	}
	
	
	/**
	 * 
	* @Function: getCurriculumBySlaveDataSource
	* @Description: 通过不同的数据源访问数据库获取相应信息
	*
	* @param: @param id
	* @param: @return
	* @return：CurriculumTbl
	* @throws：异常描述
	*
	* @author: JinWH
	* @date: 2019年5月6日 下午6:47:56 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2019年5月6日     JinWH           v1.0.0               修改原因
	 */
	public CurriculumTbl getCurriculumBySlaveDataSource(int id) {
		logger.info(MessageFormat.format("根据id取课程信息，数据源：{0}",dataSource.getClass().toString()));
		/**
		 * 重要！！
		 * 因为aop的特性，如果是类里调用内部方法，则不会触发aop。导致切换数据源无效！
		 * 所以这种方式无效  CurriculumTbl tmp = getCurriculumByPrimaryKey(id); 
		 * 因此需要通过调用外部类的方式，触发aop
		 */
		CurriculumTbl tmp = self.getCurriculumByPrimaryKey(id);
		UserInfoTbl tmp2 = userInfoTblMapper.selectByPrimaryKey(Long.valueOf(id));
		return tmp;
	}
	
	@TargetDataSource(name = "ds1")
	public CurriculumTbl getCurriculumByPrimaryKey(int id) {
		return curriculumTblMapper.selectByPrimaryKey(id);
	}
	
	
	public String createUser(UserInfoTbl userInfo) {
		UserInfoTbl record = new UserInfoTbl();
		if(StringUtils.isEmpty(userInfo.getLoginName()) || StringUtils.isEmpty(userInfo.getPassword()))
			return "新增用户的用户名或密码不符合规范";
		record.setLoginName(userInfo.getLoginName());
		List<UserInfoTbl> userInfoList = userInfoTblMapper.findSelective(record);
		if(userInfoList.size() > 0)
			return "新增用户的用户名已存在";
		//生成随机盐值
		record.setSalt("cac" + String.valueOf(System.currentTimeMillis()));
		//生成密码
		record.setPassword(DigestUtils.md5DigestAsHex((record.getSalt() + userInfo.getPassword()).getBytes()));
		//生成默认角色id
		record.setRoleId(Long.valueOf(1l));
		//生成时间
		record.setCreateTime(new Date());
		record.setUserName(record.getLoginName());
		
		if(userInfoTblMapper.insertSelective(record) == 0)
			return "新增用户失败";
		
		return "OK";	
	}

	public List<UserInfoTbl> showUser(UserInfoTbl userInfo) {
		
		return userInfoTblMapper.findSelective(userInfo);
	}
}
