package com.demo.cdmall1.websocket;

import org.springframework.stereotype.*;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.*;

import lombok.*;

@RequiredArgsConstructor
@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {
	private final WSUserService service;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		service.add(session);
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		service.delete(session);
	}
}
