package com.demo.cdmall1.domain.order.entity;

import java.io.*;

import lombok.*;

@SuppressWarnings("unused")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderAddressId implements Serializable  {
	private String username;
	private Integer addressNo;
}
