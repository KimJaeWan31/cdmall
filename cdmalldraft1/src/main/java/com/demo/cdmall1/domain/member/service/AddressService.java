package com.demo.cdmall1.domain.member.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.demo.cdmall1.domain.member.entity.Address;
import com.demo.cdmall1.domain.member.entity.AddressId;
import com.demo.cdmall1.domain.member.entity.AddressRepository;
import com.demo.cdmall1.domain.member.entity.Member;
import com.demo.cdmall1.domain.member.entity.MemberFail;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AddressService {
	private final AddressRepository dao;
	
	@Transactional
	public List<Address> delete(String loginId, Integer adno){
		AddressId id = new AddressId(loginId, adno);
		Address address = dao.findById(id).orElseThrow(MemberFail.MemberNotFoundException::new);
		
		dao.delete(address);
		return dao.findByMember(Member.builder().username(loginId).build());
	}

	public Address choice(String loginId, Integer adno) {
		AddressId id = new AddressId(loginId, adno);
		return dao.findById(id).orElseThrow(MemberFail.MemberNotFoundException::new);
	}
	
}
