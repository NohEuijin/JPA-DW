package com.example.dw.repository.freeBoard;

import com.example.dw.domain.dto.community.FreeBoardCommentDto;

import java.util.List;

public interface FreeBoardCommentCustom {

    //자유게시판 댓글 목록
    List<FreeBoardCommentDto> findFreeBoardCommentsByFreeBoardId(Long freeBoardId);
}
