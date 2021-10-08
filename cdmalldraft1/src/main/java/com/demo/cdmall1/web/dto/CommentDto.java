package com.demo.cdmall1.web.dto;

import java.time.*;

import javax.validation.constraints.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		@NotEmpty(message = "빈글을 작성할 수 없습니다")
		private String content;
		@NotNull
		private Integer bno;
	}
	
	@Data
	@AllArgsConstructor
	public static class Delete {
		private Integer cno;
		private Integer bno;
		public Comment toEntity() {
			return Comment.builder().board(Board.builder().bno(bno).build()).cno(cno).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Read {
		private Integer cno;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private String content;
		private String profile;
	}
}
