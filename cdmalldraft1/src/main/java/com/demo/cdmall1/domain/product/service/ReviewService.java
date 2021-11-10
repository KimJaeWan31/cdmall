package com.demo.cdmall1.domain.product.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.util.FileCopyUtils;

import com.demo.cdmall1.domain.member.entity.Member;
import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.util.ZmallConstant;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ReviewService {
	private final ReviewRepository dao;
	private final ReviewDslRepository dslDao;
	
	public ReviewDto.ReviewListResponse list(Integer pno, Integer pageno){
		Pageable pageable = PageRequest.of(pageno-1, 10);
		Product product = Product.builder().pno(pno).build();
		ReviewDto.ReviewListResponse dto = new ReviewDto.ReviewListResponse(dslDao.readAll(pageable, product), dslDao.countByPno(product), pageno, 10);
		return dto;
	}
	
	public Review write(ReviewDto.Write dto, String loginId) {
		Review review = dto.toEntity().setWriter(loginId);
		Product product = Product.builder().pno(dto.getPno()).build();
		Member member = Member.builder().username(loginId).build();
		review.setProduct(product);
		review.setMember(member);
		
		System.out.println("****************dto2: "+review.getProduct().getPno());
		
		if(dto.getRattachments()==null)
			return dao.save(review);
		
		dto.getRattachments().forEach(uploadFile->{
			// 저장할 파일 이름을 지정(현재시간을 1/1000초 단위로 계산)
			String saveFileName = System.currentTimeMillis() + "-" + uploadFile.getOriginalFilename();
			File saveFile = new File(ZmallConstant.PRATTACHMENT_FOLDER, saveFileName);
			try {
				uploadFile.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			boolean isImage = uploadFile.getContentType().toLowerCase().startsWith("image/");
			// board는 관계의 주인이 아니다. board쪽에서 attachment를 저장하면 반영이 안된다
			review.addAttachment(new ReviewAttachment(0, review, uploadFile.getOriginalFilename(), saveFileName, uploadFile.getSize(), isImage));
		});
		
		System.out.println("********************review: "+review);
		return dao.save(review);
	}
	
	
	public double avgOfStars(int pno) {
		Product product = Product.builder().pno(pno).build();
		List<Integer> listOfStars = dslDao.readStarByPno(product);
		
		Integer sumOfStars = listOfStars.stream().mapToInt(Integer::intValue).sum();
		
		Long countOfStars = dslDao.countByPno(product);
		
		double scale = Math.pow(10, 1);
		double avgOfStars = (double) sumOfStars/countOfStars ;
			
		return Math.round(avgOfStars*scale)/scale;
	}
	
}
