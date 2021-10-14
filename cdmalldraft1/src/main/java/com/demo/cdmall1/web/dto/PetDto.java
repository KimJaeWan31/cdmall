package com.demo.cdmall1.web.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class PetDto {
	@Data
	@AllArgsConstructor
	public static class Add{
		@NotBlank(message="필수 입력입니다")
		private String petName;
		
		private MultipartFile petSajin;
		
		private Integer petAge;
		
		private Boolean gender;
		
		private String petKind;
	}

}
