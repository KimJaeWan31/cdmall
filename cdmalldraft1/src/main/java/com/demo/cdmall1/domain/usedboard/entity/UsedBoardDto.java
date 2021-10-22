package com.demo.cdmall1.domain.usedboard.entity;

import java.time.*;
import java.util.*;
import java.util.List;

import org.springframework.web.multipart.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;
import lombok.experimental.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsedBoardDto {
	@Data
	@AllArgsConstructor
	public static class Write {		
		private String title;
		private String content;
		private java.util.List<MultipartFile> usedattachments;
		public UsedBoard toEntity() {
			return UsedBoard.builder().title(title).content(content).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Update {
		private Integer ubno;
		private String title;
		private String content;
		public UsedBoard toEntity() {
			return UsedBoard.builder().title(title).content(content).ubno(ubno).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class GoodOrBad {
		private Integer ubno;
		private Boolean isGood;
	}
	@Data
	@AllArgsConstructor
	public static class WarnCheck{
		private Integer ubno;
		private Boolean isWarn;
	}
	
	@Data
	@AllArgsConstructor
	public static class ListView {
		private Integer ubno;
		private String title;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime createDateTime;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer commentCnt;
		private Integer warnCnt;
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
		private Integer ubno;
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
		private List<UsedCommentDto.Read> usedcomments;
		private Integer warnCnt;
	}
	
	@Data
	@AllArgsConstructor
	public static class WarnList {
		private Integer ubno;
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
}
