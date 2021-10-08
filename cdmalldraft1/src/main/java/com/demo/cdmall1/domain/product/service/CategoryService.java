package com.demo.cdmall1.domain.product.service;

import java.util.*;

import javax.annotation.*;
import javax.transaction.*;

import org.hibernate.internal.build.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import com.demo.cdmall1.domain.product.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class CategoryService {
	@Autowired
	private final CategoryRepository dao;
	private List<Category> categories;
	
	// 쿼리가 부담스러운 경우 내용이 자주 변경되지 않는다면 캐시를 이용하자
	// cache -> update, delete 어떻게?
	// 내용이 자주 변경되는 경우라면 SpringCache를 이용하자 @Cacheable, @CacheEvict
	@PostConstruct
	public void init() {
		this.categories = dao.findAll();
	}
	
	public List<Category> readAll() {
		// 대분류, 중분류, 소분류를 각각 따로 읽어오자
		// 대분류 읽어 오자
		List<Category> largeCategories = new ArrayList<>();
		
		List<Object[]> list = dao.readLargeCategory();		// [{1	PC주요부품	} ,	{2		PC저장장치}]
		for(Object[] ar: list) {
			Category category = Category.builder().categoryCode((String)ar[0]).categoryName((String)ar[1]).build();
			largeCategories.add(category);
		}
		
		
		for(int i=0; i<largeCategories.size(); i++) {
			// 각 대분류에 해당하는 중분류들을 mediumCategories에 담는다
			List<Category> mediumCategories = new ArrayList<>();
			list = dao.readChildCategory(Category.builder().categoryCode(largeCategories.get(i).getCategoryCode()).build());
			for(Object[] ar: list) {
				Category category = Category.builder().categoryCode((String)ar[0]).categoryName((String)ar[1]).build();
				mediumCategories.add(category);
			}
			// 대분류 객체에 저장
			largeCategories.get(i).setCategories(mediumCategories);
			
			for(int j=0; j<largeCategories.get(i).getCategories().size(); j++) {
				// 각 중분류에 해당하는 소분류들을 smallCategories에 담는다
				List<Category> smallCategories = new ArrayList<>();
				list = dao.readChildCategory(Category.builder().categoryCode(mediumCategories.get(j).getCategoryCode()).build());
				for(Object[] ar: list) {
					Category category = Category.builder().categoryCode((String)ar[0]).categoryName((String)ar[1]).build();
					smallCategories.add(category);
				}
				// 대분류 객체에 들어있는 중분류 객체에 소분류들을 저장 
				largeCategories.get(i).getCategories().get(j).setCategories(smallCategories);
			}
		}
		
		return largeCategories;
		
	}

	public List<Category> read(String categoryCode) {
		// List, Set, Map -> java collection, collection api......
		List<Object[]> list = null;
		if(categoryCode==null || categoryCode.equals("")==true) 
			list = dao.readLargeCategory();
		else
			list = dao.readChildCategory(Category.builder().categoryCode(categoryCode).build());
		
		List<Category> result = new ArrayList<>();
		
		// (java) stream api
		list.forEach(ar->{
			Category category = Category.builder().categoryCode((String)ar[0]).categoryName((String)ar[1]).build();
			result.add(category);
		});
		return result;
	}
	
}









