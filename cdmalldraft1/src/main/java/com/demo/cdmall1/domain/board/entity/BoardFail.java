package com.demo.cdmall1.domain.board.entity;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardFail {
	public static class BoardNotFoundException extends RuntimeException {
	}
	
	public static class IllegalJobException extends RuntimeException {	
	}

	public static class AlreadyRecommendException extends RuntimeException {
	}

	public static class AttachmentNotFoundException extends RuntimeException {
	}
	
	public static class CommentNotFoundException extends RuntimeException {
	}
}
