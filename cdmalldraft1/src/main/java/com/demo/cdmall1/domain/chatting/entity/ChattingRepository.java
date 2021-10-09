package com.demo.cdmall1.domain.chatting.entity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ChattingRepository extends JpaRepository<ChattingHistory, Integer>, ChattingCustomRepository{

}
