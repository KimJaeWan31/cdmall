package com.demo.cdmall1.security;

import java.util.*;
import java.util.stream.*;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.member.entity.*;

import lombok.*;


// 스프링 시큐리티는 인증(authentication)과 인가(authorization) 기능을 제공한다
// 1. UserDetailsService가 DB에서 회원 정보를 읽어와  UserDetails 객체(Account)를 생성해 인증 프로바이더에게 넘겨준다
// 2. 스프링시큐리티의 인증 프로바이더가 Account와 사용자 입력 정보를 비교해 인증에 성공하면 Authentication 객체를 생성한다

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final MemberRepository dao;
	
	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 1. 사용자가 없으면 InternalAuthenticationServiceException을 발생시켜라
		Member member = dao.findById(username).orElseThrow(()->new InternalAuthenticationServiceException("USER NOT FOUND"));
		
		// 2-1. username, password, enabled로 UserDetails 객체를 생성
		Account account = Account.builder().username(member.getUsername()).password(member.getPassword()).isEnabled(member.getEnabled()).build();
		
		// 2-2. 권한 정보를 추가(권한 이름을 가지고 SimpleGrantedAuthority 객체를 생성)
		Collection<GrantedAuthority> authorities = member.getAuthorities().stream().map(a->new SimpleGrantedAuthority(a.getAuthorityName())).collect(Collectors.toList());
		
		account.setAuthorities(authorities);
		return account;
	}
}
