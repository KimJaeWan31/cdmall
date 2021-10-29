package com.demo.cdmall1.web.dto;

import java.time.*;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.imageboard.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageBoardDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		private String title; 
		private String content; 
		private java.util.List<MultipartFile> ibattachments;
		public ImageBoard toEntity() {
			return ImageBoard.builder().title(title).content(content).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Update {
		private Integer ibno;
		private String title;
		private String content;
		public ImageBoard toEntity() {
			return ImageBoard.builder().title(title).content(content).ibno(ibno).build();
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
	public static class ReportCheck{
		private Integer ibno;
		private Boolean isReport;
	}
	
	@Data
	@AllArgsConstructor
	public static class List {
		private Integer ibno;
		private String title;
		private String writer;
		private String imageFileName;
		@JsonProperty("writeTime")
		private LocalDateTime createDateTime;
		private Integer goodCnt;
		private Integer reportCnt;
	}
	
	@Data
	@AllArgsConstructor
	public static class ReportList {
		private Integer ibno;
		private String title;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime createDateTime;
		private Integer goodCnt;
		private Integer reportCnt;
		private Boolean isActive;
		
	}
	
	@Data
	@AllArgsConstructor
	public static class WarnList {
		private Integer ibno;
		private String title;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime createDateTime;
		private Integer goodCnt;
		private Integer reportCnt;
		private Boolean isActive;
	}
}
