package com.demo.cdmall1.domain.noticeboard.entity;

import org.springframework.data.jpa.repository.*;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Integer>, NoticeBoardCustomRepository{

}
