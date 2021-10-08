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
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class NBCommentController {
	private final NBCommentService nbservice;
	private final RestTemplate template;
	
	@PostMapping(path="/nbcomments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertNBComment(@Valid NBCommentDto.Write dto, BindingResult results, Principal principal) throws BindException {
		if(results.hasErrors())
			throw new BindException(results);
		
		// get방식으로 MemberController에게 프사이름을 요청
		// RestTemplate 요청의 항상 절대 주소....http://~
		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8089").path("/members/profile").queryParam("username", principal.getName()).build().toUri();
		String profile = template.getForObject(uri.toString(), String.class);
		
		// 댓글을 저장
		List<NBComment> nbcomments = nbservice.write(dto, profile, principal.getName());
		
		// post방식으로 BoardController에게 댓글 수 업데이트를 요청
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("nbno", dto.getNbno()+"");
		Integer cnt = template.postForObject("http://localhost:8089/noticeBoard/nbcomments", params, Integer.class);
		System.out.println(cnt);
		System.out.println("+++++++++++++++++++++=======================");
		System.out.println(nbcomments);
		// 댓글들 리턴
		return ResponseEntity.ok(nbcomments);
		
	}
	
	// 댓글 삭제
	@DeleteMapping(path="/nbcomments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteNBComment(@Valid NBCommentDto.Delete dto, BindingResult results, Principal principal) throws BindException {
		if(results.hasErrors())
			throw new BindException(results);
		
		// 댓글 삭제
		List<NBComment> nbcomments = nbservice.delete(dto, principal.getName());
		
		// post방식으로 BoardController에게 댓글 수 업데이트를 요청
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("nbno", dto.getNbno()+"");
		Integer cnt = template.postForObject("http://localhost:8089/noticeBoard/nbcomments", params, Integer.class);
		System.out.println(cnt);
		
		// 댓글들 리턴
		return ResponseEntity.ok(nbcomments);
		
	}
}
