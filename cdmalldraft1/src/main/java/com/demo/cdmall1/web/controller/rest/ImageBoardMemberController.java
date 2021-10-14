package com.demo.cdmall1.web.controller.rest;

import java.net.*;
import java.security.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.board.service.*;
import com.demo.cdmall1.domain.imageboard.service.*;
import com.demo.cdmall1.domain.member.service.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class ImageBoardMemberController {
	private final ImageBoardMemberService imageMemberService;
	private final RestTemplate restTemplate;
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/imageBoard_member/good_or_bad")
	public ResponseEntity<?> goodOrBad(Integer ibno, boolean isGood, Principal principal) {
		GoodOrBad state = imageMemberService.goodOrBad(ibno, isGood, principal.getName());
		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8081").path("/imageBoard/good_or_bad")
				.queryParam("ibno", ibno+"").queryParam("state", state.ordinal()+"").build().toUri();
		Integer cnt = restTemplate.getForObject(uri.toString(), Integer.class);
		return ResponseEntity.ok(cnt);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/imageBoard_member/is_exist")
	public ResponseEntity<?> isExist(Integer ibno, String loginId) {
		boolean check = imageMemberService.isExist(ibno, loginId); 
		return ResponseEntity.ok(check);                                                                                                                                                                                                                                                                                                     
	}
	
}
