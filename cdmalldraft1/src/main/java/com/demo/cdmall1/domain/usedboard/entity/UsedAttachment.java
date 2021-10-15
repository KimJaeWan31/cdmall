package com.demo.cdmall1.domain.usedboard.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="usedBoard")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(UsedAttachmentId.class)
@Entity
@Table(indexes=@Index(name="usedattachment_idx_ubno", columnList="ubno"))
public class UsedAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="usedattachment_seq")
	@SequenceGenerator(name="usedattachment_seq", sequenceName="usedattachment_seq", allocationSize=1)
	private Integer ano;
	
	@JsonIgnore
	@Id									
	@ManyToOne
	@JoinColumn(name="ubno")
	private UsedBoard usedBoard;
	
	@Column(length=50)
	private String originalFileName;			
	
	@Column(length=80)
	private String saveFileName;			
	
	private Long length;						
	
	private Boolean isImage;		
}
