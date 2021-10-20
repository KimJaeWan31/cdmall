package com.demo.cdmall1.web.dto;

import java.time.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class OrderDto {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderProduct {
		private Integer pno;
		private Integer count;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class List {
		private Integer pno;
		private Integer count;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ReviewAvailable {
		private Integer orderNo;
		@JsonProperty("orderday")
		@JsonFormat(pattern="yyyy년 MM월 dd일")
		private LocalDateTime createTime;
		private String image;
		private String manufacturer;
		private String name;
		private Integer pno;
		private Integer orderItemNo;
	}
}
