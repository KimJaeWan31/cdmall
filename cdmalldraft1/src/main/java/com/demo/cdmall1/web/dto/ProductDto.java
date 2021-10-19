package com.demo.cdmall1.web.dto;

import java.util.*;

import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.util.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {
	@Data
	@AllArgsConstructor
	public static class Write {
		private Integer pno;
		private String manufacturer;
		private String name;
		private MultipartFile image;
		private String info;
		private Integer price;
		private String categoryCode;
		private Integer stock;
		private List<Option> options;
		//private List<MultipartFile> prodattachments;
		public Product toEntity() {
			return Product.builder().manufacturer(manufacturer).name(name).info(info).price(price).categoryCode(categoryCode).stock(stock).build();
		}
	}
	
	
	@Data
	@AllArgsConstructor
	public static class ProductList {
		private Integer pno;
		private String manufacturer;
		private String name;
		private String image;
		private Integer price;
		private Double avgOfStar;
		private Integer reviewCount;
		private String imageFileName;
		private Integer goodCnt;
		private Integer goodCnlCnt;
		public void changeImageName() {
			this.image = ZmallConstant.PRODUCT_URL + this.image;
			
		}

	}
	
	@Data
	@AllArgsConstructor
	public static class ProductListResponse {
		private List<ProductList> content;
		private Long totalcount;
		private Integer pageno;
		private Integer pagesize;
		
	}

	// 중간에 추가한 값이 넘어오는가? null이 뜬다면
	// 		html에서 name이 틀렸다, jQuery에서 서버로 넘기지 않는다, DTO에 필드가 없다, 엔티티로 변환하는 메서드에서 누락됐다....

}
