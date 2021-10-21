package com.demo.cdmall1.domain.member.entity;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MemberFail {
	public static class UsernameExistException extends RuntimeException {
	}
	
	public static class EmailExistException extends RuntimeException {
	}
		
	public static class MemberNotFoundException extends RuntimeException {
	}
	
	public static class SellerNotFoundException extends RuntimeException {
	}

	public static class PasswordCheckException extends RuntimeException {
	}

	public static class JoinCheckFailException extends RuntimeException {
	}
}
