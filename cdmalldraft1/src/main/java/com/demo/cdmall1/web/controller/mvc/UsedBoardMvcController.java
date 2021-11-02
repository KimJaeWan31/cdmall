package com.demo.cdmall1.web.controller.mvc;

import javax.servlet.http.*;

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
	//중고게시판검색
	@GetMapping("/usedBoard/search")
	public void search() {
		
	}
	
	@PostMapping("/usedBoard/search")
	public void search(@RequestParam (defaultValue = "1") Integer pageno,String word, HttpSession session) {
		session.setAttribute("word", word);
	}
}
