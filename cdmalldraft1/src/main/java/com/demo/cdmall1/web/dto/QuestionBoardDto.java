package com.demo.cdmall1.web.dto;

import java.time.*;
import java.util.*;
import java.util.List;

import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.web.dto.BoardDto.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;
import lombok.experimental.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionBoardDto {
	@Data
	@AllArgsConstructor
	public static class Write {		
		private String title;
		private String content;
		private java.util.List<MultipartFile> qsattachments;
		public QuestionBoard toEntity() {
			return QuestionBoard.builder().title(title).content(content).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Update {
		private Integer qbno;
		private String title;
		private String content;
		public QuestionBoard toEntity() {
			return QuestionBoard.builder().title(title).content(content).qbno(qbno).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class GoodOrBad {
		private Integer qbno;
		private Boolean isGood;
	}
	
	@Data
	@AllArgsConstructor
	public static class ListView {
		private Integer qbno;
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
		private Integer qbno;
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
		private List<QSCommentDto.Read> qscomments;
		private Set<Attachment> attachments;
	}
}
