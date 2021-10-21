package com.demo.cdmall1.web.controller.rest;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

import javax.servlet.http.*;
import javax.validation.*;

import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.domain.product.service.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;
 
@RequiredArgsConstructor
@RestController
public class ProductController {
	private final ProductService service;
	// 이미지 첨부파일 보기
	@GetMapping(path={"/products/image", "/temp/image"}, produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> showImage(@RequestParam String imagename) throws IOException {
		File file = new File(ZmallConstant.IMAGE_FOLDER + imagename);
		System.out.println(file);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(ZmallUtil.getMediaType(imagename));
		headers.add("Content-Disposition", "inline;filename="  + imagename);
		try {
			return ResponseEntity.ok().headers(headers).body(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	@PostMapping(path="/products/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insert(@Valid ProductDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
		
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		
		Product product= service.insert(dto, principal.getName());
		URI uri = UriComponentsBuilder.newInstance().path("/product/read").queryParam("pno", product.getPno()).build().toUri();
		return ResponseEntity.created(uri).body(product);
		
//		return ResponseEntity.ok(service.insert(dto));
	}
	
	
	@PostMapping(path="/shop/searchAll", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> search(@RequestParam(defaultValue = "1") Integer pageno, HttpSession session){
		String word = session.getAttribute("word").toString();
		
		System.out.println(word);
		
		URI uri = UriComponentsBuilder.newInstance().path("/shop/search").queryParam("word", word).build().toUri();
		Map<String, Object> product = service.readSearchAll(pageno, word);
		return ResponseEntity.created(uri).body(product);
	}
	
	
	
	@GetMapping(path="/products/{pno}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> read(@PathVariable Integer pno) {
		return ResponseEntity.ok(service.read(pno));
	}
	
	@GetMapping(path="/products/all", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno, String writer) {
		return ResponseEntity.ok(service.list(pageno, writer));
	}
	
	@GetMapping("/product/good_or_bad")
	public ResponseEntity<?> GoodOrBadCnt(@RequestParam Integer pno, @RequestParam Integer state) {
		Integer cnt = service.goodOrBad(pno, state);
		return ResponseEntity.ok(cnt);
	}
	
	// 재고 확인
	@GetMapping(path="/products/stock", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkStock(Integer pno, Integer count) {
		return ResponseEntity.ok(service.checkStock(pno, count));
	}
		
	@PostMapping("/product/save_url")
	public ResponseEntity<?> Continue(@RequestParam String url){
		service.continueShopping(url);
		return ResponseEntity.ok(null);
	}
}
