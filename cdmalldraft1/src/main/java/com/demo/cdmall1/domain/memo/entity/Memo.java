package com.demo.cdmall1.domain.memo.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.demo.cdmall1.domain.jpa.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = {@Index(name="memo_idx_sender", columnList="sender"), @Index(name="memo_idx_receiver",columnList="receiver")})
@DynamicUpdate
public class Memo extends BaseCreateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="memo_seq")
	@SequenceGenerator(name="memo_seq", sequenceName="memo_seq", allocationSize=1)
	private Integer mno;
	
	@Column(length=50)
	private String title;
	
	@Column(length=200)
	private String content;
	
	@Column(length=10)
	private String sender;
	@Column(length=10)
	private String receiver;
	
	private Boolean isRead;
	
	private Boolean disabledBySender;
	
	private Boolean disabledByReceiver;
	
	@PrePersist
	public void init() {
		this.disabledByReceiver = false;
		this.disabledBySender = false;
		this.isRead = false;
	}
}





