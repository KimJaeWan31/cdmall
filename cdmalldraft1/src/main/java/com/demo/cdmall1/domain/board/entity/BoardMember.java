package com.demo.cdmall1.domain.board.entity;

import java.io.*;

import javax.persistence.*;

import lombok.*;

// 사용자가 추천/비추한 글
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BoardMemberId.class)
@Entity
public class BoardMember {
	/*
	 	@ManyToOne
		private Member member;
		@ManyToOne
		private Board board;
		
		관계가 걸리면 BoardClass에서 findById를 하면 {Member(username, email, password...), Board(bno, title, content.....)}
	*/
	@Id
	private String username;
	
	@Id
	private Integer bno;
}
