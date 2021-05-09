package com.cac.webWork.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MqSender{
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void publishMqMessage(String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		rabbitTemplate.convertAndSend("myDirectExchange", "green", message + sdf.format(new Date()));
//		rabbitTemplate.convertAndSend("myTopicExchange", "dddd.orange.1", message + sdf.format(new Date()));
//		rabbitTemplate.convertAndSend("direct", "green", message);
	}
}
