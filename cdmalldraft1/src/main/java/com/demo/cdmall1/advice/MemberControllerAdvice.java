package com.demo.cdmall1.advice;

import java.net.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.member.entity.*;
import com.demo.cdmall1.domain.member.entity.MemberFail.*;
import com.demo.cdmall1.web.dto.*;

@ControllerAdvice
public class MemberControllerAdvice {
	@ExceptionHandler(MemberFail.JoinCheckFailException.class)
	public ResponseEntity<?> joinCheckFailException(RedirectAttributes ra) {
		URI uri = UriComponentsBuilder.newInstance().path("/").build().toUri();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		ra.addFlashAttribute("msg", "체크코드가 만료되었습니다. 재가입이 필요합니다");
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
	
	@ExceptionHandler(MemberNotFoundException.class)
	public ResponseEntity<?> memberNotFoundException() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("사용자를 찾을 수 없습니다");
	}
	
	@ExceptionHandler(PasswordCheckException.class)
	public ResponseEntity<?> passwordCheckException() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("비밀번호를 확인하지 못 했습니다");
	}
	
	
	@ExceptionHandler(MissingServletRequestParameterException.class) 
	public ResponseEntity<?> missingServletRequestParameterException(MissingServletRequestParameterException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getLocalizedMessage());
	}
	
	@ExceptionHandler(BindException.class) 
	public ResponseEntity<?> bindException(BindingResult e) {
		//e.getAllErrors().forEach(m->System.out.println(m));
		List<BindExceptionMessage> list = new ArrayList<>();
		e.getAllErrors().forEach(error->{
			FieldError fe = (FieldError)error;
			list.add(new BindExceptionMessage(fe.getField(), fe.getDefaultMessage()));
		});
		list.forEach(m->System.out.println(m));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(list);
	}
}
