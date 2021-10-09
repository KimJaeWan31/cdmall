package com.demo.cdmall1.domain.member.entity;

import org.springframework.data.repository.*;

public interface AuthorityRepository extends CrudRepository<Authority, AuthorityId>{
	public Authority findByMember(Member Member);
}
