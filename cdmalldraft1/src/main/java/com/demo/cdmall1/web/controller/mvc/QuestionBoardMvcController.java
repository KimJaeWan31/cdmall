package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuestionBoardMvcController {
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/questionBoard/list")
	public void list() {
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/questionBoard/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/questionBoard/write")
	public void write() {	
	}
	
}
