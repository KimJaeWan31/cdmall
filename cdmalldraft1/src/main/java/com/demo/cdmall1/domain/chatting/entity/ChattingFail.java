package com.demo.cdmall1.domain.chatting.entity;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChattingFail {
	public static class MessageHistoryNotFoundException extends RuntimeException {
	}
	
	public static class IllegalJobException extends RuntimeException {	
	}

	public static class AlreadyRecommendException extends RuntimeException {
	}

	public static class AttachmentNotFoundException extends RuntimeException {
	}
	
	
}
