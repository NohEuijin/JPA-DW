package com.example.dw.repository.freeBoard;

import com.example.dw.domain.entity.freeBoard.FreeBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {

}
