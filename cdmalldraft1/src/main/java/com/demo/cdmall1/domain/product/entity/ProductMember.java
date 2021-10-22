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
	private Integer pno;
	@Id
	private String username;

}
