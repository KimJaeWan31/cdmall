package com.demo.cdmall1.domain.imageboard.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.BaseCreateTimeEntity;
import com.demo.cdmall1.util.ZmallConstant;
import com.demo.cdmall1.web.dto.IBCommentDto;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="imageBoard")		// ToString을 만들 때 board빼고 만들어라
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
// 테이블의 이름을 변경하고 bno에 대한 인덱스를 지정(인덱스를 만들 때는 board가 아니라 bno)
@Table(name="ibcomments", indexes=@Index(name="ibcomment_idx_ibno", columnList="ibno"))
public class IBComment extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ibcomment_seq")
	@SequenceGenerator(name="ibcomment_seq", sequenceName="ibcomment_seq", allocationSize=1)
	private Integer ibcno;   
	
	// board-comment에 관계를 설정. board를 읽으면 comment가 읽히고, comment를 읽어도 board가 읽힌다
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ibno")
	private ImageBoard imageBoard;
	
	@Column(length=10)
	private String writer;
	
	@Column(length=100)
	private String content;
	
	@Column(length=15)
	private String profile;
	
	public IBCommentDto.Read toDto() {
		return new IBCommentDto.Read(ibcno, writer, this.getCreateTime(), content, ZmallConstant.PROFILE_URL +  profile);
	}
}
