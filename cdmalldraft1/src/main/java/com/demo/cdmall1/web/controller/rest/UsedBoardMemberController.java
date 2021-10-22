package com.demo.cdmall1.web.controller.rest;

import java.net.*;
import java.security.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.usedboard.entity.*;
import com.demo.cdmall1.domain.usedboard.service.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class UsedBoardMemberController {
	private final UsedBoardMemberService service;
	private final RestTemplate restTemplate;
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/usedBoard_member/warn")
	public ResponseEntity<?> warn(Integer ubno, Principal principal){
		WarnCheck state = service.warncheck(ubno, principal.getName());
		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8081").path("/usedBoard/warn")
				.queryParam("ubno", ubno+"").queryParam("state", state.ordinal()+"").build().toUri();
		Integer cnt = restTemplate.getForObject(uri.toString(), Integer.class);
		return ResponseEntity.ok(cnt);
	}
}
