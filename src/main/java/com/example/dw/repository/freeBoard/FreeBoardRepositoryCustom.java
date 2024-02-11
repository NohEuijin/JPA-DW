package com.example.dw.repository.freeBoard;

import com.example.dw.domain.dto.community.*;
import com.example.dw.domain.form.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardRepositoryCustom {

    //자유게시판 리스트 조회(페이징, 검색)
    Page<FreeBoardListDto> findFreeBoardListBySearch(Pageable pageable, SearchForm searchForm);
    //자유게시판 상세페이지 조회
    List<FreeBoardResultDetailDto> findFreeBoardById(Long id);
    //자유게시판 이미지 조회
    List<FreeBoardImgDto> findFreeBoardImgByFreeBoardId(Long freeBoardId);
    //자유게시판 인기글 뽑기
    List<FreeBoardDto> findFreeBoardRankByIdId();

    //마이페이지 내가 작성한 게시판 리스트 뽑기 연습
    Page<MyFreeBoardResultDto> findByUserId(Pageable pageable,Long userId);
}
