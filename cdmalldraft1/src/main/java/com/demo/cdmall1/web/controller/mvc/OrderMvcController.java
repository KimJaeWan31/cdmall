package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderMvcController {

	@Secured("ROLE_USER")
	@GetMapping("/order/read")
	public void read() {
	}

	// 새 주소 추가 화면을 띄우는 메서도
	@Secured("ROLE_USER")
	@GetMapping("/order/new_address")
	public void newAddress() {
	}
	
	// 주문 완료 후 이동
	@Secured("ROLE_USER")
	@GetMapping("/order/list")
	public void list() {
	}
}
