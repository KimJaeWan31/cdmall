package com.demo.cdmall1.domain.member.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SellerRepository extends CrudRepository<Seller, String>{

	@Query("select count(s.username) from Seller s")
	public Long countAll();

}
