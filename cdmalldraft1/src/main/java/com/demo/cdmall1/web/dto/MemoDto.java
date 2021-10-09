package com.demo.cdmall1.web.dto;

import java.time.*;

import com.demo.cdmall1.domain.memo.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoDto {
	@Data
	public static class Write {
		private String title;
		private String content;
		private String receiver;
		public Memo toEntity() {
			return Memo.builder().title(title).content(content).receiver(receiver).build();
		}				
	}
	
	@Data
	@AllArgsConstructor
	public static class ReceiveList {
		private Integer mno;
		private String title;
		private String content;
		private String sender;
		@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private Boolean isRead;
	}
	
	@Data
	@AllArgsConstructor
	public static class SendList {
		private Integer mno;
		private String title;
		private String content;
		private String receiver;
		@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private Boolean isRead;
	}
	
}
