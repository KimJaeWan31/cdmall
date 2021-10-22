package com.demo.cdmall1.domain.usedboard.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.DynamicUpdate;

import com.demo.cdmall1.domain.jpa.BaseCreateAndUpdateTimeEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@Entity
@DynamicUpdate
public class UsedBoard extends BaseCreateAndUpdateTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usedBoard_seq")
	@SequenceGenerator(name="usedBoard_seq", sequenceName = "usedBoard_seq", allocationSize = 1)
	private Integer ubno;
	
	@Column(length = 30)
	private String title;
	
	@Lob
	private String content;
	
	@Column(length = 10)
	private String writer;
	
	private Integer readCnt;
	
	private Integer goodCnt;
	
	private Integer badCnt;
	
	private Integer commentCnt;
	
	private Integer attachmentCnt;
	
	private Integer warnCnt;
	
	@OneToMany(mappedBy="usedBoard", cascade=CascadeType.REMOVE)
	@OrderBy(value="ubcno DESC")
	private Set<UsedComment> comments;
	
	@OneToMany(mappedBy="usedBoard", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<UsedAttachment> usedattachments;
	
	@PrePersist
	public void init() {
		this.readCnt = 0;
		this.goodCnt = 0;
		this.badCnt = 0;
		this.commentCnt = 0;
		this.attachmentCnt = 0;
		this.warnCnt=0;
		if(this.usedattachments!=null)
			this.attachmentCnt = usedattachments.size();
	}
	
	public UsedBoard increaseReadCnt(String loginId) {
		if(loginId!=null && loginId.equals(this.writer)==false)
			this.readCnt++;
		return this;
	}

	public Integer updateCommentCnt() {
		this.commentCnt = this.getComments().size();
		return this.commentCnt;
	}
	
	public UsedBoard update(UsedBoardDto.Update dto) {
		if(dto.getTitle()!=null)
			this.title = dto.getTitle();
		if(dto.getContent()!=null)
			this.content = dto.getContent();
		return this;
	}

	public void addUsedAttachment(UsedAttachment usedattachment) {
		if(usedattachments==null)
			this.usedattachments = new HashSet<UsedAttachment>();
		// 관계의 주인인 attachments에서도 변경.
		// Board board =.....
		// service에서 board.getAttachments().add(new Attachment(board,.....)); -> board가 저장되지 않는다
		usedattachment.setUsedBoard(this);
		this.usedattachments.add(usedattachment);
		
	}
	
	public Integer updateAttachmentCnt(Integer bno2) {
		this.attachmentCnt = usedattachments.size();
		return this.attachmentCnt;
	}



	
}
