package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("isAuthenticated()")
@Controller
public class MemoMvcController {
	@GetMapping("/memo/write")
	public void write() {
	}
	
	@GetMapping("/memo/send")
	public void send() {
	}
	
	@GetMapping("/memo/receive")
	public void receiver() {
	}
	
	@GetMapping("/memo/read")
	public void read() {
	}
}
