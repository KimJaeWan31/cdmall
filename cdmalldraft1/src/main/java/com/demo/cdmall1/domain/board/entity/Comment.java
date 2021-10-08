package com.demo.cdmall1.domain.board.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="board")		// ToString을 만들 때 board빼고 만들어라
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
// 테이블의 이름을 변경하고 bno에 대한 인덱스를 지정(인덱스를 만들 때는 board가 아니라 bno)
@Table(name="comments", indexes=@Index(name="comment_idx_bno", columnList="bno"))
public class Comment extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="comment_seq")
	@SequenceGenerator(name="comment_seq", sequenceName="comment_seq", allocationSize=1)
	private Integer cno;   
	
	// board-comment에 관계를 설정. board를 읽으면 comment가 읽히고, comment를 읽어도 board가 읽힌다
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="bno")
	private Board board;
	
	@Column(length=10)
	private String writer;
	
	@Column(length=100)
	private String content;
	
	@Column(length=15)
	private String profile;
	
	public CommentDto.Read toDto() {
		return new CommentDto.Read(cno, writer, this.getCreateTime(), content, ZmallConstant.PROFILE_URL +  profile);
	}
}
