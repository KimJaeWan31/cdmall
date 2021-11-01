package com.demo.cdmall1.domain.customercenter.entity;

import java.util.*;


import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;

import org.hibernate.annotations.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.domain.noticeboard.entity.*;
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
public class VocBoard extends BaseCreateAndUpdateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="vocBoard_seq")
	@SequenceGenerator(name="vocBoard_seq", sequenceName="vocBoard_seq", allocationSize=1)
	private Integer vbno;
	
	@Column(length=30)
	private String title;
	
	@Lob
	private String content;
	
	@Column(length=10)
	private String writer;
	
	@Column(length=10)
	private String originalWriter;
	
	private Integer attachmentCnt;
	
	private Integer re_ref;
	
	private Integer re_lev;
	
	private Integer re_seq;
	
	@OneToMany(mappedBy="vocBoard", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<VAttachment> vattachments;
	
	@PrePersist
	public void init() {
		this.attachmentCnt = 0;
		if(this.vattachments!=null)
			this.attachmentCnt = vattachments.size();
	}

	public void addVAttachment(VAttachment vattachment) {
		if(vattachments==null)
			this.vattachments = new HashSet<VAttachment>();
		// 관계의 주인인 attachments에서도 변경.
		// Board board =.....
		// service에서 board.getAttachments().add(new Attachment(board,.....)); -> board가 저장되지 않는다
		vattachment.setVocBoard(this);
		this.vattachments.add(vattachment);
	}
	
	public VocBoard update(VocBoardDto.Update dto) {
		if(dto.getTitle()!=null)
			this.title = dto.getTitle();
		if(dto.getContent()!=null)
			this.content = dto.getContent();
		return this;
	}

}

