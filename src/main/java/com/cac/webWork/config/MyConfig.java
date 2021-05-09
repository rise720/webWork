package com.cac.webWork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 
* @ClassName: MyConfig
* @Description: properties配置文件POJO
*
* @author: JinWH
* @date: 2019年4月10日 下午6:08:51 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年4月10日     JinWH           v1.0.0               修改原因
 */
@Component
@ConfigurationProperties(prefix = "")	//配置文件自动封装成实体类
@PropertySource("classpath:/config/myConfig.properties")	//指定配置文件
public class MyConfig {
	private T1class t1;
	private T1class t2;
	@Value("${system.filter}")
	private String filterFlag;
	public T1class getT1() {
		return t1;
	}
	public void setT1(T1class t1) {
		this.t1 = t1;
	}
	
	public String getT1T2() {
		return t1.getF1();
	}
	public T1class getT2() {
		return t2;
	}
	public void setT2(T1class t2) {
		this.t2 = t2;
	}
	public String getFilterFlag() {
		return filterFlag;
	}
	public void setFilterFlag(String filterFlag) {
		this.filterFlag = filterFlag;
	}
}

class T1class{
	private String f1;
	private String f2;
	public String getF1() {
		return f1;
	}
	public void setF1(String f1) {
		this.f1 = f1;
	}
	public String getF2() {
		return f2;
	}
	public void setF2(String f2) {
		this.f2 = f2;
	}	
}
