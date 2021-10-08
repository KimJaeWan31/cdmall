package com.demo.cdmall1.domain.product.service;

import java.util.*;

import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository dao;
	
	public Product insert(ProductDto.Write dto) {
		Product product = dto.toEntity();
		List<Option> options = dto.getOptions();
		product.setImage("default.jpg");
		// 옵션이 있는 경우 옵션 추가
		if(dto.getOptions()!=null)
			options.forEach(option->product.addOption(option));
		return dao.save(product);
	}

	public Product read(Integer pno) {
		return dao.findById(pno).get();
	}
}
