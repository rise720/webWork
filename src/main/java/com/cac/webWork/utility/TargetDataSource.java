package com.cac.webWork.utility;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
* @ClassName: TargetDataSource
* @Description: 自定义注解
*
* @version: v1.0.0
* @author: JinWH
* @date: 2019年5月5日 下午5:58:23 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年5月5日     JinWH           v1.0.0               修改原因
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})	//作用于类、接口或者方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {
	String name();
}
