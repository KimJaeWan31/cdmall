package com.demo.cdmall1.domain.product.service;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository dao;
	private final ProductMemberRepository pmdao;
	private final ProductDslRepository dslDao;
	
	public Product insert(ProductDto.Write dto, String name ) {
		Product product = dto.toEntity();

		
		MultipartFile uploadFile = dto.getImage();
		product.setImage(uploadFile.getOriginalFilename());
		
		// 저장할 파일 이름을 지정(현재시간을 1/1000초 단위로 계산)
			
		String saveFileName = System.currentTimeMillis() + "-" + uploadFile.getOriginalFilename();
		File saveFile = new File(ZmallConstant.PRODIMAGE_FOLDER, saveFileName);
		product.setImageFileName(saveFileName);
		try {
			uploadFile.transferTo(saveFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		boolean isImage = uploadFile.getContentType().toLowerCase().startsWith("image/");
		// board는 관계의 주인이 아니다. board쪽에서 attachment를 저장하면 반영이 안된다
		product.addAttachment(new ProductAttachment(0, null, uploadFile.getOriginalFilename(), saveFileName, uploadFile.getSize(), isImage));
		
		return dao.save(product);
	}

	@Transactional(readOnly=true)
	public Product read(Integer pno) {
		Product product = dao.findById(pno).orElseThrow(ProductFail.ProductNotFoundException::new);
		return product;
	}
	
	public Boolean checkStock(Integer pno, Integer count) {
		if(dao.readStock(pno)<count)
			throw new ProductFail.OutOfStockException();
		return dao.readStock(pno)>=count;
	}
	
	public Map<String,Object> readSearchAll(Integer pageno, String word){
		Pageable pageable = PageRequest.of(pageno-1, 10);
		Map<String,Object> map = new HashMap<>();
		map.put("content", dao.search(pageable, word));
		map.put("totalcount", dao.countAll(word));
		map.put("pageno", pageno);
		map.put("pagesize", 10);
		return map;
	}

	@Transactional(readOnly=true)
	public ProductDto.ProductListResponse list(Integer pageno, String manufacturer) {
		// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
		Pageable pageable = PageRequest.of(pageno-1, 15);
		ProductDto.ProductListResponse dto = new ProductDto.ProductListResponse(dslDao.readAll(pageable, manufacturer), dslDao.countAll(manufacturer), pageno, 15);
		return dto;
	}
	
	@Transactional(readOnly=true)
	public ProductDto.ProductListResponse listByCateg(Integer pageno, String categCode) {
		// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
		Pageable pageable = PageRequest.of(pageno-1, 15);
		ProductDto.ProductListResponse dto = new ProductDto.ProductListResponse(dslDao.readByCateg(pageable, categCode), dslDao.countByCateg(categCode), pageno, 15);
		
		return dto;
	}
	
	@Transactional(readOnly=true)
	public ProductDto.ProductWishListResponse wishList(Integer pageno, String username) {
		// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
		Pageable pageable = PageRequest.of(pageno-1, 10);
		ProductDto.ProductWishListResponse dto = new ProductDto.ProductWishListResponse(dslDao.readByUsername(pageable, username), dslDao.wishTotalCount(username), pageno, 10);
		
		
		return dto;
	}
	
	@Transactional(readOnly=true)
	public ProductDto.ProductListResponse listByCreateTime(Integer pageno, String manufacturer) {
		// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
		Pageable pageable = PageRequest.of(pageno-1, 15);
		ProductDto.ProductListResponse dto = new ProductDto.ProductListResponse(dslDao.readAll(pageable, manufacturer), dslDao.countAll(manufacturer), pageno, 15);
		return dto;
	}
	
	@Transactional(readOnly=true)
	public ProductDto.ProductListResponse listBySalesAmount(Integer pageno, String manufacturer) {
		// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
		Pageable pageable = PageRequest.of(pageno-1, 15);
		ProductDto.ProductListResponse dto = new ProductDto.ProductListResponse(dslDao.readAll(pageable, manufacturer), dslDao.countAll(manufacturer), pageno, 15);
		return dto;
	}
	
	
	
	
	
	@Transactional
	public Integer goodOrBad(Integer pno, Integer state) {
		Product product = dao.findById(pno).orElseThrow(BoardFail.BoardNotFoundException::new);
		
		if(state==0) {
			product.setGoodCnt(product.getGoodCnt()+1);
			return product.getGoodCnt();
		} else if(state==1) {
			product.setGoodCnt(product.getGoodCnt()-1);
			return product.getGoodCnt();
		} else if(state==2) {
			product.setGoodCnt(product.getGoodCnt());
		}
		
		return product.getGoodCnt();
	}

	public void continueShopping(String currentUrl) {
		HttpSession session = ZmallUtil.getSession();	
		session.setAttribute("saved_url", currentUrl);
	}

	@Transactional
	public void wishDelete(List<Integer> dtos) {
		
		for(int i = 0; i < dtos.size(); i++) {
			pmdao.deleteByPno(dtos.get(i));
			Product product = dao.findById(dtos.get(i)).orElseThrow(BoardFail.BoardNotFoundException::new);
			product.setGoodCnt(product.getGoodCnt()-1);
			//return 값이랑, 개별적으로 삭제 버튼 활성화
			
		}
		
	}
	
	@Transactional
	public void productCurrentWishDelete(Integer pno) {
			pmdao.deleteByPno(pno);
			Product product = dao.findById(pno).orElseThrow(BoardFail.BoardNotFoundException::new);
			product.setGoodCnt(product.getGoodCnt()-1);
			//return 값이랑, 개별적으로 삭제 버튼 활성화
			
		
	}

	


}
