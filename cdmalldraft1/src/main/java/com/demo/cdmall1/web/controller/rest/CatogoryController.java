package com.demo.cdmall1.web.controller.rest;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.domain.product.service.*;

import lombok.*;

@RestController
@RequiredArgsConstructor
public class CatogoryController {
	private final CategoryService service;
	
	// insert.html에서 대분류 선택하면 그 대분류 받아서 중분류 리턴, 중분류 선택하면 그 중분류 받아서 소분류 리턴
	@GetMapping(path="/categories", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Category>> read(@RequestParam(required=false) String categoryCode) {
		System.out.println(categoryCode);
		return ResponseEntity.ok(service.read(categoryCode));
	}
	
	// 카테고리 전체를 출력
	@GetMapping(path="/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Category>> readAll() {
		return ResponseEntity.ok(service.readAll());
	}
}
