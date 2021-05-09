package com.cac.webWork.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableRabbit
public class RabbitMqConfig {
	
	/**
	Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输, 
	Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。 
	Queue:消息的载体,每个消息都会被投到一个或多个队列。 
	Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来. 
	Routing Key:路由关键字,exchange根据这个关键字进行消息投递。 
	vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。 
	Producer:消息生产者,就是投递消息的程序. 
	Consumer:消息消费者,就是接受消息的程序. 
	Channel:消息通道,在客户端的每个连接里,可建立多个channel.
	*/
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${spring.rabbitmq.host}")
	private String host;
	
	@Value("${spring.rabbitmq.port}")
	private int port;
	
	@Value("${spring.rabbitmq.username}")
	private String username;
	
	@Value("${spring.rabbitmq.password}")
	private String password;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		//连接池，默认连接数只有1个。
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
		connectionFactory.setChannelCacheSize(1);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost("vitualHost");
		connectionFactory.setPublisherConfirms(true);
		return connectionFactory;
	}
	
//	@Bean
//	public AmqpAdmin amqpAdmin() {
//		return new RabbitAdmin(connectionFactory());
//	}
	
	@Bean
	public RabbitTemplate RabbitTemplate() {
		return new RabbitTemplate(connectionFactory());
	}
	
	//配置消费者监听的容器
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrentConsumers(3);
		factory.setMaxConcurrentConsumers(10);
		//factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式手工确认
		return factory;
	}
}
