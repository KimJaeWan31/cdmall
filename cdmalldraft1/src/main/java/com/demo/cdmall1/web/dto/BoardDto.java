package com.demo.cdmall1.web.dto;

import java.time.*;
import java.util.*;

import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;
import lombok.experimental.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		private String title;
		private String content;
		private java.util.List<MultipartFile> attachments;
		public Board toEntity() {
			return Board.builder().title(title).content(content).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Update {
		private Integer bno;
		private String title;
		private String content;
		public Board toEntity() {
			return Board.builder().title(title).content(content).bno(bno).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class GoodOrBad {
		private Integer bno;
		private Boolean isGood;
	}

	// querydsl에서 사용할 DTO
	@Data
	@AllArgsConstructor
	public static class ListView {
		private Integer bno;
		private String title;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime createDateTime;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer commentCnt;
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
	public static class Read {
		private Integer bno;
		private String title;
		private String content;
		private Integer badCnt;
		private Integer goodCnt;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer commentCnt;
		private LocalDateTime createTime;
		private LocalDateTime updateTime;
		private String writer;
		@Accessors(chain=true)
		private List<CommentDto.Read> comments;
		private Set<Attachment> attachments;
	}
}
