package com.demo.cdmall1.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerCenterMvcController {
	
	@GetMapping("/customerCenter/faqList")
	public void faqList() {
	}
	
	@GetMapping("/customerCenter/vocList")
	public void voc() {
	}
	
	@GetMapping("/customerCenter/vocWrite")
	public void vocWrite() {
	}
	
	@GetMapping("/customerCenter/vocRead")
	public void vocRead() {
	}
	
	@GetMapping("/customerCenter/vocWriteReply")
	public void vocWriteReply() {
	}
}
