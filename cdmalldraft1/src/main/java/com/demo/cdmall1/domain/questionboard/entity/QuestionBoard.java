package com.demo.cdmall1.domain.questionboard.entity;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;

import org.hibernate.annotations.*;

import com.demo.cdmall1.domain.board.entity.*;
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
public class QuestionBoard extends BaseCreateAndUpdateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="questionBoard_seq")
	@SequenceGenerator(name="questionBoard_seq", sequenceName="questionBoard_seq", allocationSize=1)
	private Integer qbno;
	
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
	
	@OneToMany(mappedBy="questionBoard", cascade=CascadeType.REMOVE)
	@OrderBy(value="qbcno DESC")
	private Set<QSComment> comments;
	
	@OneToMany(mappedBy="questionBoard", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<QSAttachment> qsattachments;
	
	@PrePersist
	public void init() {
		this.readCnt = 0;
		this.goodCnt = 0;
		this.badCnt = 0;
		this.commentCnt = 0;
		this.attachmentCnt = 0;
		if(this.qsattachments!=null)
			this.attachmentCnt = qsattachments.size();
	}
	
	public QuestionBoard increaseReadCnt(String loginId) {
		if(loginId!=null && loginId.equals(this.writer)==false)
			this.readCnt++;
		return this;
	}

	public Integer updateCommentCnt() {
		this.commentCnt = this.getComments().size();
		return this.commentCnt;
	}
	
	public QuestionBoard update(QuestionBoardDto.Update dto) {
		if(dto.getTitle()!=null)
			this.title = dto.getTitle();
		if(dto.getContent()!=null)
			this.content = dto.getContent();
		return this;
	}

	public void addQSAttachment(QSAttachment qsattachment) {
		if(qsattachments==null)
			this.qsattachments = new HashSet<QSAttachment>();
		// 관계의 주인인 attachments에서도 변경.
		// Board board =.....
		// service에서 board.getAttachments().add(new Attachment(board,.....)); -> board가 저장되지 않는다
		qsattachment.setQuestionBoard(this);
		this.qsattachments.add(qsattachment);
		
	}
	
	public Integer updateAttachmentCnt(Integer bno2) {
		this.attachmentCnt = qsattachments.size();
		return this.attachmentCnt;
	}




}
