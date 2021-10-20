package com.demo.cdmall1.web.dto;


import com.demo.cdmall1.domain.order.entity.OrderAddress;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderAddressDto {
	@Data
	public static class Add {
		private String nickname;
		private String zipcode;
		private String address1;
		private String address2;
		private Boolean isDefault;
		public OrderAddress toEntity() {
			return OrderAddress.builder().nickname(nickname).zipcode(zipcode).address1(address1).address2(address2).isDefault(isDefault).build();
		}
	}	
}
