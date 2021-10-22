package com.demo.cdmall1.domain.usedboard.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.usedboard.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class UsedBoardMemberService {
	private final UsedBoardMemberRepository dao;

	@Transactional
	public WarnCheck warncheck(Integer ubno, String loginId) {
		boolean isExist = dao.existsByUbnoAndUsername(ubno, loginId);
		if(isExist==true) {
			UsedBoardMember usedBoardMember = dao.findByUbnoAndUsername(ubno, loginId);
			Boolean isWarn = usedBoardMember.getIsWarn();
		
			if(isWarn==true) {
				usedBoardMember.setIsWarn(false);
				return WarnCheck.SUB_WARM;	
			}
			usedBoardMember.setIsWarn(true);
			return WarnCheck.DO_WARN;
		} else {
			dao.save(new UsedBoardMember(loginId, ubno, "null" ,true));
			return WarnCheck.DO_WARN;
		}
	}

}
