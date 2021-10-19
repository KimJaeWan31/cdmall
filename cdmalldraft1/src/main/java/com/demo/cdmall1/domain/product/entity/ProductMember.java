package com.demo.cdmall1.domain.product.entity;

import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProductMemberId.class)
@Entity
public class ProductMember {
	
	@Id
	private String username;
	
	@Id
	private Integer pno;
}
