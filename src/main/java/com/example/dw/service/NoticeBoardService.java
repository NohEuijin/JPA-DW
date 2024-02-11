package com.example.dw.service;

import com.example.dw.domain.dto.notice.NoticeDetailDto;
import com.example.dw.domain.dto.notice.NoticeListDto;
import com.example.dw.repository.admin.NoticeBoardRepository;
import com.example.dw.repository.admin.NoticeBoardRepositoryCustom;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;
    private final NoticeBoardRepositoryCustom noticeBoardRepositoryCustom;
    private final static String VIEWCOOKIENAME = "alreadyViewCookie";

    /**
     * 공지사항 리스트
     * @param pageable 페이징 처리
     * @param keyword 검색 요건
     * @return 공지사항 목록
     */
    @Transactional
    public Page<NoticeListDto> noticeListDtos(
            Pageable pageable, String keyword){
        return noticeBoardRepositoryCustom.findNoticeBoardListBySearch(pageable, keyword);
    }

    /**
     * 공지사항 랭킹
     * @return 랭킹 목록
     */
    @Transactional
    public List<NoticeListDto> noticeRank(){
        return noticeBoardRepositoryCustom.findNoticeRankListById();
    }

    /**
     * 공지사항 상세
     * @param noticeBoardId
     * @return
     */
    @Transactional
    public List<NoticeDetailDto> noticeDetailDto(Long noticeBoardId){
        return noticeBoardRepositoryCustom.findNoticeById(noticeBoardId);
    }

    /**
     * 공지사항 조회수
     * 요청에서 쿠키를 가져와서 이미 해당 공지사항을 조회한 경우를 확인
     * 조회한 적이 없는 경우에만 새로운 쿠키를 생성하고 응답에 추가한 후에 조회수를 증가
     * 만약 쿠키가 존재하지 않는 경우에도 마찬가지로 새로운 쿠키를 생성하고 응답에 추가한 후에 조회수를 증가
     * @return 조회수 증가 결과
     */
    @Transactional
    public int increaseViewCount(Long noticeBoardId, HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        boolean checkCookie = false;
        int result = 0;
        if(cookies != null){
            for (Cookie cookie : cookies)
            {
                // 이미 조회를 한 경우 체크
                if (cookie.getName().equals(VIEWCOOKIENAME+noticeBoardId)) checkCookie = true;
            }
            if(!checkCookie){
                Cookie newCookie = createCookieForForNotOverlap(noticeBoardId);
                response.addCookie(newCookie);
                result = noticeBoardRepository.increaseViewCount(noticeBoardId);
            }
        } else {
            Cookie newCookie = createCookieForForNotOverlap(noticeBoardId);
            response.addCookie(newCookie);
            result = noticeBoardRepository.increaseViewCount(noticeBoardId);
        }
        return result;
    }

    /**
     * 조회수 중복 방지를 위한 쿠키 생성 메소드
     * @param postId 변수
     * @return 생성된 쿠키
     */
    private Cookie createCookieForForNotOverlap(Long postId) {
        Cookie cookie = new Cookie(VIEWCOOKIENAME+postId, String.valueOf(postId));
        cookie.setComment("조회수 중복 증가 방지 쿠키");	// 쿠키 용도 설명 기재
        cookie.setMaxAge(getRemainSecondForTommorow()); 	// 하루를 준다.
        cookie.setHttpOnly(true);				// 서버에서만 조작 가능
        return cookie;
    }

    /**
     * 다음 날 정각까지 남은 시간(초)
     * @return 현재 시간부터 내일 정각까지의 시간 간격을 초 단위 계산 값
     */
    private int getRemainSecondForTommorow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
        return (int) now.until(tommorow, ChronoUnit.SECONDS);
    }
}
