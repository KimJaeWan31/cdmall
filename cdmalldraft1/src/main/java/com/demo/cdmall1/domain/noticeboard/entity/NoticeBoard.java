package com.demo.cdmall1.domain.noticeboard.entity;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

import org.hibernate.annotations.*;

import com.demo.cdmall1.domain.jpa.BaseCreateAndUpdateTimeEntity;
import com.demo.cdmall1.web.dto.NoticeBoardDto;


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
public class NoticeBoard extends BaseCreateAndUpdateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="noticeBoard_seq")
	@SequenceGenerator(name="noticeBoard_seq", sequenceName="noticeBoard_seq", allocationSize=1)
	private Integer nbno;
	
	@Column(length=30)
	private String title;

	@Lob
	private String content;
	
	@Column(length=10)
	private String writer;
	
	private Integer readCnt;
	
	@OneToMany(mappedBy="noticeBoard", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<NBAttachment> nbattachments;
	
	@PrePersist
	public void init() {
		this.readCnt = 0;
	}
	
	public NoticeBoard increaseReadCnt(String loginId) {
		if(loginId!=null && loginId.equals(this.writer)==false)
			this.readCnt++;
		return this;
	}
	
	public void addAttachment(NBAttachment nbattachment) {
		if(nbattachments==null)
			this.nbattachments = new HashSet<NBAttachment>();
		// 관계의 주인인 attachments에서도 변경.
		// Board board =.....
		// service에서 board.getAttachments().add(new Attachment(board,.....)); -> board가 저장되지 않는다
		nbattachment.setNoticeBoard(this);
		nbattachments.add(nbattachment);
	}
	
	public NoticeBoard update(NoticeBoardDto.Update dto) {
		if(dto.getTitle()!=null)
			this.title = dto.getTitle();
		if(dto.getContent()!=null)
			this.content = dto.getContent();
		return this;
	}
}
