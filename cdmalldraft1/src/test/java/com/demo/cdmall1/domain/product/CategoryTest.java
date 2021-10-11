package com.demo.cdmall1.domain.product;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.domain.product.service.*;

import lombok.extern.slf4j.*;

@Slf4j
@SpringBootTest
public class CategoryTest {
	@Autowired
	private CategoryRepository dao;
	@Autowired
	private CategoryService service;
	
	@Test
	public void insertTest() {
		// 대분류
		Category c1 = new Category("1", "강아지용품", null, null);
		Category c2 = new Category("2", "고양이용품", null, null);
		
		// 중분류 만들어 대분류에 추가
		Category c11 = Category.builder().categoryCode("11").categoryName("야외용품").build();
		Category c12 = Category.builder().categoryCode("12").categoryName("실내용품").build();
		c1.addCategory(c11);
		c1.addCategory(c12);
		
		Category c21 = Category.builder().categoryCode("21").categoryName("야외용품").build();
		Category c22 = Category.builder().categoryCode("22").categoryName("실내용품").build();
		c2.addCategory(c21);
		c2.addCategory(c22);
		
		// 소분류 만들어 중분류에 추가
		Category c111 = Category.builder().categoryCode("111").categoryName("목줄").build();
		Category c112 = Category.builder().categoryCode("112").categoryName("가슴줄").build();
		c11.addCategory(c111);
		c11.addCategory(c112);
		
		Category c121 = Category.builder().categoryCode("121").categoryName("안전문").build();
		Category c122 = Category.builder().categoryCode("122").categoryName("방석,매트,계단").build();
		c12.addCategory(c121);
		c12.addCategory(c122);
		
		Category c211 = Category.builder().categoryCode("211").categoryName("이동장").build();
		Category c212 = Category.builder().categoryCode("212").categoryName("리드줄").build();
		c21.addCategory(c211);
		c21.addCategory(c212);
		
		Category c221 = Category.builder().categoryCode("221").categoryName("캣타워").build();
		Category c222 = Category.builder().categoryCode("222").categoryName("화장실").build();
		c22.addCategory(c221);
		c22.addCategory(c222);
		
		dao.save(c1);
		dao.save(c2);
	}
	 
	//@Test
	public void findAllTest() {
		List<Category> categories = dao.findAll();
		log.info("개수 : {}", categories.size());
		categories.forEach(c->log.info("{}", c));
	}
	
	//@Test
	public void readAllTest() {
		service.readAll().forEach(c->log.info("{}", c));
	}
}
