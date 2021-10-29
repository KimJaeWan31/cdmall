package com.demo.cdmall1.web.dto;

import java.time.*;

import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		private String title;
		private String content;
		private String category;
		private java.util.List<MultipartFile> attachments;
		public Board toEntity() {
			return Board.builder().title(title).content(content).category(category).build();
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
	
	@Data
	@AllArgsConstructor
	public static class WarnCheck{
		private Integer bno;
		private Boolean isWarn;
	}

	// querydsl에서 사용할 DTO
	@Data
	@AllArgsConstructor
	public static class List {
		private Integer bno;
		private String title;
		private String writer;
		//@JsonProperty Json으로 변환시 이름을 개발자가 지정한대로 설정
		@JsonProperty("writeTime")
		private LocalDateTime creaDateTime;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer commentCnt;
		private Integer goodCnt;
		private Integer badCnt;
		private String category;
		private Integer warnCnt;
		//private Boolean isActive;
	}
	
	@Data
	@AllArgsConstructor
	public static class BestList {
		private Integer bno;
		private String title;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime creaDateTime;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer commentCnt;
		private Integer goodCnt;
		private Integer badCnt;
		private String category;
		private Integer warnCnt;
	}
	
	@Data
	@AllArgsConstructor
	public static class BestRank {
		private Integer bno;
		private String title;
		private String writer;
		private Integer commentCnt;
		private String category;
	}
	
	@Data
	@AllArgsConstructor
	public static class WarnList {
		private Integer bno;
		private String title;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime creaDateTime;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer commentCnt;
		private Integer goodCnt;
		private Integer badCnt;
		private String category;
		private Integer warnCnt;
		private Boolean isActive;
		
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
	
	
	/*
	 * @Data
	 * 
	 * @AllArgsConstructor public static class ListResponse { private List<ListView>
	 * content; private Long totalcount; private Integer pageno; private Integer
	 * pagesize; }
	 * 
	 * @Data public static class Read { private Integer bno; private String title;
	 * private String content; private Integer badCnt; private Integer goodCnt;
	 * private Integer readCnt; private Integer attachmentCnt; private Integer
	 * commentCnt; private LocalDateTime createTime; private LocalDateTime
	 * updateTime; private String writer;
	 * 
	 * @Accessors(chain=true) private List<CommentDto.Read> comments; private
	 * Set<Attachment> attachments; }
	 */
	 
}
