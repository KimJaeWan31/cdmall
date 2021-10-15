package com.demo.cdmall1.domain.usedboard.entity;

import org.springframework.data.jpa.repository.*;

public interface UsedBoardRepository extends JpaRepository<UsedBoard, Integer>, UsedBoardCustomRepository{

}
