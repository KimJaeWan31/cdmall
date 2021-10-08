package com.demo.cdmall1.domain.questionboard.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="questionBoard")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="qscomments", indexes=@Index(name="qscomment_idx_qbno", columnList="qbno"))
public class QSComment extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qscomment_seq")
	@SequenceGenerator(name="qscomment_seq", sequenceName="qscomment_seq", allocationSize=1)
	private Integer qbcno;   
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="qbno")
	private QuestionBoard questionBoard;
	
	@Column(length=10)
	private String writer;
	
	@Column(length=100)
	private String content;
	
	@Column(length=15)
	private String profile;
	
	public QSCommentDto.Read toDto() {
		return new QSCommentDto.Read(qbcno, writer, this.getCreateTime(), content, ZmallConstant.PROFILE_URL +  profile);
	}
}
