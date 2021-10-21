package com.demo.cdmall1.domain.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
@Entity
public class Seller {
	@Id
	@Column(length=10)
	private String username;
	
	private String busniessNo;
	
	private String state;
	
	
	private Boolean isActive;
	
}
