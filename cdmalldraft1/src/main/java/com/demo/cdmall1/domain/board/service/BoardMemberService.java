package com.demo.cdmall1.domain.board.service;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.member.service.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class BoardMemberService {
	private final BoardMemberRepository dao;
	
	public GoodOrBad goodOrBad(Integer bno, boolean isGood, String loginId) {
		boolean isExist = dao.existsByBnoAndUsername(bno, loginId);
		System.out.println();
		if(isExist==true) {
			if(isGood==true)
				return GoodOrBad.GET_GOOD;				// 추천수를 읽어라
			return GoodOrBad.GET_BAD;					// 비추수를 읽어라
		} else {
			dao.save(new BoardMember(loginId, bno));
			if(isGood==true) 
				return GoodOrBad.DO_GOOD;				// 추천수를 증가한 다음 읽어라
			return GoodOrBad.DO_BAD;					// 비추수를 증가한 다음 읽어라
		}
		
	}

}
