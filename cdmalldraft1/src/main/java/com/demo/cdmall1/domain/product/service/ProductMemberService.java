package com.demo.cdmall1.domain.product.service;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.product.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ProductMemberService {
	private final ProductMemberRepository dao;
	
	
	@Transactional
	public WishorNot wishOrNot(Integer pno, boolean isGood, String loginId) {
		boolean isExist = dao.existsByPnoAndUsername(pno, loginId);
		
		if(isExist==true) {
			if(isGood==true)
				return WishorNot.GET_LIKE;				// 추천수를 읽어라
			dao.deleteByPnoAndUsername(pno, loginId);
			return WishorNot.GET_DISLIKE;					// 비추수를 읽어라
		} else {
			dao.save(new ProductMember(pno,loginId));
			if(isGood==true) 
				return WishorNot.DO_LIKE;				// 추천수를 증가한 다음 읽어라
			return WishorNot.DO_DISLIKE;					// 비추수를 증가한 다음 읽어라
		}
		
	}
	
	 @Transactional(readOnly=true) 
	 public boolean isExist(Integer pno, String loginId) { 
		boolean isTrue = dao.existsByPnoAndUsername(pno, loginId);
		System.out.println(isTrue);
		return isTrue;
	 }
	 public List<ProductMember> getWishListByUsername(String username) {
		 return  dao.findAllByUsername(username);
	 }
	 
	 
	 
	 
}
