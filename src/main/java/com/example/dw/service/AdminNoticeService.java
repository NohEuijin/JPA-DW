package com.example.dw.service;


import com.example.dw.domain.dto.admin.AdminDto;
import com.example.dw.domain.dto.admin.AdminNoticeBoardDto;
import com.example.dw.domain.dto.notice.NoticeListDto;
import com.example.dw.domain.entity.admin.Admin;
import com.example.dw.domain.entity.admin.FaqBoard;
import com.example.dw.domain.entity.admin.NoticeBoard;
import com.example.dw.domain.form.FaqBoardForm;
import com.example.dw.domain.form.NoticeBoardForm;
import com.example.dw.domain.form.SearchForm;
import com.example.dw.repository.admin.AdminRepository;
import com.example.dw.repository.admin.FaqBoardRepository;
import com.example.dw.repository.admin.NoticeBoardRepository;
import com.example.dw.repository.admin.NoticeBoardRepositoryCustom;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminNoticeService {

    private final AdminRepository adminRepository;
    private final NoticeBoardRepository noticeBoardRepository;
    private final FaqBoardRepository faqBoardRepository;
    private final NoticeBoardRepositoryCustom noticeBoardRepositoryCustom;

    //관리자 로그인
    @Transactional
    public AdminDto adminLogin(String adminAccount, String adminPassword){

       Admin admin= adminRepository.findByAdminAccountAndAdminPassword(adminAccount, adminPassword).get();

       AdminDto adminDto = new AdminDto(admin.getId(), admin.getAdminAccount(), admin.getAdminPassword());

        return adminDto;

    }
    //관리자 로그아웃
    @Transactional
    public void adminLogout(HttpSession session){
        session.invalidate();
    }


    //공지사항 작성
    @Transactional
    public Long register(NoticeBoardForm noticeBoardForm){

         NoticeBoard noticeBoard = noticeBoardRepository.save(noticeBoardForm.toEntity());

         return noticeBoard.getId();

    }

    //Faq 작성
    @Transactional
    public Long faqRegister(FaqBoardForm faqBoardForm){

        FaqBoard faqBoard = faqBoardRepository.save(faqBoardForm.toEntity());

        return faqBoard.getId();

    }



    //Faq 상세 보기
    @Transactional
    public FaqBoard faqDetail(Long faqBoardId){

        FaqBoard detail = faqBoardRepository.findById(faqBoardId).get();
        return Optional.ofNullable(detail).orElseThrow(()->{throw new IllegalArgumentException("존재하지 않음");});
    }

    //faq 수정
    @Transactional
    public FaqBoard faqModify(FaqBoardForm faqBoardForm, Long id) {

        FaqBoard faqBoard = faqBoardRepository.findById(id).get();
        faqBoard.update(faqBoardForm);

        return Optional.ofNullable(faqBoard).orElseThrow(()->{throw new IllegalArgumentException("존재하지 않음");});
    }

    //faq 삭제
    @Transactional
    public void faqDelete(Long id){

        if (id == null) {
            throw new IllegalArgumentException("faq 게시번호 누락");
        }

        faqBoardRepository.deleteById(id);

    }



    //공지사항 상세 보기
    @Transactional
    public NoticeBoard noticeDetail(Long id){
        NoticeBoard detail = noticeBoardRepository.findById(id).get();

        return Optional.ofNullable(detail).orElseThrow(()->{
            throw new IllegalArgumentException("존재하지 않음");
        });
    }

    //공지사항 수정
    @Transactional
    public NoticeBoard noticeModify(NoticeBoardForm noticeBoardForm,Long id){

        //영속상태로
        NoticeBoard noticeBoard = noticeBoardRepository.findById(id).get();

        //변경감지
        noticeBoard.update(noticeBoardForm);

        return Optional.ofNullable(noticeBoard).orElseThrow(()->{
            throw new IllegalArgumentException("존재하지 않음");
        });
    }

    //공지사항 삭제
    @Transactional
    public void noticeDelete(Long id ){

        if (id == null) {

            throw new IllegalArgumentException("존재하지 않는 공지사항 게시 번호");
        }

        noticeBoardRepository.deleteById(id);
    }



}
