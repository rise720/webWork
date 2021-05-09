package com.cac.webWork;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.cac.webWork.utility.DynamicDataSourceRegister;

@ServletComponentScan(basePackages = {"com.cac.webWork.filter"})
@MapperScan("com.cac.webWork.dao")
@Import(DynamicDataSourceRegister.class)
@EnableScheduling	//开启定时服务
//@EnableDubboConfig //开启dubbo功能. (dubbo provider服务可以使用或者不使用web容器)
@SpringBootApplication
/**
 * 
 * @ClassName: WebWorkApplication
 * @Description: 功能整合包括：
 * 						  webSocket redis mq
 * 						  shiro
 * @author JinWH
 * @date 2020年4月29日
 *
 */
public class WebWorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebWorkApplication.class, args);
	}
}