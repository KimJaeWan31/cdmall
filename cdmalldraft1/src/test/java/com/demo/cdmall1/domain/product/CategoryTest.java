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
		Category c11 = Category.builder().categoryCode("11").categoryName("푸드").build();
		Category c12 = Category.builder().categoryCode("12").categoryName("리빙").build();
		Category c13 = Category.builder().categoryCode("13").categoryName("장난감").build();
		Category c14 = Category.builder().categoryCode("14").categoryName("의류").build();
		c1.addCategory(c11);
		c1.addCategory(c12);
		c1.addCategory(c13);
		c1.addCategory(c14);
		
		Category c21 = Category.builder().categoryCode("21").categoryName("푸드").build();
		Category c22 = Category.builder().categoryCode("22").categoryName("리빙").build();
		Category c23 = Category.builder().categoryCode("23").categoryName("장난감").build();
		Category c24 = Category.builder().categoryCode("24").categoryName("미용").build();
		c2.addCategory(c21);
		c2.addCategory(c22);
		c2.addCategory(c23);
		c2.addCategory(c24);
		
		// 소분류 만들어 중분류에 추가
		Category c111 = Category.builder().categoryCode("111").categoryName("사료").build();
		Category c112 = Category.builder().categoryCode("112").categoryName("간식").build();
		Category c113 = Category.builder().categoryCode("113").categoryName("영양제").build();
		c11.addCategory(c111);
		c11.addCategory(c112);
		c11.addCategory(c113);
		
		Category c121 = Category.builder().categoryCode("121").categoryName("물통").build();
		Category c122 = Category.builder().categoryCode("122").categoryName("식기").build();
		Category c123 = Category.builder().categoryCode("123").categoryName("배변패드").build();
		Category c124 = Category.builder().categoryCode("124").categoryName("미용").build();
		c12.addCategory(c121);
		c12.addCategory(c122);
		c12.addCategory(c123);
		c12.addCategory(c124);
		
		Category c131 = Category.builder().categoryCode("131").categoryName("노즈워크").build();
		Category c132 = Category.builder().categoryCode("132").categoryName("터그").build();
		Category c133 = Category.builder().categoryCode("133").categoryName("공").build();
		c21.addCategory(c131);
		c21.addCategory(c132);
		c21.addCategory(c133);
		
		Category c141 = Category.builder().categoryCode("c141").categoryName("대").build();
		Category c142 = Category.builder().categoryCode("c142").categoryName("중").build();
		Category c143 = Category.builder().categoryCode("c143").categoryName("소").build();
		c21.addCategory(c141);
		c21.addCategory(c142);
		c21.addCategory(c143);
		
		
		Category c211 = Category.builder().categoryCode("211").categoryName("사료").build();
		Category c212 = Category.builder().categoryCode("212").categoryName("간식").build();
		Category c213 = Category.builder().categoryCode("213").categoryName("영양제").build();
		c21.addCategory(c211);
		c21.addCategory(c212);
		c21.addCategory(c213);
		
		Category c221 = Category.builder().categoryCode("221").categoryName("캣타워").build();
		Category c222 = Category.builder().categoryCode("221").categoryName("물통").build();
		Category c223 = Category.builder().categoryCode("223").categoryName("하우스").build();
		c22.addCategory(c221);
		c22.addCategory(c222);
		c22.addCategory(c223);
		
		
		
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
