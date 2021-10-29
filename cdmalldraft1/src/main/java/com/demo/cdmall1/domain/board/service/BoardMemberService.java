package com.demo.cdmall1.domain.board.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.member.service.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class BoardMemberService {
	private final BoardMemberRepository dao;
	
	@Transactional
	public GoodOrBad goodOrBad(Integer bno, boolean isGood, String loginId) {
		boolean isExist = dao.existsByBnoAndUsername(bno, loginId);

		if(isExist==true) {
			BoardMember boardMember = dao.findByBnoAndUsername(bno, loginId);
			
			System.out.println("=====isExist===============");
			if(boardMember.getKind().equals(null)) {
				boardMember.setKind("null");
			}
			String kind = boardMember.getKind();
			if(kind.equals("good")) {
				if(isGood) {
					boardMember.setKind("null");	
					return GoodOrBad.SUB_GOOD;
				}
				return GoodOrBad.GET_BAD;	
			}else if(kind.equals("bad")){
				if(isGood == false) {
					boardMember.setKind("null");
					return GoodOrBad.SUB_BAD;
				}
				return GoodOrBad.GET_GOOD;		
			}else if(kind.equals("null")){
				if(isGood==true) {
					boardMember.setKind("good");
					return GoodOrBad.DO_GOOD;		
				}else {
					boardMember.setKind("bad");	
					return GoodOrBad.DO_BAD;
				}
			}
		} else {
			System.out.println("=====isNOtExist===============");
			if(isGood==true) {
				System.out.println("=====isGood===============");
				dao.save(new BoardMember(loginId, bno, "good", false));
				return GoodOrBad.DO_GOOD;		
			} 
			dao.save(new BoardMember(loginId, bno, "bad", false));	
			return GoodOrBad.DO_BAD;				
		}
		return GoodOrBad.GET_GOOD;
	}
	
	@Transactional
	public WarnCheck warncheck(Integer bno, String loginId) {
		boolean isExist = dao.existsByBnoAndUsername(bno, loginId);
		if(isExist==true) {
			BoardMember boardMember = dao.findByBnoAndUsername(bno, loginId);
			Boolean isWarn = boardMember.getIsWarn();
		
			if(isWarn==true) {
				boardMember.setIsWarn(false);
				return WarnCheck.SUB_WARM;	
			}
			boardMember.setIsWarn(true);
			return WarnCheck.DO_WARN;
		} else {
			dao.save(new BoardMember(loginId, bno, "nll" ,true));
			return WarnCheck.DO_WARN;
		}
	}

}
