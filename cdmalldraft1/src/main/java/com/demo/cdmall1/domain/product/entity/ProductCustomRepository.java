package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.*;

public interface ProductCustomRepository {
	// 검색
	List<ProductDto.ProductList> search(Pageable pageable, String word);
	
	public Long countAll(String word);
}
