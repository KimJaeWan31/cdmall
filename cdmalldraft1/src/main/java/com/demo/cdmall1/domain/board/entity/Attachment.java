package com.demo.cdmall1.domain.board.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 부모 테이블과 자식 테이블을 함께 저장하려면 반드시 식별 관계여야 한다
@IdClass(AttachmentId.class)			
@Entity
@Table(indexes=@Index(name="attachment_idx_bno", columnList="bno"))
public class Attachment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="attachment_seq")
	@SequenceGenerator(name="attachment_seq", sequenceName="attachment_seq", allocationSize=1)
	private Integer ano;
	
	@JsonIgnore
	@Id									
	@ManyToOne
	@JoinColumn(name="bno")
	private Board board;
	
	@Column(length=50)
	private String originalFileName;			// 원본 파일명 : aaa.jpg
	
	@Column(length=80)
	private String saveFileName;				// 저장 파일명 : 현재시간-aaa.jpg
	
	private Long length;						
	
	private Boolean isImage;					// 이미지면 보여주고 이미지가 아니면 다운로드
}
