package com.demo.cdmall1.domain.usedboard.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class UsedAttachmentId implements Serializable {
	private UsedBoard usedBoard;
	private Integer ano;
}
