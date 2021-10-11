package com.demo.cdmall1.domain.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.cdmall1.domain.product.entity.Product;
import com.demo.cdmall1.domain.product.entity.ProductRepository;


@SpringBootTest
public class ProductTest {
	@Autowired
	private ProductRepository pdao;

	// 카테고리 코드
	// dog : 1
	// cat : 2
	// dog : 야외용품(11)
	@Test
	public void insertTest(){
		for (int i = 0; i < 100; i++) {
			Product p = new Product(null, "제조사_"+i, "상품명_"+i, "상품이미지_"+i+".jpg", 
							"상품설명입니다. 상품_"+i+"의 품질은 아주 좋습니다.", 10000+i, 
							null, null, null, null, null, null, 10+i, null, null, "111");
			pdao.save(p);
			
		}
		
	}
}
