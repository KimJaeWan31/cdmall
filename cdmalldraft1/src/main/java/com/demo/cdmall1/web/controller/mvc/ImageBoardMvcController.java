package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ImageBoardMvcController {
	@GetMapping("/imageBoard/list")
	public void list() {
	}
	
	@GetMapping("/imageBoard/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/imageBoard/write")
	public void write() {
	}
}
