package com.example.dw.api;


import com.example.dw.domain.dto.admin.AdminNoticeBoardDto;
import com.example.dw.domain.dto.community.FreeBoardListDto;
import com.example.dw.domain.dto.notice.NoticeListDto;
import com.example.dw.domain.form.SearchForm;
import com.example.dw.repository.admin.NoticeBoardRepositoryCustom;
import com.example.dw.repository.freeBoard.FreeBoardRepositoryCustom;
import com.example.dw.service.FreeBoardService;
import com.example.dw.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices/*")
public class NoticeApiController {

    private final NoticeBoardService noticeBoardService;
    /**
     * 공지사항 리스트
     * @param page 변수
     * @param keyword 검색 조건
     * @return 공지사항 목록
     */
    @GetMapping("/notice/{page}")
    public Page<NoticeListDto> noticeBoardListDto(
            @PathVariable("page") int page, String keyword) {

        Pageable pageable = PageRequest.of(page, 10);

        return noticeBoardService.noticeListDtos(pageable, keyword);
    }
}
