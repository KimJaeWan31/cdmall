package com.demo.cdmall1.domain.questionboard.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="questionBoard")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(QSAttachmentId.class)
@Entity
@Table(indexes=@Index(name="qsattachment_idx_qbno", columnList="qbno"))
public class QSAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsattachment_seq")
	@SequenceGenerator(name="qsattachment_seq", sequenceName="qsattachment_seq", allocationSize=1)
	private Integer ano;
	
	@JsonIgnore
	@Id									
	@ManyToOne
	@JoinColumn(name="qbno")
	private QuestionBoard questionBoard;
	
	@Column(length=50)
	private String originalFileName;			
	
	@Column(length=80)
	private String saveFileName;			
	
	private Long length;						
	
	private Boolean isImage;		
}
