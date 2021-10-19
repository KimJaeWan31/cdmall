package com.demo.cdmall1.web.controller.rest;

import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.demo.cdmall1.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

//import com.demo.cdmall1.domain.board.entity.Board;

@RequiredArgsConstructor
@Controller
public class AdminController {
	private final MemberService service;
	//private List<Board> list = new ArrayList<>();

	@GetMapping(path="/api/admin/data1", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getData1() {
		return ResponseEntity.ok(service.readMemberCount());
	}
	
	@GetMapping(path="/api/admin/data2", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getData2() {
		return ResponseEntity.ok(service.readBoardCount());
	}
	
}
