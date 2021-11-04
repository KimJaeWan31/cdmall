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
	@GetMapping(path={"/products/image", "/ptemp/image"}, produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> showImage(@RequestParam String imagename, HttpServletRequest req) throws IOException {
		String command = req.getRequestURI().substring(1, req.getRequestURI().lastIndexOf("/"));
		File file = new File(ZmallConstant.TEMP_FOLDER + imagename);
		if(command.equals("products")) {
			file = new File(ZmallConstant.IMAGE_FOLDER + imagename);
		}
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
	public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno, String manufacturer) {
		return ResponseEntity.ok(service.list(pageno, manufacturer));
	}
	
	@GetMapping(path="/products/allByCateg", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listByCateg(@RequestParam(defaultValue="1") Integer pageno, String categCode) {
		return ResponseEntity.ok(service.listByCateg(pageno, categCode));
	}
	
	@GetMapping(path="/products/allByRootCateg", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listByRootCateg(@RequestParam(defaultValue="1") Integer pageno, String categCode) {
		return ResponseEntity.ok(service.listByRootCateg(pageno, categCode));
	}
	
	@GetMapping(path="/products/allByMultiCateg", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listByMultiCateg(@Valid ProductDto.ListByMultiCateg dto, BindingResult bindingResult) {
		return ResponseEntity.ok(service.listByMultiCateg(dto.getPageno(), dto.getCategList()));
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
	
	@GetMapping(path="/products/wishList", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> withList(@RequestParam(defaultValue="1") Integer pageno, Principal principal){
		String username = principal.getName();
		System.out.println("1234Username = " + username);
		return ResponseEntity.ok(service.wishList(pageno, username));
	}
	
	@DeleteMapping(path="/products/wish_delete")	
	public ResponseEntity<?> productWishDelete(@RequestBody List<Integer> dtos){
		service.wishDelete(dtos);
		return ResponseEntity.ok(null); 
	};
	
	@DeleteMapping(path="/products/currentWishDelete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> productCurrentWishDelete(@RequestParam Integer pno) {
		service.productCurrentWishDelete(pno);
		return ResponseEntity.ok(null); 
	}
	
	@GetMapping(path="/products/newList", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> newList(@RequestParam(defaultValue="1") Integer pageno, String manufacturer) {
		return ResponseEntity.ok(service.listByCreateTime(pageno, manufacturer));
	}
	
	@GetMapping(path="/products/bestList", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bestList(@RequestParam(defaultValue="1") Integer pageno, String manufacturer) {
		return ResponseEntity.ok(service.listBySalesAmount(pageno, manufacturer));
	}
}
