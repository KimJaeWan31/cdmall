package com.demo.cdmall1.web.controller.rest;

import java.net.*;

import java.security.*;
import java.util.*;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.noticeboard.entity.*;
import com.demo.cdmall1.domain.noticeboard.service.*;
import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.domain.questionboard.service.*;
import com.demo.cdmall1.domain.usedboard.entity.UsedComment;
import com.demo.cdmall1.domain.usedboard.entity.UsedCommentDto;
import com.demo.cdmall1.domain.usedboard.service.UsedCommentService;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class UsedCommentController {
	private final UsedCommentService usedservice;
	private final RestTemplate template;
	
	@PostMapping(path="/usedcomments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertUsedComment(@Valid UsedCommentDto.Write dto, BindingResult results, Principal principal) throws BindException {
		if(results.hasErrors())
			throw new BindException(results);
		
		// get방식으로 MemberController에게 프사이름을 요청
		// RestTemplate 요청의 항상 절대 주소....http://~
		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8081").path("/members/profile").queryParam("username", principal.getName()).build().toUri();
		String profile = template.getForObject(uri.toString(), String.class);
		
		// 댓글을 저장
		List<UsedComment> usedcomments = usedservice.write(dto, profile, principal.getName());
		
		// post방식으로 BoardController에게 댓글 수 업데이트를 요청
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("ubno", dto.getUbno()+"");
		Integer cnt = template.postForObject("http://localhost:8081/usedBoard/comments", params, Integer.class);

		// 댓글들 리턴
		return ResponseEntity.ok(usedcomments);
		
	}
	
	// 댓글 삭제
	@DeleteMapping(path="/usedcomments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUsedComment(@Valid UsedCommentDto.Delete dto, BindingResult results, Principal principal) throws BindException {
		
		if(results.hasErrors())
			throw new BindException(results);
		
		// 댓글 삭제
		List<UsedComment> usedcomments = usedservice.delete(dto, principal.getName());
		
		// post방식으로 BoardController에게 댓글 수 업데이트를 요청
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("ubno", dto.getUbno()+"");
		System.out.println("111111111");
		Integer cnt = template.postForObject("http://localhost:8081/usedBoard/comments", params, Integer.class);

		
		// 댓글들 리턴
		
		return ResponseEntity.ok(usedcomments);
		
	}
}
