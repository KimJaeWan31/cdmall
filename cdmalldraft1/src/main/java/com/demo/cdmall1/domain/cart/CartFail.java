package com.demo.cdmall1.domain.cart;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartFail {
	public static class AlreadyExistException extends RuntimeException {
	}
	
	public static class IllegalJobException extends RuntimeException {
	}
}
