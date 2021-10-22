package com.demo.cdmall1.web.controller.rest;

import java.net.*;
import java.security.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.domain.product.service.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class ProductMemberController {
	private final ProductMemberService productMemberService;
	private final RestTemplate restTemplate;
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/product_member/good_or_bad")
	public ResponseEntity<?> goodOrBad(Integer pno, boolean isGood, Principal principal) {
		WishorNot state = productMemberService.wishOrNot(pno, isGood, principal.getName());
		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8081").path("/product/good_or_bad")
				.queryParam("pno", pno+"").queryParam("state", state.ordinal()+"").build().toUri();
		Integer cnt = restTemplate.getForObject(uri.toString(), Integer.class);
		return ResponseEntity.ok(cnt);
	}	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/product_member/is_exist")
	public ResponseEntity<?> isExist(Integer pno, String loginId) {
		boolean check = productMemberService.isExist(pno, loginId); 
		return ResponseEntity.ok(check);                                                                                                                                                                                                                                                                                                     
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/product_wish/wish")
	public ResponseEntity<?> findAllByUsername(Principal principal) {
		String username = principal.getName();
		List<ProductMember> wishList = productMemberService.getWishListByUsername(username);
		return ResponseEntity.ok(wishList);
	}
	
	
}
