package com.demo.cdmall1.domain.chatting.entity;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.demo.cdmall1.web.dto.ChattingHistoryDto;


public interface ChattingCustomRepository {
	List<ChattingHistoryDto.ListView> readAll(Pageable pageable, String guestid);
	
	public Long countAll(String guestid);
}
