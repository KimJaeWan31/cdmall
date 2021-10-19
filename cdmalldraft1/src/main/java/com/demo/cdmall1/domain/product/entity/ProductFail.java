package com.demo.cdmall1.domain.product.entity;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductFail {
	public static class AddressNotFoundException extends RuntimeException {
	}
	
	public static class ProductNotFoundException extends RuntimeException {
	}

	public static class OutOfStockException extends RuntimeException {
	}

	public static class OrderNotExistException extends RuntimeException {

	}
}
