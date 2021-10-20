package com.demo.cdmall1.domain.order.entity;

import java.util.List;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.stereotype.*;

import com.demo.cdmall1.web.dto.OrderDto;
import com.demo.cdmall1.web.dto.OrderDto.ReviewAvailable;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

import lombok.*;

@RequiredArgsConstructor
@Repository
public class OrderDslRepository {
	private final EntityManager entityManager;
	private JPAQueryFactory factory;
	private QOrder order;
	private QOrderItem orderItem;
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(entityManager);
		this.order = QOrder.order;
		this.orderItem = QOrderItem.orderItem;
	}
	
	public List<ReviewAvailable> reviewAvailableList(String loginId) {
		return factory.select(Projections.constructor(OrderDto.ReviewAvailable.class, order.orderNo, order.createTime, orderItem.image, orderItem.manufacturer, orderItem.name, orderItem.pno, orderItem.orderItemNo))
			.from(order).innerJoin(order.orderItems, orderItem).where(order.address().username.eq(loginId)).orderBy(order.createTime.desc()).fetch();
	}

	public ReviewAvailable getOrderItem(Integer orderNo, Integer orderItemNo, String loginId) {
		return factory.select(Projections.constructor(OrderDto.ReviewAvailable.class, order.orderNo, order.createTime, orderItem.image, orderItem.manufacturer, orderItem.name, orderItem.pno, orderItem.orderItemNo))
				.from(order).innerJoin(order.orderItems, orderItem).where(order.address().username.eq(loginId).and(orderItem.orderItemNo.eq(orderItemNo)))
				.orderBy(order.createTime.desc()).fetchOne();
	}
}
