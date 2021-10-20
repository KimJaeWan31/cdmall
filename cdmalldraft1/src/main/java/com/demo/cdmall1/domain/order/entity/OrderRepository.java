package com.demo.cdmall1.domain.order.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	@Query("select o from Order o where o.address.username=?1 order by o.orderNo asc")
	public List<Order> readAll(String username);

}
