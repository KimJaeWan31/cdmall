package com.demo.cdmall1.domain.product.entity;

import javax.persistence.*;

import com.demo.cdmall1.domain.jpa.*;
import com.demo.cdmall1.domain.member.entity.*;

import lombok.*;

// hashCode : 자바에서 객체를 식별하는 번호. 중복되지 않는다는 보장이 없다(알고리즘이 그다지 뛰어나지 않다)
// 롬복의 HashCode는 객체가 가진 필드값을 가지고 해시코드를 생성

@Getter
@Setter
@ToString(exclude={"product", "member"})
@EqualsAndHashCode(exclude= {"product","member"}, callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(ReviewId.class)
@Entity
@Table(indexes=@Index(name="review_idx_username", columnList="username"))
public class Review  extends BaseCreateTimeEntity {
	@Id
	@ManyToOne
	@JoinColumn(name="pno")
	private Product product;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="review_seq")
	@SequenceGenerator(name="review_seq", sequenceName="review_seq", allocationSize=1)
	private Integer rno;
	
	@ManyToOne
	@JoinColumn(name="username")
	private Member member;
	
	private Integer star;
	
	@Column(length=200)
	private String content;
}
