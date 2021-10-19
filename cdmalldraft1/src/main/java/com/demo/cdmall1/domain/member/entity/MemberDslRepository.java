package com.demo.cdmall1.domain.member.entity;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MemberDslRepository {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QMember qmember;
	private QAuthority qauthority;
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
		qauthority  = QAuthority.authority;
		qmember  = QMember.member;
	}
	
	public Long countByUser() {
		QAuthority qauthority = QAuthority.authority;
		return factory.from(qauthority).select(qauthority.member.count()).where(qauthority.authorityName.contains("USER")).fetchOne();
	}
	
	public Long countByAdmin() {
		QAuthority qauthority = QAuthority.authority;
		return factory.from(qauthority).select(qauthority.member.count()).where(qauthority.authorityName.contains("ADMIN")).fetchOne();
	}

	//회원정보 읽기(ROLE_USER)
	public List<Member> readByUser() {
		QAuthority qauthority = QAuthority.authority;
		return factory.from(qauthority).select(qauthority.member).where(qauthority.authorityName.contains("USER")).fetch();	
	}
	
	//어드민 읽기
	public List<Member> readByAdmin() {
		QAuthority qauthority = QAuthority.authority;
		return factory.from(qauthority).select(qauthority.member).where(qauthority.authorityName.contains("ADMIN")).fetch();	
	}
	
}
