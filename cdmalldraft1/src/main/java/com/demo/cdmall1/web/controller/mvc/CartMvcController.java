package com.demo.cdmall1.web.controller.mvc;


import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartMvcController {
	@Secured("ROLE_USER")
	@GetMapping("/cart/cart_read")
	public void view() {
	}	
}
