package com.demo.cdmall1.web.controller.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.cdmall1.domain.member.entity.Pet;
import com.demo.cdmall1.domain.member.service.PetService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PetController {
	private final PetService service;
	
	@DeleteMapping("/pets")
	public ResponseEntity<?> delete(Principal principal, Integer petno){
		List<Pet> pet = service.delete(principal.getName(), petno);
		return ResponseEntity.ok(pet);
	}
}
