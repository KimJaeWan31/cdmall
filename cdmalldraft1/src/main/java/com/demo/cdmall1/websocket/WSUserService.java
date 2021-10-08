package com.demo.cdmall1.websocket;

import java.util.*;

import org.hibernate.internal.build.*;
import org.springframework.stereotype.*;
import org.springframework.web.socket.*;

import com.demo.cdmall1.web.dto.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

@Component
public class WSUserService {
	private List<WSUser> list = new Vector<>();
	
	// 새로운 세션이 만들어지면 -> list에서 WSUser를 찾아서 존재하지 않으면 WSUser 생성, 존재하면 세션만 추가 
	public void add(WebSocketSession session) {
		String loginId = session.getPrincipal().getName();
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).getUsername().equals(loginId)==true) {
				System.out.println("사용자 검색 성공");
				list.get(i).add(session);
				return;
			}
		}
		WSUser user = new WSUser(loginId, session);
		list.add(user);
	}
	
	public int delete(WebSocketSession session) {
		String loginId = session.getPrincipal().getName();
		// list에서 WSUser를 찾아서
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).getUsername().equals(loginId)) {
				// WSUser가 현재 세션을 하나만 가지고 있다면 -> 마지막 세션 -> WSUser 자체를 삭제
				if(list.get(i).getSessionCount()==1) {
					list.remove(i);
					return 1;
				}
				// 그렇치 않다면 세션만 삭제
				list.get(i).delete(session);
			}
		}
		// 못 찾았다면 오류 상황
		return -1;
	}
	
	public void sendMsg(String sender, String receiver, String title, String content) {
		WebSocketResponse response = new WebSocketResponse(sender, receiver, title, content);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			 json = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(json);
		for(WSUser user:list) {
			if(user.getUsername().equals(receiver)==true) {
				user.sendMessage(json);
				break;
			}
		}
	}
	
	public void sendAll(String sender, String title, String content) {
		WebSocketResponse response = new WebSocketResponse(sender, "모든 사용자", title, content);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		for(WSUser user:list)
			user.sendMessage(json);
	}
	
}







