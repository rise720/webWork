package com.cac.webWork.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: PermissionAnnotation
 * @Description: 权限判断注解
 * @author JinWH
 * @date 2020年6月12日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionAnnotation {
	String permissionName();
}