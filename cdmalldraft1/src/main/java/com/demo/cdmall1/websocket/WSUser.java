package com.demo.cdmall1.websocket;

import java.io.*;
import java.util.*;

import org.springframework.web.socket.*;

import lombok.*;

// 사용자 아이디, WebSocketSession의 리스트
@Data
public class WSUser {
	private String username;
	private List<WebSocketSession> list = new Vector<>();
	// 필드 초기화하는 방법 3가지 : 인스턴스 초기화 -> static { } -> 생성자
	// 엔티티 초기화할 때 인스턴스 초기화를 사용하지 않고 @PrePersist를 사용한 이유? @Builder를 걸면 인스턴스 초기화가 동작하지 않는다
	
	
	// 생성자 -> 로그인하면 아이디와 웹소켓 세션을 이용해서 WSUser객체를 생성
	public WSUser(String username, WebSocketSession session) {
		this.username = username;
		this.list.add(session);
	}
	
	// 세션 추가 -> 로그인한 상태에서 새로운 탭을 열면 존재하는 WSUser에 세션을 추가
	public void add(WebSocketSession session) {
		this.list.add(session);
	}
	
	// 세션 삭제 -> 창을 닫으면 WSUser에서 세션을 삭제. 마지막 세션이라면 WSUser 자체를 삭제....-> WSUserService에서 관리
	public void delete(WebSocketSession session) {
		this.list.remove(session);
	}
	
	// 메시지 전송
	public void sendMessage(String msg) {
		TextMessage message = new TextMessage(msg);
		list.forEach(session->{
			try {
				session.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public int getSessionCount() {
		return list.size();
	}
}