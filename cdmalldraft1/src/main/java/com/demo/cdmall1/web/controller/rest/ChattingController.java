package com.demo.cdmall1.web.controller.rest;



import com.demo.cdmall1.domain.chatting.entity.ChattingModel;
import com.demo.cdmall1.domain.chatting.entity.ChattingUserStorage;
import com.demo.cdmall1.domain.chatting.entity.TempChattingHistory;
import com.demo.cdmall1.domain.chatting.service.ChattingService;
import com.demo.cdmall1.domain.member.entity.Member;
import com.demo.cdmall1.domain.member.service.MemberService;
import com.demo.cdmall1.web.dto.ChattingHistoryDto;
import com.demo.cdmall1.web.dto.MemberDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChattingController {	//	메세지 컨트롤러

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;	// SimpMessagingTemplate 활용해 상대방에게 메세지를 보낸다
    
    private final ChattingService chattingService;	
    private final MemberService memberService;
    
//    private final MessageRestService service;	// 추가 기능을 위한 임시 코드
    
    // 	메세지 보내기
    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, ChattingModel messagemodel) throws JsonProcessingException {	
        System.out.println("handling send message: " + messagemodel + " to: " + to);
        boolean isExists = ChattingUserStorage.getInstance().getUsers().contains(to);	//	현재 사용자가 UserStorage에 있는지 확인. *참고: UserStorage는 현재 채팅방에 등록된 유저객체를 저장하는 곳이다
        
        if (isExists) {	// 사용자가 채팅방에 등록 되어있다면
        	simpMessagingTemplate.convertAndSend("/topic/messages/" + to, messagemodel);	// SimpMessagingTemplate의 convertAndSend메소드를 이용해 메세지(messagemodel)를 상대망(to) 에게 보낸다

        	TempChattingHistory.getInstance().addToHistory(messagemodel);	//	보낸 메세지를 TempMessageHistory(임시 메세지기록 저장소)에 저장한다. 이유: 메세지를 여기에 임시로 저장했다가 한꺼번에 DB에 저장하기위해
        	System.out.println(TempChattingHistory.getInstance().getHistory());	//	임시 메세리 기록 보기

        }
    }
    
    //	채팅기록 DB에 저장하기
    @GetMapping(path="/message/add", produces=MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> saveMessage(@ModelAttribute @Valid ChattingHistoryDto.Save dto, BindingResult bindingResult ) {	
    	chattingService.addMessage(dto, TempChattingHistory.getInstance().getHistory());	//	'임시 채팅기록 저장소'에 있는 정보를 DB에 저장하기
    	TempChattingHistory.getInstance().removeAllHistory();	//	'임시 채팅기록 저장소'에 있는 정보들 다 지우기(리셋하는 용도)
    	return ResponseEntity.ok("DB에 추가 되었습니다.");
    }
    
    //	DB에 저장된 회원 정보 불러오기
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path="/message/memberinfo")
    public ResponseEntity<?> getMemberInfo(Principal principal){	//	DB에 저장된 회원 정보 불러오기
    	Member member = memberService.read(principal.getName());
    	
    	return ResponseEntity.ok(member);
    }
    
    //	DB에 저장된 유저의 권한정보 가져오기
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path="/message/authorityinfo")
    public ResponseEntity<?> getAuthorityInfo(@ModelAttribute @Valid MemberDto.FindByAuthority dto, BindingResult bindingResult){	
    	Member member = dto.toEntity();
    	return ResponseEntity.ok(memberService.readAuthority(member));
    }
    
    //	DB에 저잗된 채팅기록 리스트 불러오기
    @GetMapping(path="/chatting/list", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> chatList(@RequestParam(defaultValue="1") Integer pageno, String guestid){
    	return ResponseEntity.ok(chattingService.list(pageno, guestid));
    }
    
    //	메세지 기록 읽기
    @GetMapping(path="/chatting/readchathistory/{chathno}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readChatHistory(@PathVariable Integer chathno) {
		// String username = (principal==null)? null : principal.getName();
		return ResponseEntity.ok(chattingService.read(chathno));
	}
	 
	 
    
}
