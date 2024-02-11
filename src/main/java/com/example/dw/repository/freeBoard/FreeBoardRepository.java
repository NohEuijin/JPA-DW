package com.example.dw.repository.freeBoard;

import com.example.dw.domain.entity.freeBoard.FreeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    //기본키로 검색
    Optional<FreeBoard> findById(Long freeBoardId);

    /**
     * 자유게시판 조회수 증가 쿼리
     * @param freeBoardId 자유게시판 번호
     */
    @Modifying
    @Query("UPDATE FreeBoard SET freeBoardViewCount = freeBoardViewCount + 1 WHERE id = :freeBoardId")
    int increaseViewCount(@Param("freeBoardId") Long freeBoardId);
}
