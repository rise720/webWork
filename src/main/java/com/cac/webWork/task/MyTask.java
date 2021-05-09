package com.cac.webWork.task;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cac.webWork.controller.MyController;

/**
 * 
* @ClassName: MyTask
* @Description: 定时任务
*
* cron表达式：* * * * * *（共6位，使用空格隔开，具体如下） cron表达式：*(秒0-59) *(分钟0-59) *(小时0-23) * (日期1-31) *(月份1-12或是JAN-DEC) 
*
* @version: v1.0.0
* @author: JinWH
* @date: 2019年5月9日 下午6:54:09 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年5月9日     JinWH           v1.0.0               修改原因
 */
@Component
public class MyTask {
	
	private static Logger logger = LoggerFactory.getLogger("MyTask");
	
	@Scheduled(cron="0 0/1 * * * *")
	public void getClock() {
		logger.info("定时任务每分钟报时--------");
	}
}
