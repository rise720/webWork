package com.cac.webWork.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP.Channel;

//@Component
//@RabbitListener(queues = "directqueue2")
public class MqReceiver {
	private static Logger logger = LoggerFactory.getLogger(MqReceiver.class);
	
	@RabbitListener(queues = "myQueue1")
	public void receiveMqMessage1(String message) {
		logger.info("directqueue1收到message：" + message);
	}
	
	@RabbitListener(queues = "myQueue2")
	public void receiveMqMessage2(String message) {
		logger.info("directqueue2收到message：" + message);
	}
//	@RabbitHandler
//	public void process (String hello) {
//		logger.info("myQueue2收到message:" + hello);
//	}
}