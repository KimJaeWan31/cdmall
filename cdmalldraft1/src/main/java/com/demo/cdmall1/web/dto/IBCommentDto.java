package com.demo.cdmall1.web.dto;

import java.time.*;



import javax.validation.constraints.*;

import com.demo.cdmall1.domain.imageboard.entity.IBComment;
import com.demo.cdmall1.domain.imageboard.entity.ImageBoard;
import com.fasterxml.jackson.annotation.*;

import lombok.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IBCommentDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		@NotEmpty(message = "빈글을 작성할 수 없습니다")
		private String content;
		@NotNull
		private Integer ibno;
	}
	
	@Data
	@AllArgsConstructor
	public static class Delete {
		private Integer ibcno;
		private Integer ibno;
		public IBComment toEntity() {
			return IBComment.builder().imageBoard(ImageBoard.builder().ibno(ibno).build()).ibcno(ibcno).build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Read {
		private Integer ibcno;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private String content;
		private String profile;
	}
}
