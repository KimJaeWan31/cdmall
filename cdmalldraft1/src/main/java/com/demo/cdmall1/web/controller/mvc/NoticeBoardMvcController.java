package com.demo.cdmall1.web.controller.mvc;

import javax.servlet.http.*;

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
	
	//공지게시판검색
	@GetMapping("/noticeBoard/search")
	public void search() {	
	}
		
	@PostMapping("/noticeBoard/search")
	public void search(@RequestParam (defaultValue = "1") Integer pageno,String word, HttpSession session) {
		session.setAttribute("word", word);
	}
}
