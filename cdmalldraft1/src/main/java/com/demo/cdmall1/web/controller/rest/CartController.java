package com.demo.cdmall1.web.controller.rest;

import java.util.List;

import javax.servlet.http.*;

import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.demo.cdmall1.domain.order.entity.*;
import com.demo.cdmall1.domain.order.service.CartService;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class CartController {
	private final CartService service;	
	
	@PostMapping("/carts")
	public ResponseEntity<Void> add(@RequestBody Cart cart) {
		service.add(cart);
		return ResponseEntity.ok(null);
	}
	
	@GetMapping(path="/carts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cart>> read() {
		return ResponseEntity.ok(service.read());
	}

	@PatchMapping(path="/carts/increase", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cart>> increase(Integer pno) {
		return ResponseEntity.ok(service.increase(pno));
	}
	
	@PatchMapping(path="/carts/decrease", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cart>> decrease(Integer pno) {
		return ResponseEntity.ok(service.decrease(pno));
	}
	
	@DeleteMapping(path="/carts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cart>> delete(@RequestBody List<Integer> dtos) {
		return ResponseEntity.ok(service.delete(dtos));
	}
	
	@GetMapping("/carts/get_url")
	public ResponseEntity<?> getUrl(){
		
		return ResponseEntity.ok(service.getUrl()); 
	}
	
	
	
	
	// 카트정보를 읽어온다
	// 상품만 선택하는 경우는 페이지를 로딩할 때 임시장바구니를 생성. 옵션을 선택하는 경우는 페이지를 로딩할 때 임시 장바구니를 읽어오겠다
	/*
	 * @GetMapping("/temp_cart") public ResponseEntity<?> getCart(Integer pno,
	 * HttpSession session) { Cart cart = service.getCart(pno, session); return
	 * ResponseEntity.ok(cart); }
	 * 
	 * // optionNo가 null이면 상품 선택. 그렇지 않은 경우 옵션 선택
	 * 
	 * @PostMapping("/temp_cart") public ResponseEntity<?> add(Integer pno, Integer
	 * optionNo, HttpSession session) { Cart cart = service.add(pno, optionNo,
	 * session); return ResponseEntity.ok(cart); }
	 * 
	 * @PatchMapping("/temp_cart/increase") public ResponseEntity<?>
	 * increase(Integer pno, HttpSession session) { Cart cart =
	 * service.increase(pno, session); return ResponseEntity.ok(cart); }
	 * 
	 * @PatchMapping("/temp_cart/decrease") public ResponseEntity<?>
	 * decrease(Integer pno, HttpSession session) { Cart cart =
	 * service.decrease(pno, session); return ResponseEntity.ok(cart); }
	 * 
	 * @PatchMapping("/temp_cart/option/increase") public ResponseEntity<?>
	 * increaseOption(Integer pno, Integer optionNo, HttpSession session) { Cart
	 * cart = service.increaseOption(pno, optionNo, session); return
	 * ResponseEntity.ok(cart); }
	 * 
	 * @PatchMapping("/temp_cart/option/decrease") public ResponseEntity<?>
	 * decreaseOption(Integer pno, Integer optionNo, HttpSession session) { Cart
	 * cart = service.decreaseOption(pno, optionNo, session); return
	 * ResponseEntity.ok(cart); }
	 * 
	 * @DeleteMapping("/temp_cart/option") public ResponseEntity<?>
	 * deleteOption(Integer pno, Integer optionNo, HttpSession session) { Cart cart
	 * = service.deleteOption(pno, optionNo, session); return
	 * ResponseEntity.ok(cart); }
	 */
}
