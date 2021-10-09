package com.demo.cdmall1.web.controller.rest;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.demo.cdmall1.domain.chatting.entity.ChattingUserStorage;

@RestController
@CrossOrigin
public class ChattingUsersController {

	//	채팅 유저 리스트에 현재유저를 등록
    @GetMapping("/registration/{userName}")
    public ResponseEntity<Void> register(@PathVariable String userName) {
        System.out.println("handling register user request: " + userName);
        try {
            ChattingUserStorage.getInstance().addUser(userName);	// 유저리스트 저장소(싱글톤)에 현재 유저 추가
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 모든 유저 조회
    @GetMapping("/fetchAllUsers")
    public Set<String> fetchAll() {
        return ChattingUserStorage.getInstance().getUsers();
    }
    
    // 현재유저 리스트에서 비등록
    @GetMapping("/deregistraion/{userName}")
    public void deregister(@PathVariable String userName) {
    	System.out.println("handling deregister user request: " + userName);
    	ChattingUserStorage.getInstance().removeUser(userName);	//	유저리스트 저장소(싱글톤)에 현재 유저 지우기
    	System.out.println("successfully removed from the user storage: " + userName);
    }
    
}
