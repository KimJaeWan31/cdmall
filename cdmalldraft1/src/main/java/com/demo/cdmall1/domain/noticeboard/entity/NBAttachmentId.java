package com.demo.cdmall1.domain.noticeboard.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class NBAttachmentId implements Serializable {
	private NoticeBoard noticeBoard;
	private Integer ano;
}
