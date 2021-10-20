package com.demo.cdmall1.domain.order.entity;

import java.io.*;

import lombok.*;

@SuppressWarnings("unused")
@EqualsAndHashCode
public class OrderItemId implements Serializable {
	private Integer order;
	private Integer orderItemNo;
}
