package com.demo.cdmall1.domain.product.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude = "review") // ToString을 만들 때 board빼고 만들어라
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
// 테이블의 이름을 변경하고 bno에 대한 인덱스를 지정(인덱스를 만들 때는 board가 아니라 bno)
@Table(name = "reviewComment", indexes = @Index(name = "reviewComment_idx_bno", columnList = "rno"))
public class ReviewComment extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewComment_seq")
	@SequenceGenerator(name = "reviewComment_seq", sequenceName = "reviewComment_seq", allocationSize = 1)
	private Integer rcno;

	// board-comment에 관계를 설정. board를 읽으면 comment가 읽히고, comment를 읽어도 board가 읽힌다
	@JsonIgnore
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "pno"), @JoinColumn(name = "rno") })
	//@JoinColumn(name="rno")
	private Review review;

	@Column(length = 10)
	private String writer;

	@Column(length = 100)
	private String content;

	@Column(length = 15)
	private String profile;

	public CommentDto.Read toDto() {
		return new CommentDto.Read(rcno, writer, this.getCreateTime(), content, ZmallConstant.PROFILE_URL + profile);
	}

}
