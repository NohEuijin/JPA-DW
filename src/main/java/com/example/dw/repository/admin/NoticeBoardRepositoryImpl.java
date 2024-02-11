package com.example.dw.repository.admin;


import com.example.dw.domain.dto.admin.AdminNoticeBoardDto;
import com.example.dw.domain.dto.admin.QAdminNoticeBoardDto;
import com.example.dw.domain.dto.notice.NoticeDetailDto;
import com.example.dw.domain.dto.notice.NoticeListDto;
import com.example.dw.domain.dto.notice.QNoticeDetailDto;
import com.example.dw.domain.dto.notice.QNoticeListDto;
import com.example.dw.domain.form.SearchForm;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.dw.domain.entity.admin.QNoticeBoard.noticeBoard;
import static com.example.dw.domain.entity.freeBoard.QFreeBoard.freeBoard;
import static com.example.dw.domain.entity.freeBoard.QFreeBoardImg.freeBoardImg;
import static com.example.dw.domain.entity.user.QUsers.users;

@Repository
@RequiredArgsConstructor
public class NoticeBoardRepositoryImpl implements NoticeBoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminNoticeBoardDto> findNoticeListBySearch(Pageable pageable, SearchForm searchForm) {

        List<AdminNoticeBoardDto> content = getNoticeBoardList(pageable, searchForm);
        Long count = getCount(searchForm);

        return new PageImpl<>(content, pageable, count);
    }

    private Long getCount(SearchForm searchForm){

        Long count = jpaQueryFactory
                .select(noticeBoard.count())
                .from(noticeBoard)
                .where(
                        cateKeywordEq(searchForm)
                )
                .fetchOne();
        return count;
    }

    private List<AdminNoticeBoardDto> getNoticeBoardList(Pageable pageable, SearchForm searchForm){

        List<AdminNoticeBoardDto> contents = jpaQueryFactory
                .select(new QAdminNoticeBoardDto(
                        noticeBoard.id,
                        noticeBoard.noticeBoardTitle,
                        noticeBoard.noticeBoardContent,
                        noticeBoard.noticeBoardViewCount,
                        noticeBoard.noticeBoardRd,
                        noticeBoard.noticeBoardMd
                ))
                .from(noticeBoard)
                .where(
                        cateKeywordEq(searchForm)
                )
                .orderBy(noticeBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();



        return contents;
    }


    private BooleanExpression cateKeywordEq(SearchForm searchForm){
        if(StringUtils.hasText(searchForm.getCate())&&StringUtils.hasText(searchForm.getKeyword())){

            switch (searchForm.getCate()){

                case "noticeBoardTitle" :
                    return noticeBoard.noticeBoardTitle.containsIgnoreCase(searchForm.getKeyword());
                case "noticeBoardContent" :
                    return noticeBoard.noticeBoardContent.containsIgnoreCase(searchForm.getKeyword());
                default:
                    break;
            }

        }
        return noticeBoard.id.isNotNull();
    }

    /**
     * 여기서 부터 Notice Board
     */

    /**
     * 공지사항 제목에 대한 조건식을 생성
     * @param keyword 검색
     * @return 검색 키워드가 비어 있지 않으면 공지사항 제목이 해당 키워드를 포함,
     * 대소문자를 무시하는 조건식을 생성하고, 그렇지 않으면 null
     */
    private BooleanExpression noticeBoardTitleEq(String keyword){
        return StringUtils.hasText(keyword) ? noticeBoard.noticeBoardTitle.containsIgnoreCase(keyword) : null;
    }

    /**
     * 공지사항 갯수 조회
     * @param keyword 검색
     * @return 공지사항 갯수
     */
    private Long getCount(String keyword) {
        Long count = jpaQueryFactory
                .select(noticeBoard.count())
                .from(noticeBoard)
                .where(noticeBoardTitleEq(keyword))
                .fetchOne();
        return count;
    }

    /**
     * 공지사항 목록 조회
     * @param pageable 페이징
     * @param keyword 검색
     * @return 조회된 결과, PageImpl 객체로
     */
    @Override
    public Page<NoticeListDto> findNoticeBoardListBySearch(Pageable pageable, String keyword) {

        //검색
        BooleanExpression keywordTitle = noticeBoardTitleEq(keyword);

        List<NoticeListDto> contents = jpaQueryFactory
                .select(new QNoticeListDto(
                        noticeBoard.id,
                        noticeBoard.noticeBoardTitle,
                        noticeBoard.noticeBoardContent,
                        noticeBoard.noticeBoardViewCount,
                        noticeBoard.noticeBoardRd,
                        noticeBoard.noticeBoardMd
                ))
                .from(noticeBoard)
                .where(keywordTitle)
                .orderBy(noticeBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //페이징을 위한 전체 데이터 수 조회
        Long count = getCount(keyword);
        System.out.println(contents.toString()+"리스트");

        return new PageImpl<>(contents, pageable, count);
    }

    /**
     * 공지사항 상세 조회
     * @param id 변수(공지사항을 선택)
     * @return 조회된 공지사항 정보를 리스트
     */
    @Override
    public List<NoticeDetailDto> findNoticeById(Long id) {
        System.out.println(id + "공지사항 아이디 조회");
        List<NoticeDetailDto> noticeDetailDtos =
         jpaQueryFactory
                .select(new QNoticeDetailDto(
                noticeBoard.id,
                noticeBoard.noticeBoardTitle,
                noticeBoard.noticeBoardContent,
                noticeBoard.noticeBoardViewCount,
                noticeBoard.noticeBoardRd,
                noticeBoard.noticeBoardMd
        ))
                .from(noticeBoard)
                .where(noticeBoard.id.eq(id))
                .fetch();

        noticeDetailDtos.forEach(r -> System.out.println(r.toString()+"noticeDetailDtos 조회"));

        return noticeDetailDtos;
    }

    /**
     * 공지사항 자주 찾는 글 top3 조회
     * @return 조회수 기준, 내림차순 정렬, 상위 3개 결과
     */
    @Override
    public List<NoticeListDto> findNoticeRankListById() {
        return jpaQueryFactory.select(new QNoticeListDto(
                noticeBoard.id,
                noticeBoard.noticeBoardTitle,
                noticeBoard.noticeBoardContent,
                noticeBoard.noticeBoardViewCount,
                noticeBoard.noticeBoardRd,
                noticeBoard.noticeBoardMd
        ))
                .from(noticeBoard)
                .orderBy(noticeBoard.noticeBoardViewCount.desc())
                .limit(3)
                .fetch();
    }
}

