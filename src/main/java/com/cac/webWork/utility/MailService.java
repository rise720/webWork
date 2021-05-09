package com.cac.webWork.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 
* @ClassName: MailService
* @Description: 邮件服务
*
* @version: v1.0.0
* @author: JinWH
* @date: 2019年5月9日 下午7:40:55 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年5月9日     JinWH           v1.0.0               修改原因
 */
@Component
public class MailService{

	private static Logger logger = LoggerFactory.getLogger(MailService.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendSimpleMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("myMail");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		
		try {
			mailSender.send(message);
			logger.info("简单邮件已发送！");
		}catch(Exception e) {
			logger.error("简单邮件发送失败！"+ e.getMessage());
		}
	}
}
