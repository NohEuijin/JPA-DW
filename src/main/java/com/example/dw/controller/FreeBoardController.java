package com.example.dw.controller;

import com.example.dw.domain.dto.community.FreeBoardResultDetailDto;
import com.example.dw.domain.form.FreeBoardWritingForm;
import com.example.dw.repository.freeBoard.FreeBoardRepositoryCustom;
import com.example.dw.service.FileService;
import com.example.dw.service.FreeBoardCommentService;
import com.example.dw.service.FreeBoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/community/*")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;
    private final FileService fileService;

    /**
     * 자유게시판 리스트 페이지
     * @return 자유게시판 리스트 페이지 이동
     */
    @GetMapping("/freeBoardList")
    public String freeBoard(Model model){
        model.addAttribute("rankList",freeBoardService.findFreeBoardRankList());
        return "/community/freeBoardList";
    }

    /**
     * 자유게시판 글쓰기 페이지
     */
    @GetMapping("/freeBoardWriting")
    public String freeBoardWriting(){
        return "/community/freeBoardWriting";
    }

    @PostMapping("/freeBoardWriting")
    public String write(FreeBoardWritingForm freeBoardWritingForm,
                        @RequestParam("freeBoardImg")List<MultipartFile> files) throws IOException{
        //파일확인
        files.forEach(r-> System.out.println("[파일목록] : "+r.getOriginalFilename()));
        Long id = freeBoardService.register(freeBoardWritingForm);
        fileService.registerDBFreeBoardImg(files, id);
        return "redirect:/community/freeBoardList";
    }

    /**
     * 자유게시판 상세 페이지
     */
    @GetMapping("/freeBoardDetail/{freeBoardId}")
    public String freeBoardDetail(@PathVariable("freeBoardId")Long freeBoardId,
                                  Model model,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        // 조회수 증가
        freeBoardService.increaseViewCount(freeBoardId,request,response);
        model.addAttribute("detail",freeBoardService.freeBoardDetail(freeBoardId));
        return "community/freeBoardDetail";
    }

    /**
     * 자유게시판 수정 페이지
     */
    @GetMapping("/modify/{freeBoardId}")
    public String freeBoardModifyPage(
            @PathVariable("freeBoardId") Long freeBoardId, Model model){
        model.addAttribute("freeBoard", freeBoardService.freeBoardDetail(freeBoardId));
        return "/community/freeBoardModify";
    }

    /**
     * 자유게시판 수정
     */
    @PutMapping("/modify/{freeBoardId}/edit")
    public RedirectView freeBoardModify(
            @PathVariable("freeBoardId") Long freeBoardId,
            FreeBoardWritingForm freeBoardWritingForm,
            @RequestParam("freeBoardImg")List<MultipartFile> files) throws IOException {

        freeBoardWritingForm.setId(freeBoardId);
        freeBoardService.modify(freeBoardWritingForm, files);
        return new RedirectView("/community/freeBoardDetail/{freeBoardId}");
    }

    /**
     * 자유게시판 삭제
     */
    @GetMapping("/delete/{freeBoardId}")
    public RedirectView goodsDelete(@PathVariable("freeBoardId") Long freeBoardId){
        freeBoardService.delete(freeBoardId);
        return new RedirectView("/community/freeBoardList");
    }
}
