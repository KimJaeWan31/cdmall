package com.demo.cdmall1.web.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class CKResponse {
	private Integer uploaded;
	private String fileName;
	private String url;
}
