package com.demo.cdmall1.domain.order.entity;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderFail {
	public static class AddressNotFoundException extends RuntimeException {
	}
	public static class OrderNotExistException extends RuntimeException {

	}
}
