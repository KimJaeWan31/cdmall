package com.demo.cdmall1.domain.customercenter.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="vocBoard")		// ToString을 만들 때 board빼고 만들어라
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
// 테이블의 이름을 변경하고 bno에 대한 인덱스를 지정(인덱스를 만들 때는 board가 아니라 bno)
@Table(name="vcomments", indexes=@Index(name="vcomment_idx_vbno", columnList="vbno"))
public class VComment extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="vcomment_seq")
	@SequenceGenerator(name="vcomment_seq", sequenceName="vcomment_seq", allocationSize=1)
	private Integer vcno;   
	
	// board-comment에 관계를 설정. board를 읽으면 comment가 읽히고, comment를 읽어도 board가 읽힌다
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="vbno")
	private VocBoard vocBoard;
	
	@Column(length=10)
	private String writer;
	
	@Column(length=100)
	private String content;
	
	@Column(length=15)
	private String profile;
	
	public VCommentDto.Read toDto() {
		return new VCommentDto.Read(vcno, writer, this.getCreateTime(), content, ZmallConstant.PROFILE_URL +  profile);
	}
}
