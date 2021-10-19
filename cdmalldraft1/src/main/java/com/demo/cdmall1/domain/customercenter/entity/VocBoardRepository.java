package com.demo.cdmall1.domain.customercenter.entity;

import org.springframework.data.jpa.repository.*;

public interface VocBoardRepository extends JpaRepository<VocBoard, Integer>, VocBoardCustomRepository {

}
