package com.demo.cdmall1.domain.board.entity;

import org.springframework.data.jpa.repository.*;

public interface BoardRepository extends JpaRepository<Board, Integer>, BoardCustomRepository {

}
