package com.demo.cdmall1.web.controller.rest;

import java.net.*;
import java.security.*;
import java.util.*;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.memo.service.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.websocket.*;

import lombok.*;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
public class MemoController {
	private final MemoService service;
	private final WSUserService wsService;
	
	// 요청자 : sender
	// 새 메모를 작성하면 수신자 화면에 메모 도착 메시지 출력하고 읽지 않은 메모의 개수를 증가시킨다
	@PostMapping("/memos/new")
	public ResponseEntity<?> write(@Valid MemoDto.Write dto, BindingResult bindingResult, Principal principal) {

		service.write(dto, principal.getName());

		wsService.sendMsg(principal.getName(), dto.getReceiver(), "메모 도착", dto.getTitle());

		wsService.sendMsg(principal.getName(), dto.getReceiver(), "읽지않은 메모", service.unreadMemo(dto.getReceiver())+"");

		URI uri = UriComponentsBuilder.newInstance().path("/memo/send").build().toUri();
		return ResponseEntity.created(uri).body(null);
	}
	
	// 요청자 : receiver
	// 클라에서 화면이동을 하면 ajax로 읽지 않은 메모의 개수를 요청 -> 처리
	@GetMapping("/memos/unread")
	public ResponseEntity<?> unreadMemo(Principal principal) {
		return ResponseEntity.ok(service.unreadMemo(principal.getName()));
	}
	
	@GetMapping("/memos/send")	
	public ResponseEntity<?> sendList(Principal principal) {
		return ResponseEntity.ok(service.sendList(principal.getName()));
	}
	
	@GetMapping("/memos/receive")	
	public ResponseEntity<?> receiveList(Principal principal) {
		return ResponseEntity.ok(service.receiveList(principal.getName()));
	}
	
	@GetMapping("/memos/{mno}")
	public ResponseEntity<?> read(@PathVariable Integer mno) {
		return ResponseEntity.ok(service.read(mno));
	}
	
	@PatchMapping("/memos/disabled_by_sender")
	public ResponseEntity<?> disabledBySender(@RequestParam List<Integer> mnos) {
		service.disabledBySender(mnos);
		return ResponseEntity.ok(null);
	}
		
	@PatchMapping("/memo/disabled_by_receiver")
	public ResponseEntity<?> disabledByReceiver(List<Integer> mnos) {
		service.disabledByReceiver(mnos);
		return ResponseEntity.ok(null);
	}
	

}
