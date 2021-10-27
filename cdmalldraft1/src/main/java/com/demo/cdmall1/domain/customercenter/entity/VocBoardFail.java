package com.demo.cdmall1.domain.customercenter.entity;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VocBoardFail {
	public static class VocBoardNotFoundException extends RuntimeException {
	}
	
	public static class IllegalJobException extends RuntimeException {	
	}

	public static class AlreadyRecommendException extends RuntimeException {
	}

	public static class VAttachmentNotFoundException extends RuntimeException {
	}
	
	public static class VCommentNotFoundException extends RuntimeException {
	}
}
