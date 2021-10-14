package com.demo.cdmall1.domain.member.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.demo.cdmall1.domain.member.entity.Member;
import com.demo.cdmall1.domain.member.entity.MemberFail;
import com.demo.cdmall1.domain.member.entity.Pet;
import com.demo.cdmall1.domain.member.entity.PetId;
import com.demo.cdmall1.domain.member.entity.PetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PetService {
	private final PetRepository dao;
	
	@Transactional
	public List<Pet> delete(String loginId, Integer petno){
		PetId id = new PetId(loginId, petno);
		Pet pets = dao.findById(id).orElseThrow(MemberFail.MemberNotFoundException::new);
		
		dao.delete(pets);
		return dao.findByMember(Member.builder().username(loginId).build());
	}

}
