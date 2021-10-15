package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsedBoardMvcController {
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/usedBoard/list")
	public void list() {
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/usedBoard/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/usedBoard/write")
	public void write() {	
	}
	
}
