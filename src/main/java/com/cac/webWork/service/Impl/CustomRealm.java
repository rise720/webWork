package com.cac.webWork.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cac.webWork.dao.UserInfoTblMapper;
import com.cac.webWork.model.UserInfoTbl;

/**
 * 
 * @ClassName: CustomRealm
 * @Description: shiro 自定义realm
 *  				在Shiro中，最终是通过Realm来获取应用程序中的用户、角色及权限信息的
 * 					在Realm中会直接从我们的数据源中获取Shiro需要的验证信息。可以说，Realm是专用于安全框架的DAO.
 * @author JinWH
 * @date 2020年4月30日
 *
 */
public class CustomRealm  extends AuthorizingRealm{
	
	@Autowired
	private UserInfoTblMapper userInfoTblMapper;
	
	/**
	 * 授权用户权限
	 * 
     * 当访问到页面的时候，链接配置了相应的权限或者shiro标签才会执行此方法否则不会执行
     * 所以如果只是简单的身份认证没有权限的控制的话，那么这个方法可以不进行实现，直接返回null即可。
     * 如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "roles[100002]，perms[权限添加]");
     * 就说明访问/add这个链接必须要有 "权限添加" 这个权限和具有 "100002" 这个角色才可以访问
     * 如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "perms[权限添加]");
     * 就说明访问/add这个链接必须要有“权限添加”这个权限才可以访问
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		
		//获取用户信息
		UserInfoTbl userInfo = (UserInfoTbl)SecurityUtils.getSubject().getPrincipal();

		//添加角色
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole(String.valueOf(userInfo.getRoleId()));
        
        //获取权限
        Set<String> stringSet = new HashSet<>();
        switch(String.valueOf(userInfo.getRoleId())){
    		default:
    			stringSet.add("{\"user\":{\"r\":11,\"u\":10},\"class\":{\"r\":10,\"u\":0}}");
    			break;
        }
        authorizationInfo.setStringPermissions(stringSet);
        
        return authorizationInfo;
	}

	/**
	 * 身份认证。即登录通过账号和密码验证登陆人的身份信息。
	 * controller层中执行subject.login(token)，之后会调用此处方法。
	 * 在返回SimpleAuthenticationInfo对象后，会使用配置的密码比较器credentialsMatcher进行密码校验
	 * 校验对象是token中的密码，与SimpleAuthenticationInfo对象的密码与盐md5加密后的密码，两者进行比较，如果不不相同抛ExcessiveAttemptsException异常
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //获取用户名密码 第一种方式
//        String userName = (String) authenticationToken.getPrincipal();
//        String userPwd = new String((char[]) authenticationToken.getCredentials());        
        //获取用户名 密码 第二种方式
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        String userPwd = new String(usernamePasswordToken.getPassword());
        
        if(userName == null)
        	throw new UnknownAccountException("未知账户");
        //根据用户名从数据库取得用户信息
        UserInfoTbl record = new UserInfoTbl();
        record.setLoginName(userName);        
        List<UserInfoTbl> userInfoList =  userInfoTblMapper.findSelective(record);
        if(userInfoList == null || userInfoList.size() != 1)
        	throw new UnknownAccountException("未知账户");
        record = userInfoList.get(0);

        //自定义盐值，加盐顺序是md5(盐+原密码)
        ByteSource credentialsSalt = ByteSource.Util.bytes(record.getSalt());
//        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT),HASH_ITERATIONS).toHex();
        /**
         * 把数据库的密码和用户登录用的明文密码加密之后的形式进行对比,就是认证过程
         * 第一个参数传入的是对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
         * 第二个参数传的是查询数据库之后的数据库里的密码,而不是用户登录的明文密码
         * 第三个参数是盐值
         * 第四个参数是用户名
         */
        return new SimpleAuthenticationInfo(record, record.getPassword(), credentialsSalt, record.getLoginName());
	}
}