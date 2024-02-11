package com.example.dw.controller;

import com.example.dw.domain.dto.admin.AdminDto;
import com.example.dw.domain.entity.admin.FaqBoard;
import com.example.dw.domain.entity.admin.NoticeBoard;
import com.example.dw.domain.form.FaqBoardForm;
import com.example.dw.domain.form.NoticeBoardForm;
import com.example.dw.service.AdminNoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/*")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;


    //관리자 로그인 페이지
    @GetMapping("/enterLogin")
    public String adminLoginPage(){
        return "/admin/adminLogin";
    }

    //관리자 로그인
    @PostMapping("/login")
    public RedirectView adminLogin(String adminAccount, String adminPassword, HttpServletRequest req, RedirectAttributes redirectAttributes){
        AdminDto admin = null;
        System.out.println(adminAccount);
        System.out.println(adminPassword);

        try {
             admin = adminNoticeService.adminLogin(adminAccount, adminPassword);

        }catch(Exception e){
            System.out.println("로그인 실풰");
            redirectAttributes.addFlashAttribute("isLogin","0");
            return new RedirectView("/admin/enterLogin");

        }
        System.out.println("로그인 성겅");

        req.getSession().setAttribute("adminId", admin.getId());
        return new RedirectView("/admin/userStatus");

    }



    //관리자 로그아웃
    @PostMapping("/logout")
    public RedirectView adminLogout(HttpSession session){

        session.invalidate();

        return new RedirectView("/admin/enterLogin");
    }
    
    
    
    @GetMapping("/FBList")
    public String FreeBoardList(){
        return "/admin/adminFreeList";
    }
    
    //공지리스트
    @GetMapping("/noticeList")
        public String noticeList(){
            return "/admin/adminNoticeList";
    }

    //공지사항 작성 페이지 이동
    @GetMapping("/noticeWrite")
    public String noticeWritePage(){
        return "/admin/adminNoticeReg";
    }


    //공지사항 작성
    @PostMapping("/noticeWrite")
    public String noticeWrite(NoticeBoardForm noticeBoardForm){

        adminNoticeService.register(noticeBoardForm);
        return "/admin/adminNoticeList";

    }

    //Faq작성 페이지 이동
    @GetMapping("/faqWrite")
    public String faqWritePage(){
        return "/admin/adminFaqReg";
    }

    //Faq 작성
    @PostMapping("/faqWrite")
    public RedirectView faqWrite(FaqBoardForm faqBoardForm){

        System.out.println(faqBoardForm.getId());



        adminNoticeService.faqRegister(faqBoardForm);

        return new RedirectView("/admin/noticeList");
    }

    //faq 수정 페이지 이동
    @GetMapping("/faqModifyPage/{faqBoardId}")
    public String faqModifyPage(@PathVariable("faqBoardId") Long faqBoardId, Model model){

        FaqBoard faqBoard = adminNoticeService.faqDetail(faqBoardId);

        FaqBoardForm faqBoardForm = new FaqBoardForm();
        faqBoardForm.setId(faqBoardId);
        faqBoardForm.setFaqBoardTitle(faqBoard.getFaqBoardTitle());
        faqBoardForm.setFaqBoardContent(faqBoard.getFaqBoardContent());

        model.addAttribute("detail", faqBoardForm);

        return "/admin/adminFaqModify";
    }

    //faq 수정 완료
    @PutMapping("/faqModify/{id}/edit")
    public RedirectView faqModify(
            @PathVariable("id") Long faqBoardId,
            FaqBoardForm faqBoardForm){
        System.out.println(faqBoardId+"===========================");

        adminNoticeService.faqModify(faqBoardForm, faqBoardId);


        return new RedirectView("/admin/noticeList");
    }
    //추후 삭제는 비동기로 바꿀까 생각중
    //faq 삭제
    @GetMapping("/faqDelete/{id}")
        public RedirectView faqDelete(
                @PathVariable("id") Long faqBoardId){

        
        
        adminNoticeService.faqDelete(faqBoardId);

        return new RedirectView("/admin/noticeList");

    }

    //공지사항 수정 페이지
    @GetMapping("/noticeModifyPage/{noticeBoardId}")
    public String noticeModifyPage(@PathVariable("noticeBoardId")Long noticeBoardId,
                                  Model model){

        NoticeBoard noticeBoard = adminNoticeService.noticeDetail(noticeBoardId);
        NoticeBoardForm noticeBoardForm = new NoticeBoardForm();
        noticeBoardForm.setId(noticeBoardId);
        noticeBoardForm.setNoticeBoardTitle(noticeBoard.getNoticeBoardTitle());
        noticeBoardForm.setNoticeBoardContent(noticeBoard.getNoticeBoardContent());

        model.addAttribute("detail", noticeBoardForm);
        return "/admin/adminNoticeModify";
    }
    
    //공지사항 수정 완료
    @PutMapping("/noticeModify/{id}/edit")
    public RedirectView noticeModify(@PathVariable("id") Long id,
                                     NoticeBoardForm noticeBoardForm){

        adminNoticeService.noticeModify(noticeBoardForm,id);

        return new RedirectView("/admin/noticeList");

    }


    //추후 삭제는 비동기로 바꿀까 생각중
    //공지사항 삭제
    @GetMapping("/noticeDelete/{id}")
    public RedirectView noticeDelete(@PathVariable("id") Long id){

        adminNoticeService.noticeDelete(id);

        return new RedirectView("/admin/noticeList");

    }


}
