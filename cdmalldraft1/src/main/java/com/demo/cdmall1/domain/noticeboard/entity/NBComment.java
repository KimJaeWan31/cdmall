package com.demo.cdmall1.domain.noticeboard.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.BaseCreateTimeEntity;
import com.demo.cdmall1.util.ZmallConstant;
import com.demo.cdmall1.web.dto.NBCommentDto;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="noticeBoard")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="nbcomments", indexes=@Index(name="nbcomment_idx_nbno", columnList="nbno"))
public class NBComment extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="nbcomment_seq")
	@SequenceGenerator(name="nbcomment_seq", sequenceName="nbcomment_seq", allocationSize=1)
	private Integer nbcno;   
	
	// board-comment에 관계를 설정. board를 읽으면 comment가 읽히고, comment를 읽어도 board가 읽힌다
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="nbno")
	private NoticeBoard noticeBoard;
	
	@Column(length=10)
	private String writer;
	
	@Column(length=100)
	private String content;
	
	@Column(length=15)
	private String profile;
	
	public NBCommentDto.Read toDto() {
		return new NBCommentDto.Read(nbcno, writer, this.getCreateTime(), content, ZmallConstant.PROFILE_URL +  profile);
	}
}
