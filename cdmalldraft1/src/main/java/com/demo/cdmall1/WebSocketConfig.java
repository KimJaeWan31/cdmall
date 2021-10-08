package com.demo.cdmall1;

import org.springframework.context.annotation.*;
import org.springframework.web.socket.config.annotation.*;

import com.demo.cdmall1.websocket.*;

import lombok.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	private final MessageWebSocketHandler handler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler, "/web/socket").setAllowedOrigins("*");
	}
}	
