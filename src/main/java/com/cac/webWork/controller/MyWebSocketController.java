package com.cac.webWork.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 
 * @ClassName: MyWebSocketController
 * @Description: ws协议的controller
 * @author JinWH
 * @date 2020年3月19日
 *
 */
@ServerEndpoint("/myWebSocket/{userId}")
@Component
public class MyWebSocketController {
	private static Logger logger = LoggerFactory.getLogger(MyWebSocketController.class);
	
	//记录当前在线连接数
	private static AtomicInteger onlineCount = new AtomicInteger(0);
	//存放每个客户端对应的MyWebSocket对象
	private static ConcurrentHashMap<String, MyWebSocketController> webSocketMap = new ConcurrentHashMap<String, MyWebSocketController>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	//接收userId
    private String userId = "";
    
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        logger.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        logger.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.hasText(message)){
            try {
                //解析发送的报文
//            	ObjectMapper objectMapper = new ObjectMapper();
//            	JsonNode jsonNode = objectMapper.readValue(message, JsonNode.class);
                //追加发送人(防止串改)
                //传送给对应toUserId用户的websocket
                if(StringUtils.hasText(userId)&&webSocketMap.containsKey(userId)){
                    webSocketMap.get(userId).sendMessage(message);
                }else{
                    logger.error("请求的userId:"+userId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        logger.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.hasText(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            logger.error("用户"+userId+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.intValue();
    }

    public static synchronized void addOnlineCount() {
        MyWebSocketController.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
    	MyWebSocketController.onlineCount.getAndDecrement();
    }
}