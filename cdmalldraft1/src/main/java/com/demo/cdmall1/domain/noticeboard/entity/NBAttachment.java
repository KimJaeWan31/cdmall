package com.demo.cdmall1.domain.noticeboard.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude="noticeBoard")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(NBAttachmentId.class)
@Entity
@Table(indexes=@Index(name="nbattachment_idx_nbno", columnList="nbno"))
public class NBAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="nbattachment_seq")
	@SequenceGenerator(name="nbattachment_seq", sequenceName="nbattachment_seq", allocationSize=1)
	private Integer ano;
	
	@JsonIgnore
	@Id									
	@ManyToOne
	@JoinColumn(name="nbno")
	private NoticeBoard noticeBoard;
	
	@Column(length=50)
	private String originalFileName;			
	
	@Column(length=80)
	private String saveFileName;			
	
	private Long length;						
	
	private Boolean isImage;		
}
