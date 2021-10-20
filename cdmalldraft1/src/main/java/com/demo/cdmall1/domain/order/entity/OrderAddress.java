package com.demo.cdmall1.domain.order.entity;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Builder
@IdClass(OrderAddressId.class)
@Entity
public class OrderAddress {
	@Id
	@Column(length=10)
	private String username;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_address_seq")
	@SequenceGenerator(name="order_address_seq", sequenceName="order_address_seq", allocationSize=1)
	private Integer addressNo;
	
	@Id
	@Column(length=10)
	private String nickname;
	
	@Column(length=5)
	private String zipcode;
	
	@Column(length=50)
	private String address1;
	
	@Column(length=50)
	private String address2;
	
	private boolean isDefault;
}
