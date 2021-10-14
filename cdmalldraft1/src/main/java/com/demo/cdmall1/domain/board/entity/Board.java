package com.demo.cdmall1.domain.board.entity;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;

import org.hibernate.annotations.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;
import lombok.experimental.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
@Entity
@DynamicUpdate
public class Board extends BaseCreateAndUpdateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="board_seq")
	@SequenceGenerator(name="board_seq", sequenceName="board_seq", allocationSize=1)
	private Integer bno;
	
	@Column(length=30)
	private String title;
	
	@Lob
	private String content;
	
	@Column(length=10)
	private String writer;
	
	private Integer readCnt;
	
	private Integer goodCnt;
	
	private Integer badCnt;
	
	private Integer commentCnt;
	
	private Integer attachmentCnt;
	
	private String category;
	
	private Integer warnCnt;
	
	// DB에서는 외래키를 가진 쪽이 자식 테이블, JPA에서는 외래키를 가진 쪽이 관계의 주인(관계의 주인만 외래키 필드를 변경할 수 있다)
	// 관계의 주인이 아닌 쪽은 mappedBy를 이용해 외래키를 지정한다(주인이 가진 외래키 엔티티의 이름)
	@OneToMany(mappedBy="board", cascade=CascadeType.REMOVE)
	@OrderBy(value="cno DESC")
	private Set<Comment> comments;
	
	// OneToMany는 기본 Lazy 로딩, ManyToOne은 기본 Eager 로딩
	// Lazy는 board 따로, attachment 따로, comment 따로 읽어온다. 퀴리가 3번 나간다
	// 	만약 comment에서 member를 참고한다고 해보자. 그러면 3번글을 읽으라고 하면
	//		select * from board where bno=3;			
	//		select * from comments where bno=3; -> 결과가 100개라면
	//		각 comment에 대해서 member를 읽으러가게 된다(쿼리가 100번 발생) => N+1 문제
	//		N+1이 발생하지 않으면 Lazy로 가자
	// Eager는 left outer join으로 한번에 읽어온다
	@OneToMany(mappedBy="board", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<Attachment> attachments;
	
	@PrePersist
	public void init() {
		this.readCnt = 0;
		this.goodCnt = 0;
		this.badCnt = 0;
		this.commentCnt = 0;
		this.attachmentCnt = 0;
		this.warnCnt=0;
		if(this.attachments!=null)
			this.attachmentCnt = attachments.size();
	}

	public Board increaseReadCnt(String loginId) {
		if(loginId!=null && loginId.equals(this.writer)==false)
			this.readCnt++;
		return this;
	}

	public Integer updateCommentCnt() {
		this.commentCnt = this.getComments().size();
		return this.commentCnt;
	}

	public void addAttachment(Attachment attachment) {
		if(attachments==null)
			this.attachments = new HashSet<Attachment>();
		// 관계의 주인인 attachments에서도 변경.
		// Board board =.....
		// service에서 board.getAttachments().add(new Attachment(board,.....)); -> board가 저장되지 않는다
		attachment.setBoard(this);
		this.attachments.add(attachment);
	}
	
	public Board update(BoardDto.Update dto) {
		if(dto.getTitle()!=null)
			this.title = dto.getTitle();
		if(dto.getContent()!=null)
			this.content = dto.getContent();
		return this;
	}

	public Integer updateAttachmentCnt(Integer bno2) {
		this.attachmentCnt = attachments.size();
		return this.attachmentCnt;
	}
}




