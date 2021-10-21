package com.demo.cdmall1.domain.imageboard.service;

import javax.transaction.Transactional;



import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.imageboard.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ImageBoardMemberService {
	private final ImageBoardMemberRepository dao;
	@Transactional
	public LikeOrDislike likeOrDislike(Integer ibno, boolean isGood, String loginId) {
		boolean isExist = dao.existsByIbnoAndUsername(ibno, loginId);
		
		if(isExist==true) {
			if(isGood==true)
				return LikeOrDislike.GET_LIKE;				// 추천수를 읽어라
			dao.deleteByIbnoAndUsername(ibno, loginId);
			return LikeOrDislike.GET_DISLIKE;					// 비추수를 읽어라
		} else {
			dao.save(new ImageBoardMember(loginId, ibno));
			if(isGood==true) 
				return LikeOrDislike.DO_LIKE;				// 추천수를 증가한 다음 읽어라
			return LikeOrDislike.DO_DISLIKE;					// 비추수를 증가한 다음 읽어라
		}
		
	}
	
	@Transactional
	 public boolean isExist(Integer ibno, String loginId) { 
		boolean isTrue = dao.existsByIbnoAndUsername(ibno, loginId);
		return isTrue;
	 }
	 
	 @Transactional
		public ReportCheck reportcheck(Integer ibno, String loginId) {
			boolean isExist = dao.existsByIbnoAndUsername(ibno, loginId);
			if(isExist==true) {
				ImageBoardMember imageBoardMember = dao.findByIbnoAndUsername(ibno, loginId);
				Boolean isReport = imageBoardMember.getIsReport();
			
				if(isReport==true) {
					imageBoardMember.setIsReport(false);
					return ReportCheck.SUB_REPORT;	
				}
				imageBoardMember.setIsReport(true);
				return ReportCheck.DO_REPORT;
			} else {
				dao.save(new ImageBoardMember(loginId, ibno, true));
				return ReportCheck.DO_REPORT;
			}
		}
}
