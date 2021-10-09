package com.demo.cdmall1.web.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.demo.cdmall1.domain.chatting.entity.ChattingHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChattingHistoryDto {
	@Data
	@AllArgsConstructor
	public static class Save{ 
		private String guestid; 
		private String hostid;
		public ChattingHistory toEntity() {
			return ChattingHistory.builder().guestid(guestid).hostid(hostid).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class ListResponse {
		private List<ListView> content;
		private Long totalcount;
		private Integer pageno;
		private Integer pagesize;
	}

	@Data
	@AllArgsConstructor
	public static class ListView {
		private Integer chathno;
		
		@JsonFormat(pattern="yyyy년MM월dd일 HH:mm:ss")
		@JsonProperty("created_date")
		private LocalDateTime createdTime;
		private String guestid;
		private String hostid;
	}
	
	@Data
	public static class Read{
		private Integer chathno;
		private String guestid;
		private String hostid;
		private LocalDateTime createdTime;
		private String history;
	}
}
