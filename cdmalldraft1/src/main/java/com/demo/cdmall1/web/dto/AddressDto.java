package com.demo.cdmall1.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressDto {
	@Data
	@AllArgsConstructor
	public static class Read{
		private Integer adno;
		private String addressName;
	}
}
