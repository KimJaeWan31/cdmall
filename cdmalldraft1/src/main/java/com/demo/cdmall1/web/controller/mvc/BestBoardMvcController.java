package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class BestBoardMvcController {
	@GetMapping("/bestBoard/bestList")
	public void bestList() {
	}
	
	@GetMapping("/bestBoard/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/bestBoard/write")
	public void write() {
	}
}
