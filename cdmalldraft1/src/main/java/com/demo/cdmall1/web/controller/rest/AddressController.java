package com.demo.cdmall1.web.controller.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.cdmall1.domain.member.entity.Address;
import com.demo.cdmall1.domain.member.service.AddressService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AddressController {
	private final AddressService service;
	
	@DeleteMapping("/address")
	public ResponseEntity<?> delete(Principal principal, Integer adno){
		List<Address> addresses = service.delete(principal.getName(), adno);
		return ResponseEntity.ok(addresses);
	}
	
	@GetMapping("/address")
	public ResponseEntity<?> choice(Principal principal, Integer adno){
		Address addresses = service.choice(principal.getName(), adno);
		return ResponseEntity.ok(addresses);
	}
}
