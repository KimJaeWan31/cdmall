package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoticeBoardMvcController {
	@GetMapping("/noticeBoard/list")
	public void list() {
	}
	
	@GetMapping("/noticeBoard/read")
	public void read() {
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/noticeBoard/write")
	public void write() {
	}
	
	@GetMapping("/noticeBoard/indexlist")
	public void indexList() {
	}
}
