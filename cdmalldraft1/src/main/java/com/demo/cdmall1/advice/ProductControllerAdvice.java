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
import com.demo.cdmall1.domain.product.entity.ProductFail;
import com.demo.cdmall1.web.dto.*;

@ControllerAdvice
public class ProductControllerAdvice {
	@ExceptionHandler(ProductFail.ProductNotFoundException.class)
	public ResponseEntity<String> productExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("상품을 찾을 수 없습니다");
	}
	
	@ExceptionHandler(ProductFail.OutOfStockException.class)
	public ResponseEntity<String> outOfStockExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("더 이상 구입할 수 없습니다");
	}
}
