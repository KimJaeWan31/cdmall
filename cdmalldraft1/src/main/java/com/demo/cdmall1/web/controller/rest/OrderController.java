package com.demo.cdmall1.web.controller.rest;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.cdmall1.domain.member.entity.Address;
import com.demo.cdmall1.domain.member.service.AddressService;
import com.demo.cdmall1.domain.order.entity.Order;
import com.demo.cdmall1.domain.order.entity.OrderItem;
import com.demo.cdmall1.domain.order.service.OrderService;
import com.demo.cdmall1.web.dto.OrderDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {
	
	private final OrderService service;
	// 장바구니에서 선택하고 주문 버튼 누르면 선택한 장바구니 물품을 주문 목록으로 이동
	@PostMapping("/orders/cart")
	public ResponseEntity<Void> orderCart(@RequestBody List<Integer> pnos) {
		System.out.println(pnos);
		service.orderCart(pnos);
		URI uri = UriComponentsBuilder.newInstance().path("/order/read").queryParam("select", "cart").build().toUri();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", uri.toString());
		return ResponseEntity.ok().headers(responseHeaders).body(null);
	}
	
	// 상품 화면에서 바로 주문을 선택
	@PostMapping("/orders/product")
	public ResponseEntity<Void> orderProduct(@RequestBody OrderDto.OrderProduct dto) {
		service.orderProduct(dto);
		URI uri = UriComponentsBuilder.newInstance().path("/order/read").queryParam("select", "product").build().toUri();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", uri.toString());
		return ResponseEntity.ok().headers(responseHeaders).body(null);
	}
	
	// 장바구니에서 선택 또는 상품에서 선택해 결제하려는 목록 보여주기
	@GetMapping(path="/orders", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderItem>> getOrders(String select) {
		return ResponseEntity.ok(service.getOrders(select));
	}

	@PostMapping("/orders/payment")
	public ResponseEntity<Void> payment(String select, Integer addressNo, Principal principal) {
		System.out.println(select);
		service.payment(select, addressNo, principal.getName());
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/orders/payment")
	public ResponseEntity<List<Order>> readAll(Principal principal) {
		return ResponseEntity.ok(service.readAll(principal.getName()));
	}
	
	// /review/available_list에서 요청하는 리뷰가능한 주문 목록
	@GetMapping("/orders/review_available")
	public ResponseEntity<List<OrderDto.ReviewAvailable>> reviewAvailableList(Principal principal) { 
		return ResponseEntity.ok(service.reviewAvailableList(principal.getName()));
	}
	
	@GetMapping("/orders/order_item_no")
	public ResponseEntity<OrderDto.ReviewAvailable> getOrderItem(Integer orderNo, Integer orderItemNo, Principal principal) { 
		return ResponseEntity.ok(service.getOrderItem(orderNo, orderItemNo, principal.getName()));
	}
}
