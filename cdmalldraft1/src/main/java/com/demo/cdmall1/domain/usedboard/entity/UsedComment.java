package com.demo.cdmall1.domain.usedboard.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="usedBoard")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="usedcomments", indexes=@Index(name="usedcomment_idx_ubno", columnList="ubno"))
public class UsedComment extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="usedcomment_seq")
	@SequenceGenerator(name="usedcomment_seq", sequenceName="usedcomment_seq", allocationSize=1)
	private Integer ubcno;   
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ubno")
	private UsedBoard usedBoard;
	
	@Column(length=10)
	private String writer;
	
	@Column(length=100)
	private String content;
	
	@Column(length=15)
	private String profile;
	
	public UsedCommentDto.Read toDto() {
		return new UsedCommentDto.Read(ubcno, writer, this.getCreateTime(), content, ZmallConstant.PROFILE_URL +  profile);
	}
}
