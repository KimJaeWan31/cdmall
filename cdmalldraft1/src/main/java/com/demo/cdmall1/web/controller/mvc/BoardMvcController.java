package com.demo.cdmall1.web.controller.mvc;

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
}
