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
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class QSCommentController {
	private final QSCommentService qsservice;
	private final RestTemplate template;
	
	@PostMapping(path="/qscomments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertQSComment(@Valid QSCommentDto.Write dto, BindingResult results, Principal principal) throws BindException {
		if(results.hasErrors())
			throw new BindException(results);
		
		// get방식으로 MemberController에게 프사이름을 요청
		// RestTemplate 요청의 항상 절대 주소....http://~
		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8081").path("/members/profile").queryParam("username", principal.getName()).build().toUri();
		String profile = template.getForObject(uri.toString(), String.class);
		
		// 댓글을 저장
		List<QSComment> qscomments = qsservice.write(dto, profile, principal.getName());
		
		// post방식으로 BoardController에게 댓글 수 업데이트를 요청
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("qbno", dto.getQbno()+"");
		Integer cnt = template.postForObject("http://localhost:8081/questionBoard/comments", params, Integer.class);

		// 댓글들 리턴
		return ResponseEntity.ok(qscomments);
		
	}
	
	// 댓글 삭제
	@DeleteMapping(path="/qscomments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteQSComment(@Valid QSCommentDto.Delete dto, BindingResult results, Principal principal) throws BindException {
		
		if(results.hasErrors())
			throw new BindException(results);
		
		// 댓글 삭제
		List<QSComment> qscomments = qsservice.delete(dto, principal.getName());
		
		// post방식으로 BoardController에게 댓글 수 업데이트를 요청
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("qbno", dto.getQbno()+"");
		System.out.println("111111111");
		Integer cnt = template.postForObject("http://localhost:8081/questionBoard/comments", params, Integer.class);

		
		// 댓글들 리턴
		
		return ResponseEntity.ok(qscomments);
		
	}
}
