package com.demo.cdmall1.domain.product.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="product")
@EqualsAndHashCode(exclude="product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(OptionId.class)
@Entity
@Table(name="options", indexes = @Index(name="product_idx_pno", columnList="pno"))
public class Option {
	@Id
	@ManyToOne
	@JoinColumn(name="pno")
	@JsonIgnore
	private Product product;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="option_seq")
	@SequenceGenerator(name="option_seq", sequenceName="option_seq", allocationSize=1)
	private Integer optionNo;
	
	@Column(length=30)
	private String name;		

	@Column(length = 100)
	private String info;
	
	private Integer price;
	
	@PrePersist
	public void init() {
		if(this.price==null)
			this.price=0;
	}
	
}



