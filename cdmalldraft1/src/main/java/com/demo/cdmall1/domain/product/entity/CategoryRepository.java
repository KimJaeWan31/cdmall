package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface CategoryRepository extends JpaRepository<Category, String> {
	@Query("select c.categoryCode, c.categoryName from Category c where c.superCategory is null")
	public List<Object[]> readLargeCategory();

	@Query("select c.categoryCode, c.categoryName from Category c where c.superCategory=?1")
	public List<Object[]> readChildCategory(Category category);

	
}
