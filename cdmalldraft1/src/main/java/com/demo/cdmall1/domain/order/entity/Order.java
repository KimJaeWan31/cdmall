package com.demo.cdmall1.domain.order.entity;


import java.util.*;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.BaseCreateTimeEntity;

import lombok.*;
import lombok.experimental.*;

@Getter
@Setter
@ToString(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Builder
@Entity
@Table(name="orders", indexes=@Index(name="order_idx_orderer", columnList="orderer"))
public class Order extends BaseCreateTimeEntity  {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_seq")
	@SequenceGenerator(name="order_seq", sequenceName="order_seq", allocationSize=1)
	private Integer orderNo;
	
	private Integer orderPrice;
	
	@ManyToOne
	@JoinColumns({@JoinColumn(name="orderer", referencedColumnName="username"), @JoinColumn(name="addressNo", referencedColumnName="addressNo")})
	private OrderAddress address;

	@Enumerated(EnumType.ORDINAL)
	private DeliveryStatus deliveryStatus;
	
	@OneToMany(mappedBy="order", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private List<OrderItem> orderItems;
	
	public void addOrderItem(OrderItem orderItem) {
		if(this.orderItems==null)
			this.orderItems = new ArrayList<>();
		this.orderPrice = this.orderPrice==null? 0 : this.orderPrice;
		orderItem.setOrder(this);
		this.orderItems.add(orderItem);
		this.orderPrice += orderItem.getOrderItemPrice();
	}
}
