package com.cac.webWork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 
 * @ClassName: WebSocketConfig
 * @Description: 开启websocket
 * @author JinWH
 * @date 2020年3月19日
 *
 */
@Configuration
public class WebSocketConfig {
    @Bean  
    public ServerEndpointExporter serverEndpointExporter() {  
        return new ServerEndpointExporter();  
    }
}