package com.demo.cdmall1.domain.imageboard.entity;

import java.io.*;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;

import lombok.*;

// 사용자가 추천/비추한 글
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ImageBoardMemberId.class)
@Entity
public class ImageBoardMember {
	@Autowired
	public ImageBoardMember(String loginId, Integer ibno) {
		this.username = loginId;
		this.ibno = ibno;
	}

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
	private Integer ibno;
	
	private Boolean isReport;
	
	private Boolean isHeart;
	/*
	@PrePersist
	public void init() {
		kind = "null";
	}*/
}
