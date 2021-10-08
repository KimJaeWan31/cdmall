package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="superCategory")			// System.out.println(this)으로 출력할 때 무한 루프 방지
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category {
	@Id
	@Column(name="category_code", length=10)
	private String categoryCode;
	
	@Column(name="category_name", length=10)
	private String categoryName;
	
	@JsonIgnore									// JSON으로 출력할 때 무한 루프 방지
	@ManyToOne
	@JoinColumn(name="super_category_code")
	private Category superCategory;				// 부모 카테코리
	
	// FetchType를 EAGER로 지정하면 전체 left outer join한 다음 모든 카테코리 코드에 대해 select가 발생 (N+1 문제)
	// 기본값인 LAZY 로딩은 무조건 no session 오류가 발생 -> 따로 따로 읽어야 하는데 읽을 범위를 계산 못하는 것이 아닐까....
	@OneToMany(mappedBy="superCategory", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Category> categories;			// 자식 카테고리
	
	public void addCategory(Category childCategory) {
		if(categories==null)
			categories = new ArrayList<>();
		childCategory.setSuperCategory(this);
		this.categories.add(childCategory);
	}
}
