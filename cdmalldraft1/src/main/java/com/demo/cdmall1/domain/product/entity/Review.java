package com.demo.cdmall1.domain.product.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.domain.member.entity.*;
import com.demo.cdmall1.web.dto.ReviewDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.Accessors;

// hashCode : 자바에서 객체를 식별하는 번호. 중복되지 않는다는 보장이 없다(알고리즘이 그다지 뛰어나지 않다)
// 롬복의 HashCode는 객체가 가진 필드값을 가지고 해시코드를 생성

@Getter
@Setter
@ToString(exclude = {"product", "member"})
@EqualsAndHashCode(exclude = { "product", "member" }, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@IdClass(ReviewId.class)
@Entity
@Table(name="reviews", indexes = @Index(name = "review_idx_username", columnList = "username"))
public class Review extends BaseCreateTimeEntity {
	@JsonIgnore
	@Id
	@ManyToOne
	@JoinColumn(name = "pno")
	private Product product;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
	@SequenceGenerator(name = "review_seq", sequenceName = "review_seq", allocationSize = 1)
	private Integer rno;

	@ManyToOne
	@JoinColumn(name = "username")
	private Member member;

	private Integer star;

	@Column(length = 200)
	private String content;

	@Column(length = 10)
	private String writer;

	private Integer readCnt;

	private Integer goodCnt;

	private Integer badCnt;

	private Integer commentCnt;

	private Integer attachmentCnt;

	private Integer warnCnt;

	private Boolean isActive;

	@OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, targetEntity = ReviewComment.class)
	@OrderBy(value = "rcno DESC")
	private Set<ReviewComment> reviewComments;

	@OneToMany(mappedBy = "review", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<ReviewAttachment> attachments;


	@PrePersist
	public void init() {
		this.readCnt = 0;
		this.goodCnt = 0;
		this.badCnt = 0;
		this.commentCnt = 0;
		this.attachmentCnt = 0;
		this.warnCnt = 0;
		this.isActive = true;
		if (this.attachments != null)
			this.attachmentCnt = attachments.size();
	}

	public Review increaseReadCnt(String loginId) {
		if (loginId != null && loginId.equals(this.writer) == false)
			this.readCnt++;
		return this;
	}

	public Integer updateCommentCnt() {
		this.commentCnt = this.getReviewComments().size();
		return this.commentCnt;
	}

	public void addAttachment(ReviewAttachment attachment) {
		if (attachments == null)
			this.attachments = new HashSet<ReviewAttachment>();
		// 관계의 주인인 attachments에서도 변경.
		// Board board =.....
		// service에서 board.getAttachments().add(new Attachment(board,.....)); -> board가
		// 저장되지 않는다
		attachment.setReview(this);
		this.attachments.add(attachment);
	}

	public Review update(ReviewDto.Update dto) {
		if (dto.getContent() != null)
			this.content = dto.getContent();
		return this;
	}

	public Integer updateAttachmentCnt(Integer bno2) {
		this.attachmentCnt = attachments.size();
		return this.attachmentCnt;
	}

	public Boolean updateIsActive() {
		if (this.isActive == null) {
			this.isActive = true;
		}
		this.isActive = !(this.isActive);
		return this.isActive;
	}

}
