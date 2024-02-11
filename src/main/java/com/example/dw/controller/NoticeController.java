package com.example.dw.controller;


import com.example.dw.service.NoticeBoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeBoardService noticeBoardService;

    /**
     * 공지사항 리스트 페이지
     * @param model 객체
     * @return 공지사항 리스트 이동
     */
    @GetMapping("/noticeList")
    public String notice(Model model){
        model.addAttribute("rankList",noticeBoardService.noticeRank());
        return "/community/noticeList";
    }

    /**
     * 공지사항 상세
     * @param model 객체
     * @return 공지사항 상세 페이지 이동
     */
    @GetMapping("/noticeDetail/{noticeBoardId}")
    public String noticeDetail(@PathVariable("noticeBoardId") Long noticeBoardId, Model model,
                               HttpServletRequest request,
                               HttpServletResponse response){
        // 조회수 증가
        noticeBoardService.increaseViewCount(noticeBoardId, request, response);
        model.addAttribute("detail",noticeBoardService.noticeDetailDto(noticeBoardId));
        return "/community/noticeDetail";
    }
}
