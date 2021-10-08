package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ImageBoardMvcController {
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/imageBoard/list")
	public void list() {
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/imageBoard/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/imageBoard/write")
	public void write() {
	}
}
