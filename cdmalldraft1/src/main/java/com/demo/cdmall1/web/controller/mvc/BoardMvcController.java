package com.demo.cdmall1.web.controller.mvc;

import javax.servlet.http.*;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class BoardMvcController {
	@GetMapping("/board/list")
	public void list() {
	}
	
	@GetMapping("/board/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/board/write")
	public void write() {
	}
	
	
	@GetMapping("/shop/bestList")
	public void shopBestList() {
	}
	
	
	@GetMapping("/shop/newList")
	public void shopNewList() {
	}
	
	@GetMapping("/shop/outdoorList")
	public void shopOutdoorList() {
	}
	
	@GetMapping("/board/readMap")
	public void map() {
		
	}
	//자유게시판검색
	@GetMapping("/board/search")
	public void search() {
		
	}
	
	@PostMapping("/board/search")
	public void search(@RequestParam (defaultValue = "1") Integer pageno,String word, HttpSession session) {
		session.setAttribute("word", word);
	}
	//추천게시판검색
	@GetMapping("/bestBoard/search")
	public void searchBest() {
		
	}
	
	@PostMapping("/bestBoard/search")
	public void searchBest(@RequestParam (defaultValue = "1") Integer pageno,String word, HttpSession session) {
		session.setAttribute("word", word);
	}
}
