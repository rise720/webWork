package com.cac.webWork.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
* @ClassName: RabbitMqTopicExchangeConfig
* @Description: direct直连模式的交换机配置,包括一个direct交换机，两个队列，三根网线binding
*
* @author: JinWH
* @date: 2019年7月1日 下午6:09:02 

 */
//@Configuration
public class RabbitMqTopicExchangeConfig {
	
	@Bean
	public TopicExchange TopicExchange() {
	    return new TopicExchange("myTopicExchange");
	}
	
	@Bean
    public Queue queue1() {
	   Queue queue = new Queue("myQueue1");
       return queue;
    }
	
	@Bean
	public Queue queue2() {
	   Queue queue = new Queue("myQueue2");
	   return queue;
	}
    
    //3个binding将交换机和相应队列连起来
    @Bean
    public Binding bindingorange() {
        return BindingBuilder.bind(queue1()).to(TopicExchange()).with("*.orange.#");
    }

    @Bean
    public Binding bindingblack() {
    	return BindingBuilder.bind(queue2()).to(TopicExchange()).with("*.black.#");     
    }

    @Bean
    public Binding bindinggreen() {
    	return BindingBuilder.bind(queue2()).to(TopicExchange()).with("*.green.#");
    }
}