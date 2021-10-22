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
		
		ImageBoardMember imageBoardMember = dao.findByIbnoAndUsername(ibno, loginId);
		
		if(isExist==true) {
			
			if(imageBoardMember == null) {
				if(isGood==true) {
					dao.save(new ImageBoardMember(loginId, ibno, null, true));
					return LikeOrDislike.DO_LIKE;			
				}
				dao.save(new ImageBoardMember(loginId, ibno, null, false));
				return LikeOrDislike.DO_DISLIKE;
			}
			
			Boolean isHeart = imageBoardMember.getIsHeart();
			if(isHeart==null) {
				if(isGood==true) {
					imageBoardMember.setIsHeart(true);
					return LikeOrDislike.DO_LIKE;
				}
				else{
					imageBoardMember.setIsHeart(false);
					return LikeOrDislike.GET_LIKE;
				}	
			}
			
			if(isHeart==true) {
				if(isGood==true) {
					return LikeOrDislike.GET_LIKE;
				}
				else {
					imageBoardMember.setIsHeart(false);
					return LikeOrDislike.DO_DISLIKE;
				}
			}else {
				if(isGood==true) {
					imageBoardMember.setIsHeart(true);
					return LikeOrDislike.DO_LIKE;
				}
				else {
					return LikeOrDislike.GET_LIKE;
				}
			}
		} else {
			if(isGood==true) {
				dao.save(new ImageBoardMember(loginId, ibno, null, true));
				return LikeOrDislike.DO_LIKE;				// 추천수를 증가한 다음 읽어라
			}
			dao.save(new ImageBoardMember(loginId, ibno, null, false));
			return LikeOrDislike.DO_DISLIKE;					// 비추수를 증가한 다음 읽어라
		}
	}
	
	@Transactional
	 public boolean isExist(Integer ibno, String loginId) { 
		ImageBoardMember imageBoardMember= dao.findByIbnoAndUsername(ibno, loginId);
		if(imageBoardMember==null) {
			return false;
		}
		Boolean isHeart = imageBoardMember.getIsHeart();
		if(isHeart==null) {
			return false;
		}
		else {
			return isHeart;
		}
	 }
	
	@Transactional
	 public boolean isReport(Integer ibno, String loginId) { 
		boolean isTrue = dao.existsByIbnoAndUsername(ibno, loginId);
		return isTrue;
	 }
	
	 @Transactional
		public ReportCheck reportcheck(Integer ibno, String loginId) {
			boolean isExist = dao.existsByIbnoAndUsername(ibno, loginId);
			
			if(isExist==true) {
				ImageBoardMember imageBoardMember = dao.findByIbnoAndUsername(ibno, loginId);
				Boolean isReport = imageBoardMember.getIsReport();
				if(isReport==null) {
					isReport = false;
				}
				
				if(isReport==true) {
					imageBoardMember.setIsReport(false);
					return ReportCheck.SUB_REPORT;	
				}
				imageBoardMember.setIsReport(true);
				return ReportCheck.DO_REPORT;
			} else {
				dao.save(new ImageBoardMember(loginId, ibno, true, null));
				return ReportCheck.DO_REPORT;
			}
		}
}
