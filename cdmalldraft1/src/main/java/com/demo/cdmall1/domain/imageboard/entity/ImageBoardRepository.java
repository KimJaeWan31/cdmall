package com.demo.cdmall1.domain.imageboard.entity;

import org.springframework.data.jpa.repository.*;

public interface ImageBoardRepository extends JpaRepository<ImageBoard, Integer>, ImageBoardCustomRepository{


}
