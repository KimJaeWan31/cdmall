package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.ProductDto.*;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

@Repository
public class ProductDslRepository {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QProduct qproduct;
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
		qproduct  = QProduct.product;
	}

	// select * from board where bno>0;
	public List<ProductList> readAll(Pageable pageable, String manufacturer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qproduct.pno.gt(0));
		if(manufacturer!=null)
			condition.and(qproduct.manufacturer.eq(manufacturer));
		return factory.from(qproduct).select(Projections.constructor(ProductDto.ProductList.class, qproduct.pno, qproduct.manufacturer, qproduct.name, 
				qproduct.image,qproduct.price, qproduct.avgOfStar,qproduct.reviewCount,qproduct.imageFileName,qproduct.goodCnt, qproduct.goodCnlCnt)).where(qproduct.pno.gt(0))
				.orderBy(qproduct.pno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	public Long aaaaaa(String manufacturer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qproduct.pno.gt(0));
		if(manufacturer!=null)
			condition.and(qproduct.manufacturer.eq(manufacturer));
		
		return factory.from(qproduct).select(qproduct.pno.count()).where(condition).fetchOne();
	}
}
