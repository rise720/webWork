package com.cac.webWork.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 
* @ClassName: MyFilter.java
* @Description: 过滤器使用方式
*
* @version: v1.0.0
* @author: JinWH
* @date: 2019年3月22日 下午1:09:06 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年3月22日     JinWH           v1.0.0               修改原因
 */

@WebFilter(filterName = "MyFilter", urlPatterns = "/*",
initParams = {
    @WebInitParam(name = "excludedUrls", value = "my/datasource")
}
)
public class MyFilter implements Filter{
	
	private static final String ALLOWORIGIN_CORS = "*";

	private Logger logger = LoggerFactory.getLogger(MyFilter.class);

	private String[] excludedUrlArray;
	private int testNum;
		
	@Override
	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;

		/*
		 * 支持跨域访问
		 */
		// Access-Control-Allow-Origin: 指定授权访问的域  ，如"http://localhost:8080", "*"：允许所有访问域 
		response.addHeader("Access-Control-Allow-Origin", ALLOWORIGIN_CORS);
        // Access-Control-Allow-Methods: 授权请求的方法（GET, POST, PUT, DELETE，OPTIONS等)
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Max-Age", "1800");//30 min               
        
		
		boolean isExcludedPage = false;
		 
		//过滤器逻辑
		for (String excludedUrl : excludedUrlArray) {// 遍历例外url数组   

            // 判断当前URL是否与例外页面相同
            if(request.getServletPath().substring(1).equals(excludedUrl)){ // 从第2个字符开始取（把前面的/去掉）
                isExcludedPage = true;     
                break;     
            }     
        }
		
        if (isExcludedPage) {//在过滤url之外    
        	logger.info("不进行过滤器检查");
        	filterChain.doFilter(request, response);     
        }else {// 不在过滤url之外
        	testNum ++;
        	if(testNum%10 == 0) {
        		logger.error("过滤器每10次请求，就有一次故意未通过，测试使用实际使用时删除!");
        		sresponse.setContentType("application/json; charset=utf-8");
        		sresponse.setCharacterEncoding("UTF-8");
        		
        		String errLog = "{msg:'过滤器每10次请求，就有一次故意未通过，测试使用实际使用时删除!'}";
        		PrintWriter write = sresponse.getWriter();
        		write.println(errLog);		        		
        	}else {
        		filterChain.doFilter(request, response);    
        	}
        }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		testNum = 0;
		
		/**
		 * 获取例外页面数组
		 */
		String excludedUrls = filterConfig.getInitParameter("excludedUrls");
		if (null!=excludedUrls && excludedUrls.length()!=0) { // 例外页面不为空    
			excludedUrlArray = excludedUrls.split(String.valueOf(';'));     
        } 
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		Filter.super.destroy();
	}
}
