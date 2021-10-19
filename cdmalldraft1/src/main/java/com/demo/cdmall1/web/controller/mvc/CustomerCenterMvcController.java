package com.demo.cdmall1.web.controller.mvc;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerCenterMvcController {
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/customerCenter/faqList")
	public void faqList() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/customerCenter/voc")
	public void voc() {
	}
}