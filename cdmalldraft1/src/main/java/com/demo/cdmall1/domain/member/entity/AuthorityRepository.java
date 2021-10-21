package com.demo.cdmall1.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityId>{
	public Authority findByMember(Member Member);

	@Query("delete from Authority a where a.member=?1 and a.authorityName=?2" )
	public void deleteBymemberAndauthorityName(Member member, String string);

	
}
