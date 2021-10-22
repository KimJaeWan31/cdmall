package com.demo.cdmall1.web.controller.mvc;


import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class WishMvcController {
	@Secured("ROLE_USER")
	@GetMapping("/wish/wish_read")
	public void view() {
	}	
}
