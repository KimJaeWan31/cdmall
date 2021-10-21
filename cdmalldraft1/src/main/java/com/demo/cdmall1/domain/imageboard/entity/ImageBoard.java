package com.demo.cdmall1.domain.imageboard.entity;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

import org.hibernate.annotations.*;

import com.demo.cdmall1.domain.jpa.BaseCreateAndUpdateTimeEntity;
import com.demo.cdmall1.web.dto.ImageBoardDto;


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
public class ImageBoard extends BaseCreateAndUpdateTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="imageBoard_seq")
	@SequenceGenerator(name="imageBoard_seq", sequenceName="imageBoard_seq", allocationSize=1)
	private Integer ibno;
	 
	@Column(length=30)
	private String title;

	@Lob
	private String content;
	
	@Column(length=10)
	private String writer;
	
	private Integer goodCnt;
	
	private Integer goodCnlCnt;
	
	private Integer reportCnt;
	
	private Integer ibcommentCnt;
	
	@Column(length=50)
	private String imageFileName;
	
	@OneToMany(mappedBy="imageBoard", cascade=CascadeType.REMOVE)
	private Set<IBComment> ibcomments;
	
	@OneToMany(mappedBy="imageBoard", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<IBAttachment> ibattachments;
	
	@PrePersist
	public void init() {
		this.goodCnt = 0;
		this.goodCnlCnt = 0;
		this.reportCnt=0;
	}
	
	public ImageBoard update(ImageBoardDto.Update dto) {
		if(dto.getTitle()!=null)
			this.title = dto.getTitle();
		if(dto.getContent()!=null)
			this.content = dto.getContent();
		return this;
	}
	
	public Integer updateIBCommentCnt() {
		this.ibcommentCnt = this.getIbcomments().size();
		return this.ibcommentCnt;
	}
	
	public void addAttachment(IBAttachment ibattachment) {
		if(ibattachments==null)
			this.ibattachments = new HashSet<IBAttachment>();
		ibattachment.setImageBoard(this);
		// 관계의 주인인 attachments에서도 변경.
		// Board board =.....
		// service에서 board.getAttachments().add(new Attachment(board,.....)); -> board가 저장되지 않는다
		ibattachments.add(ibattachment);
	}
	
	public Integer checkCnt;
}
