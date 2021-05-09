package com.cac.webWork.aspect;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cac.webWork.annotation.PermissionAnnotation;
import com.cac.webWork.model.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Aspect
public class PermissionAspect{
	
	Logger logger = LoggerFactory.getLogger(PermissionAspect.class);
	
//	@Pointcut("@annotation(com.cac.webWork.annotation.PermissionAnnotation)")
//	private void permisson() {
//		logger.info("进入切面");
//	}
	
//	@Before("@annotation(permissionAnnotation)")
//	public void aspectBefore(JoinPoint joinPointPermissionAnnotation, PermissionAnnotation permissionAnnotation){
//		
//		logger.info("进入切面,before" + permissionAnnotation.permissionName());
//		
//	}
	
	/**
	 * 
	 * @Title: aspectAround
	 * @Description: 切面环绕着增强
	 * 				注解：@annotation(pAparam) ，建议易写法。既表示切面的触发时机，也可获得注解的参数
	 * @param @param pjp
	 * @param @param permissionAnnotation
	 * @param @return
	 * @param @throws Throwable 参数
	 * @return Object 返回类型
	 * @throws
	 */
	@Around("@annotation(pAparam)")
	public Object aspectAround(ProceedingJoinPoint pjp, PermissionAnnotation pAparam) throws Throwable {
		String[] permissDim = pAparam.permissionName().split(":");
		if(permissDim == null || permissDim.length  != 2) {
			return Result.fail(9, "权限获取失败！");
		}
		Subject subject = SecurityUtils.getSubject();
		return pjp.proceed();
	}
	
}
