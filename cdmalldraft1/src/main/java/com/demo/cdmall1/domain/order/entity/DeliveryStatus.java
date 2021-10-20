package com.demo.cdmall1.domain.order.entity;

import com.fasterxml.jackson.annotation.*;

public enum DeliveryStatus {
	PAY("결제완료"), SHIPPING("배송중"), CANCEL("취소"), RETURN("환불");

	@JsonValue
	private String korean;
	
	private DeliveryStatus(String korean) {
		this.korean = korean;
	}
}
