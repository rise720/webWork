package com.cac.webWork.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cac.webWork.service.Impl.CustomRealm;

@Configuration
public class ShiroConfig {
	
	/**
	 * 
	 * @Title: shiroFilter
	 * @Description: 处理拦截资源文件问题
	 * 					注意：初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * 					Web应用中,Shiro可控制的Web请求必须经过Shiro主过滤器的拦截
     * 					anon:所有url都都可以匿名访问;
     *					authc: 需要认证才能进行访问;
     *					user:配置记住我或认证通过可以访问；
	 * @param @param securityManager
	 * @param @return 参数
	 * @return ShiroFilterFactoryBean 返回类型
	 * @throws
	 */
	@Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        
        //必须设置 SecurityManager,Shiro的核心安全接口
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //这里是后台的接口名,非页面，如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/my/unauth");
        //这里的/index是后台的接口名,非页面,登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面,该配置无效，并不会进行页面跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/my/unauthorized");

        //自定义拦截器限制并发人数：
        //LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //限制同一帐号同时在线的个数
        //filtersMap.put("kickout", kickoutSessionControlFilter());
        //shiroFilterFactoryBean.setFilters(filtersMap);

        //配置访问权限 必须是LinkedHashMap，因为它必须保证有序
        //过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 一定要注意顺序,否则就不好使了
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //配置不登录可以访问的资源，anon 表示资源都可以匿名访问
        filterChainDefinitionMap.put("/my/login", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        
        //logout是shiro提供的过滤器，访问该地址，自动登出；但登出后会自动跳转到logout页面，如果不想改写过滤器，还是controller层使用subject.logout()方法登出
        //filterChainDefinitionMap.put("/my/logout", "logout");
        //需要角色或权限验证，则按此规则：("/地址", "roles[100002]，perms[权限添加]")
        //filterChainDefinitionMap.put("/userInfo/del", "perms[\"userInfo:del\"]");
        //filterChainDefinitionMap.put("/my/createUser","roles[0]");
//        filterChainDefinitionMap.put("/my/createUser","perms[\"user:edit\"]");
//        filterChainDefinitionMap.put("/my/showUser","perms[\"user:show\"]");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "authc");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

	/**
	 * 
	 * @Title: securityManager
	 * @Description: 配置核心安全事务管理器
	 * @param @param customRealm
	 * @param @return 参数
	 * @return SecurityManager 返回类型
	 * @throws
	 */
    @Bean
    public SecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager  = new DefaultWebSecurityManager();
        //设置自定义realm
        securityManager.setRealm(customRealm);
        //session管理
        securityManager.setSessionManager(defaultWebSessionManager());
        
        //配置记住我：
        //securityManager.setRememberMeManager(rememberMeManager());
        
        //配置 redis缓存管理器：
        //securityManager.setCacheManager(getEhCacheManager());
        
        //配置自定义session管理，使用redis：
        //securityManager.setSessionManager(sessionManager());
        
        return securityManager;
    }
    
    /**
     * 
     * @Title: lifecycleBeanPostProcessor
     * @Description: 配置Shiro生命周期处理器
     * @param @return 参数
     * @return LifecycleBeanPostProcessor 返回类型
     * @throws
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    /**
     * 
     * @Title: customRealm
     * @Description: 自定义身份认证realm
     * @param @return 参数
     * @return CustomRealm 返回类型
     * @throws
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        
        //customRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
//        customRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
//        customRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        //customRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        //customRealm.setAuthorizationCacheName("authorizationCache");
        
        //配置自定义密码比较器，默认应该是SimpleCredentialsMatcher，密码是明文时可以使用，密码是密文时使用HashedCredentialsMatcher
        customRealm.setCredentialsMatcher(credentialsMatcher());
        return customRealm;
    }
    
    /**
     * 
     * @Title: retryLimitHashedCredentialsMatcher
     * @Description: 重写密码比较器
     * @param @return 参数
     * @return RetryLimitHashedCredentialsMatcher 返回类型
     * @throws
     */
    @Bean("credentialsMatcher")
    public HashedCredentialsMatcher credentialsMatcher(){
//        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        
        //加密算法的名称，也可以设置SHA-256等其他加密算法名字  
        credentialsMatcher.setHashAlgorithmName("MD5");
        //配置 散列的次数，比如散列两次，相当于 md5(md5(""));
        credentialsMatcher.setHashIterations(1);
        //true加密用的hex编码，false用的base64编码
        credentialsMatcher.setStoredCredentialsHexEncoded(true);

        return credentialsMatcher;
    }
    
    /**
     * 
     * @Title: defaultWebSessionManager
     * @Description: session管理，控制会话超期时间等
     * @param @return 参数
     * @return DefaultWebSessionManager 返回类型
     * @throws
     */
    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
	DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
	// 会话过期时间，单位：毫秒(在无操作时开始计时)
	defaultWebSessionManager.setGlobalSessionTimeout(6000 * 20);
	defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
	defaultWebSessionManager.setSessionIdCookieEnabled(true);
	return defaultWebSessionManager;
    }

}
