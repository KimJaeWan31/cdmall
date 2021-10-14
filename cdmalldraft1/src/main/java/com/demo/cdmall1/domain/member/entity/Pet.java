package com.demo.cdmall1.domain.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude="member")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(PetId.class)
@Table(indexes=@Index(name="pet_idx_username", columnList="username"))
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="pet_seq")
	@SequenceGenerator(name="pet_seq", sequenceName="pet_seq", allocationSize=1)
	private Integer petno;
	
	@JsonIgnore
	@Id
	@ManyToOne
	@JoinColumn(name="username")
	private Member member;
	
	@Column(length=10)
	private String petName;
	
	private String profile;
	
	private Integer petAge;
	
	private Boolean gender;
	
	@Column(length=10)
	private String petKind;
	

}
