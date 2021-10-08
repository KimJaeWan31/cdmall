package com.demo.cdmall1.web.dto;

import java.time.*;

import javax.validation.constraints.*;

import com.demo.cdmall1.domain.questionboard.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QSCommentDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		@NotEmpty(message = "빈글을 작성할 수 없습니다")
		private String content;
		@NotNull
		private Integer qbno;
	}
	
	@Data
	@AllArgsConstructor
	public static class Delete {
		private Integer qbcno;
		private Integer qbno;
		public QSComment toEntity() {
			return QSComment.builder().questionBoard(QuestionBoard.builder().qbno(qbno).build()).qbcno(qbcno) .build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Read {
		private Integer qbcno;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private String content;
		private String profile;
	}
}
