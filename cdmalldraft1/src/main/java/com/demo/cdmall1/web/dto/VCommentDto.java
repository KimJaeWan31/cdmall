package com.demo.cdmall1.web.dto;

import java.time.*;


import javax.validation.constraints.*;

import com.demo.cdmall1.domain.customercenter.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VCommentDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		@NotEmpty(message = "빈글을 작성할 수 없습니다")
		private String content;
		@NotNull
		private Integer vbno;
	}
	
	@Data
	@AllArgsConstructor
	public static class Delete {
		private Integer vcno;
		private Integer vbno;
		public VComment toEntity() {
			return VComment.builder().vocBoard(VocBoard.builder().vbno(vbno).build()).vcno(vcno) .build();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class Read {
		private Integer vcno;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createTime;
		private String content;
		private String profile;
	}
}
