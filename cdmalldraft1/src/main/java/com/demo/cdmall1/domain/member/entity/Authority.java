package com.demo.cdmall1.domain.member.entity;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="member")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(AuthorityId.class)
public class Authority {
	@Id
	@ManyToOne
	@JoinColumn(name="username")
	private Member member;
	
	@Id
	private String authorityName;
}
