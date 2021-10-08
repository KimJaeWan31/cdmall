package com.demo.cdmall1.web.dto;

import java.time.*;


import javax.validation.constraints.*;

import com.demo.cdmall1.domain.noticeboard.entity.NBComment;
import com.demo.cdmall1.domain.noticeboard.entity.NoticeBoard;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NBCommentDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		@NotEmpty(message = "빈글을 작성할 수 없습니다")
		private String content;
		@NotNull
		private Integer nbno;
	}
	
	@Data
	@AllArgsConstructor
	public static class Delete {
		private Integer nbcno;
		private Integer nbno;
		public NBComment toEntity() {
			return NBComment.builder().noticeBoard(NoticeBoard.builder().nbno(nbno).build()).nbcno(nbcno).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Read {
		private Integer nbcno;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private String content;
		private String profile;
	}
}
