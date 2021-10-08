package com.demo.cdmall1.domain.jpa;

import java.time.*;

import javax.persistence.*;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

// JPA audit 기능을 이용해 작성시간을 추적할 부모 클래스 -> 설정에 가서 audit를 키자

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCreateTimeEntity {
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@CreatedDate
	private LocalDateTime createTime;
}
