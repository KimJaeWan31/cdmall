package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product extends BaseCreateAndUpdateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_seq")
	@SequenceGenerator(name="product_seq", sequenceName="product_seq", allocationSize=1)
	private Integer pno;
	
	@Column(length=20)
	private String manufacturer;
	
	@Column(length=50)
	private String name;
	
	@Column(length=50)
	private String image;
	
	@Lob
	private String info;
	
	@Column(length=10)
	private String writer;
	
	private Integer goodCnt;
	
	private Integer goodCnlCnt;
	
	private Integer price;
	
	private Integer salesVolume;
	
	private Integer salesAmount;
	
	private Integer countOfStar;
	
	private Integer sumOfStar;
	
	private Double avgOfStar;
	
	private Integer reviewCount;
	
	private Integer stock;
	
	@Column(length=50)
	private String imageFileName;
	
	@OneToMany(mappedBy="product", cascade= {CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<Option> options;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.REMOVE)
	private Set<Review> reviews;
	
	@OneToMany(mappedBy="product", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<ProductAttachment> productattachments;
	
	// onetomany -> board를 저장할 때 첨부파일도 함께 저장, 보드를 읽어오거나 삭제할 때 댓글도 함께.
	// 객체를 필드로 가지면 관계를 맺어줘야 한다
	// private Category category;
	private String categoryCode;
	
	@PrePersist
	public void init() {
		this.goodCnlCnt = 0;
		this.goodCnt = 0;
		this.salesVolume = 0;
		this.salesAmount = 0;
		this.countOfStar = 0;
		this.sumOfStar = 0;
		this.avgOfStar = 0.0;
		this.reviewCount = 0;
	}
	
	public void addOption(Option option) {
		if(option.getName().equals(""))
			return;
		if(options==null)
			this.options = new HashSet<>();
		option.setProduct(this);
		options.add(option);
	}
	public void addAttachment(ProductAttachment productattachment) {
		if(productattachments==null)
			this.productattachments = new HashSet<ProductAttachment>();
		productattachment.setProduct(this);
		productattachments.add(productattachment);
	}
}






