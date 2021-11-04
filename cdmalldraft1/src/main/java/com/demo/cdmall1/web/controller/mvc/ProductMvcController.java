package com.demo.cdmall1.web.controller.mvc;

import javax.servlet.http.*;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductMvcController {
	@GetMapping("/product/insert")
	public void insert() {
	}
	
	@GetMapping("/product/read")
	public void read() {
	}
	
	@GetMapping("/product/list")
	public void list() {
	}
	
	@GetMapping("/product/newList")
	public void newList() {
	}
	
	@GetMapping("/product/bestList")
	public void bestList() {
	}
	
	@GetMapping("/product/listByCateg")
	public void listByCateg() {
	}
	
	@GetMapping("/product/listByRootCateg")
	public void listByRootCateg() {
	}
	
	
	@GetMapping("/product/listByMultiCateg")
	public void listByMultiCateg() {
	}
	
	@GetMapping("/shop/search")
	public void search() {
		
	}
	
	@PostMapping("/shop/search")
	public void search(@RequestParam(defaultValue = "1") Integer pageno, String word, HttpSession session) {
		session.setAttribute("word", word);
		//System.out.println(word);
		// <span  th:text="${list.sssss}">
	}
}
