package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductCustomRepository  {
	@Query("select p from Product p")
	public List<Product> readAll();

	// 가격별 PRICE (가격높은순 desc, 가격낮은순 asc)
	@Query("select p from Product p order by price desc")
	public List<Product> readPriceDesc();
	
	@Query("select p from Product p order by price asc")
	public List<Product> readPriceAsc();
	
	// 판매순 SALES_AMOUNT (desc)
	@Query("select p from Product p order by sales_amount desc")
	public List<Product> readSalesAmountDesc();
	
	// 별점순  COUNT_OF_STAR (desc)
	@Query("select p from Product p order by count_of_star desc")
	public List<Product> readCountofstarDesc();
	
	// 리뷰순 REVIEW_COUNT (desc)
	@Query("select p from Product p order by review_count desc")
	public List<Product> readReviewcountDesc();
	
	// 최근등록순 CREATE_TIME
	@Query("select p from Product p order by create_time desc")
	public List<Product> readCreateTimeDesc();
	
	// 상품개수 pno로 읽어오기
	@Query("select p.stock from Product p where pno=?1")
	public Integer readStockByPno(Integer pno);

	// 상품개수 pno로 읽어오기
	@Query("select p.stock from Product p where pno = ?1")
	public Integer readStock(Integer pno);
}
