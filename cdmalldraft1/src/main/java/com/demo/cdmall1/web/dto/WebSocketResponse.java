package com.demo.cdmall1.web.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class WebSocketResponse {
	private String sender;
	private String receiver;
	private String title;
	private String content;
}
