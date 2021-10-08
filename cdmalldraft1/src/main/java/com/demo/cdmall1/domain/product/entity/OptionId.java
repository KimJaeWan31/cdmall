package com.demo.cdmall1.domain.product.entity;

import java.io.*;

// composite-id일 때 @IdClass -> 타입은 데이터베이스에 저장된 타입에 맞추고 이름은 객체에 맞춘다
public class OptionId implements Serializable {
	private Integer product;
	private Integer optionNo;
}
