package com.demo.cdmall1.web.dto;

import java.time.*;

import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.noticeboard.entity.NoticeBoard;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeBoardDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		private String title;
		private String content;
		private java.util.List<MultipartFile> nbattachments;
		public NoticeBoard toEntity() {
			return NoticeBoard.builder().title(title).content(content).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Update {
		private Integer nbno;
		private String title;
		private String content;
		public NoticeBoard toEntity() {
			return NoticeBoard.builder().title(title).content(content).nbno(nbno).build();
		}
	}

	@Data
	@AllArgsConstructor
	public static class List {
		private Integer nbno;
		private String title;
		private String writer;
		@JsonProperty("writeTime")
		private LocalDateTime createDateTime;
		private Integer readCnt;
	}
}
