package com.demo.cdmall1.domain.product.service;

import java.io.*;
import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository dao;
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

	public Product read(Integer pno) {
		return dao.findById(pno).get();
	}

	public ProductDto.ProductListResponse list(Integer pageno, String manufacturer) {
		// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
		Pageable pageable = PageRequest.of(pageno-1, 15);
		ProductDto.ProductListResponse dto = new ProductDto.ProductListResponse(dslDao.readAll(pageable, manufacturer), dslDao.aaaaaa(manufacturer), pageno, 10);
		return dto;
	}

}
