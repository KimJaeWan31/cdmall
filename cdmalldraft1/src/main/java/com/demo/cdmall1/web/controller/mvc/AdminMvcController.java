package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMvcController {

	@GetMapping("/admin/index")
	public void admin() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/admin/login")
	public void login() {
	}
}
