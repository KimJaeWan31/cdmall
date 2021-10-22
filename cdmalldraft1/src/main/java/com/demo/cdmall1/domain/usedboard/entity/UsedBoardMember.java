package com.demo.cdmall1.domain.usedboard.entity;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.DynamicUpdate;


import lombok.*;

// 사용자가 추천/비추한 글
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UsedBoardMemberId.class)
@Entity
@DynamicUpdate
public class UsedBoardMember {
	@Id
	private String username;
	
	@Id
	private Integer ubno;
	
	@NotNull
	private String kind;
	
	private Boolean isWarn;
	


	@PrePersist
	public void init() {
		kind = "null";
	}
}
