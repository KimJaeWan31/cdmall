package com.demo.cdmall1.domain.order.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;


public interface OrderAddressRepository extends CrudRepository<OrderAddress, OrderAddressId> {
	@Query("select a from OrderAddress a where a.username=:username and a.isDefault=true")
	public OrderAddress findDefault(String username);

	public List<OrderAddress> findByUsername(String username);

	public boolean existsByUsername(String loginId);

	@Modifying
	@Query("update OrderAddress a set a.isDefault=false where a.username=?1")
	public void setDefaultToFalse(String loginId);

	public OrderAddress findByAddressNo(Integer addressNo);

}
