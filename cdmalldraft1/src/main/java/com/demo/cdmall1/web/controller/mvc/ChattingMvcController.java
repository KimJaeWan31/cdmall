package com.demo.cdmall1.web.controller.mvc;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChattingMvcController {
	@GetMapping("/chatting/chatmain")
	public void chattingMain() {	//	채팅 메인화면 
							//	메인화면 진입시 사용자의 권한에 따라 보이는 화면이 다르다. 예) 호스트(관리자, 상담자) / 게스트
	}
	
	@GetMapping("/chatting/chatlist")	// 저장된 채팅기록 리스트 화면
	public void chattingList() {
		
	}
	
	@GetMapping("/chatting/chatread")	//	저장된 채팅기록 읽기 화면
	public void chattingHistory() {

		
	}
  }
