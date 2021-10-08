package com.demo.cdmall1.web.controller.rest;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import com.demo.cdmall1.domain.product.service.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class ProductController {
	private final ProductService service;
	
	@PostMapping(path="/products/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insert(@Valid ProductDto.Write dto, BindingResult bindingResult) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		return ResponseEntity.ok(service.insert(dto));
	}
	
	@GetMapping("/products/{pno}")
	public ResponseEntity<?> read(@PathVariable Integer pno) {
		return ResponseEntity.ok(service.read(pno));
	}
}
