package com.demo.cdmall1.domain.customercenter.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.*;

public interface VocBoardCustomRepository {
		List<VocBoardDto.List> readAll(Pageable pageable);
		
		public Long countByVbno();

}
