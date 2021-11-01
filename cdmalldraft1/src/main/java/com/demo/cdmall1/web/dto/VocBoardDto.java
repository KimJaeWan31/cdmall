package com.demo.cdmall1.web.dto;

import java.time.*;


import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.customercenter.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VocBoardDto {
	@Data
	@AllArgsConstructor
	public static class Write {		
		private String title;
		private String content;
		private java.util.List<MultipartFile> vattachments;
		public VocBoard toEntity() {
			return VocBoard.builder().title(title).content(content).build();
		}
	}
	@Data
	@AllArgsConstructor
	public static class WriteReply {	
		private Integer vbno;
		private String originalWriter;
		private String title;
		private String content;
		private java.util.List<MultipartFile> vattachments;
		public VocBoard toEntity() {
			return VocBoard.builder().originalWriter(originalWriter).title(title).content(content).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Update {
		private Integer vbno;
		private String title;
		private String content;
		public VocBoard toEntity() {
			return VocBoard.builder().title(title).content(content).vbno(vbno).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class List {
		private Integer vbno;
		private String title;
		private String writer;
		//@JsonProperty Json으로 변환시 이름을 개발자가 지정한대로 설정
		@JsonProperty("writeTime")
		private LocalDateTime createDateTime;
		private Integer attachmentCnt;
		private Integer re_lev;
	}

}
