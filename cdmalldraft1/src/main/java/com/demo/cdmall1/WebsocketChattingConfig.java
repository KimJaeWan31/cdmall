package com.demo.cdmall1;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketChattingConfig implements WebSocketMessageBrokerConfigurer {
    
	//	Stomp end point를 등록함으로써 실시간으로 등록할 자신의 주소를 등록한다
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {	
        registry.addEndpoint("/chat").withSockJS();

    }

	//	상대방의 주소를 등록함으로써 자신의 주소와 상대방의 주소를 연결한다
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {	
        registry.setApplicationDestinationPrefixes("/app").enableSimpleBroker("/topic");
    }
}
