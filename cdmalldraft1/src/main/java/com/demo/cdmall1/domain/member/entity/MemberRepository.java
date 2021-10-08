package com.demo.cdmall1.domain.member.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

public interface MemberRepository extends CrudRepository<Member, String> {
	
	// 기본으로 제공하는 existsById는 select count(*) from member where username=?
	// 수동으로 만든 exists 쿼리는 limit(오라클의 rownum)가 걸린다
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<Member> findByCheckcode(String checkcode);

	@Query("select m.username from Member m where m.email=:email")
	Optional<String> findUsernameByEmail(String email);

	List<Member> findByCheckcodeIsNotNull();
	
}
