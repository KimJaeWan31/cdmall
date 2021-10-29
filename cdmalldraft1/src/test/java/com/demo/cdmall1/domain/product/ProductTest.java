package com.demo.cdmall1.domain.product;

import java.util.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.product.entity.*;


@SpringBootTest
public class ProductTest {
	@Autowired
	private ProductRepository pdao;
	
	@Autowired
	private BoardRepository bdao;

	// 카테고리 코드
	// dog : 1
	// cat : 2
	// dog : 야외용품(11)
	/*
	 * @Test public void insertTest(){ for (int i = 0; i < 100; i++) { Product p =
	 * new Product(null, "제조사_"+i, "상품명_"+i, "상품이미지_"+i+".jpg",
	 * "상품설명입니다. 상품_"+i+"의 품질은 아주 좋습니다.", 10000+i, null, null, null, null, null,
	 * null, 10+i, null, null, "111"); pdao.save(p);
	 * 
	 * }
	 * 
	 * }
	 */
	 	@Test
		public void test1() {
			pdao.readAll();
		}
		
		//@Test
		public void test2() {
			List<Product> plist = pdao.readPriceDesc();
			System.out.println(plist);
		}
		
		//@Test
		public void test3() {
			List<Product> plist = pdao.readPriceAsc();
			System.out.println(plist);
		}
		
		//@Test
		public void test5() {
			List<Product> plist = pdao.readSalesAmountDesc();
			System.out.println(plist);
		}
		
		@Transactional
		//@Test
		public void test6() {
			List<Product> plist = pdao.readCountofstarDesc();
			System.out.println(plist);
		}
		
		@Transactional
		//@Test
		public void test7() {
			List<Product> plist = pdao.readReviewcountDesc();
			System.out.println(plist);
		}
		
		@Transactional
		//@Test
		public void test9() {
			List<Product> plist = pdao.readCreateTimeDesc();
			System.out.println(plist);
		}
		
		
	
}
