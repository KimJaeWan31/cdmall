package com.demo.cdmall1.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.demo.cdmall1.domain.product.entity.ProductFail;

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
