package com.demo.cdmall1.domain.board.entity;


import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;


import lombok.*;

// 사용자가 추천/비추한 글
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BoardMemberId.class)
@Entity
@DynamicUpdate
public class BoardMember {
	@Id
	private String username;
	
	@Id
	private Integer bno;
	
	private String kind;
	
	private Boolean isWarn;
	
}
