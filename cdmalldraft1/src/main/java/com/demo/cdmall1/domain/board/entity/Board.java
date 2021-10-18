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
	
	private Boolean isActive;
	
	@OneToMany(mappedBy="board", cascade=CascadeType.REMOVE)
	@OrderBy(value="cno DESC")
	private Set<Comment> comments;
	
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
		this.isActive=true;
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
	
	public Boolean updateIsActive() {
		
		if(this.isActive == null) {
			this.isActive = true;
		}this.isActive = !(this.isActive);
		return this.isActive;
	}
}




