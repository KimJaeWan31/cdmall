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
	public static class ProductParamList {
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
	public static class ProductWishList{
		private Integer pno;
		private String name;
		private String image;
		private Integer price;
		private String manufacturer;
		private String imageFileName;
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
	
	@Data
	@AllArgsConstructor
	public static class ProductWishListResponse {
		private List<ProductWishList> content;
		private Long totalcount;
		private Integer pageno;
		private Integer pagesize;
		
	}
	
	@Data
	@AllArgsConstructor
	public static class ListByMultiCateg{
		private Integer pageno;
		private List<String> categList;
	}


}
