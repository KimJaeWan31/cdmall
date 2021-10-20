package com.demo.cdmall1.domain.order.entity;

import lombok.*;

@Data
@AllArgsConstructor
public class CartItem {
	private Integer optionNo;
	private String name;
	private String info;
	private Integer price;
	private Integer count;
	private Integer itemPrice;
	public void increase() {
		this.count++;
		this.itemPrice = this.count * this.price;
	}
	public void decrease() {
		if(this.count>1)
			this.count--;
		this.itemPrice = this.count * this.price;
	}
	
	
}
