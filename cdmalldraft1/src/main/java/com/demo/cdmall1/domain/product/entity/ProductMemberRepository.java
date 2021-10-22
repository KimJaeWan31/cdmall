package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import org.springframework.data.repository.*;

public interface ProductMemberRepository extends CrudRepository<ProductMember, ProductMemberId>  {
	boolean existsByPnoAndUsername(Integer pno, String loginId);
	
	void deleteByPnoAndUsername(Integer pno, String loginId);
	
	List<ProductMember> findAllByUsername(String username);
}
