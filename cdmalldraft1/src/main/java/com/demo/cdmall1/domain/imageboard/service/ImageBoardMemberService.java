package com.demo.cdmall1.domain.imageboard.service;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.imageboard.entity.*;
import com.demo.cdmall1.domain.member.service.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ImageBoardMemberService {
	private final ImageBoardMemberRepository dao;
	@Transactional
	public GoodOrBad goodOrBad(Integer ibno, boolean isGood, String loginId) {
		boolean isExist = dao.existsByIbnoAndUsername(ibno, loginId);
		
		if(isExist==true) {
			if(isGood==true)
				return GoodOrBad.GET_GOOD;				// 추천수를 읽어라
			dao.deleteByIbnoAndUsername(ibno, loginId);
			return GoodOrBad.GET_BAD;					// 비추수를 읽어라
		} else {
			dao.save(new ImageBoardMember(loginId, ibno));
			if(isGood==true) 
				return GoodOrBad.DO_GOOD;				// 추천수를 증가한 다음 읽어라
			return GoodOrBad.DO_BAD;					// 비추수를 증가한 다음 읽어라
		}
		
	}
	
	 @Transactional(readOnly=true) 
	 public boolean isExist(Integer ibno, String loginId) { 
		boolean isTrue = dao.existsByIbnoAndUsername(ibno, loginId);
		System.out.println(isTrue);
		return isTrue;
	 }
}
