package com.demo.cdmall1.domain.usedboard.entity;

import java.time.*;

import javax.validation.constraints.*;

import com.demo.cdmall1.domain.questionboard.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsedCommentDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		@NotEmpty(message = "빈글을 작성할 수 없습니다")
		private String content;
		@NotNull
		private Integer ubno;
	}
	
	@Data
	@AllArgsConstructor
	public static class Delete {
		private Integer ubcno;
		private Integer ubno;
		public UsedComment toEntity() {
			return UsedComment.builder().usedBoard(UsedBoard.builder().ubno(ubno).build()).ubcno(ubcno) .build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Read {
		private Integer ubcno;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private String content;
		private String profile;
	}
}
