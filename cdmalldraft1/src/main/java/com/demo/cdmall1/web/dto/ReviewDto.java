package com.demo.cdmall1.web.dto;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDto {
	@Data
	@AllArgsConstructor 
	public static class Write { 
		private Integer pno; 
		private String writer; 
		private String content;
		private Integer star;
		private List<MultipartFile> rattachments; 
		public Review toEntity() { 
			return Review.builder().star(star).writer(writer).content(content).build(); 
		} 
	}
	
	@Data
	@AllArgsConstructor
	public static class ReviewList{
		private Integer rno;
		private Integer star;
		private String content;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime createTime;
		private Integer readCnt;
		private Integer goodCnt;
		private Integer badCnt;
		private Integer warnCnt;
	}
	
	@Data
	@AllArgsConstructor
	public static class ReviewListResponse {
		private List<ReviewList> content;
		private Long totalcount;
		private Integer pageno;
		private Integer pagesize;
	}

	@Data
	@AllArgsConstructor
	public static class Update {
		public String getContent() {
			return null;
		}
	}
	
	/*
	 * @Data
	 * 
	 * @AllArgsConstructor public static class StarList{ private Integer star; }
	 */
}
