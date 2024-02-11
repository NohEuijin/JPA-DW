package com.example.dw.repository.freeBoard;

import com.example.dw.domain.dto.admin.UserFileDto;
import com.example.dw.domain.dto.community.*;
import com.example.dw.domain.form.SearchForm;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.dw.domain.entity.freeBoard.QFreeBoard.freeBoard;
import static com.example.dw.domain.entity.freeBoard.QFreeBoardImg.freeBoardImg;
import static com.example.dw.domain.entity.user.QUsers.users;
import static com.example.dw.domain.entity.user.QUserFile.userFile;
import static com.example.dw.domain.entity.freeBoard.QFreeBoardComment.freeBoardComment;


import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class FreeBoardRepositoryImpl implements FreeBoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    /**
     * 자유게시판 상세 정보 목록
     */
    @Override
    public List<FreeBoardResultDetailDto> findFreeBoardById(Long id) {
        List<FreeBoardDetailDto> list = getFreeBoardDetail(id);

        // 상세정보, 이미지 정보 매핑 후 매핑 정보 기반으로 객체 생성
        List<FreeBoardResultDetailDto> result = list.stream().collect(groupingBy(r -> new FreeBoardResultDetailDto(
                        r.getId(),r.getFreeBoardTitle(),r.getFreeBoardContent(),r.getFreeBoardRd(),r.getFreeBoardMd(),
                        r.getFreeBoardViewCount(),r.getUserId(),r.getUserAccount(),r.getUserNickName(),r.getUserFileId(),
                        r.getRoute(),r.getName(),r.getUuid()),
                Collectors.mapping(r -> new FreeBoardImgDto(r.getFreeBoardImgId(),r.getFreeBoardImgRoute(),r.getFreeBoardImgName(),
                        r.getFreeBoardImgUuid(),r.getId()),toList())
        )).entrySet().stream().map(s -> new FreeBoardResultDetailDto(
                s.getKey().getId(), s.getKey().getFreeBoardTitle(),s.getKey().getFreeBoardContent(),
                s.getKey().getFreeBoardRd(), s.getKey().getFreeBoardMd(), s.getKey().getUserId(),
                s.getKey().getFreeBoardViewCount(), s.getKey().getUserAccount(),s.getKey().getUserNickName(),
                s.getKey().getUserFileId(),s.getKey().getRoute(),s.getKey().getName(),s.getKey().getUuid(),
                s.getValue()))
                .collect(Collectors.toList());

        System.out.println(result.toString()+"FreeBoardResultDetailDto 조회!!");
        return result;
    }

    /**
     * 자유게시판 상세 조회
     * @param id freeBoardId
     * @return 조회 값
     */
    private List<FreeBoardDetailDto> getFreeBoardDetail(Long id) {
        System.out.println(id + "의 아이디 조회!");
        List<FreeBoardDetailDto> freeBoardDetailDtos = jpaQueryFactory
                .select(new QFreeBoardDetailDto(
                        freeBoard.id,
                        freeBoard.freeBoardTitle,
                        freeBoard.freeBoardContent,
                        freeBoard.freeBoardRd,
                        freeBoard.freeBoardMd,
                        freeBoard.freeBoardViewCount,
                        freeBoardImg.id,
                        freeBoardImg.freeBoardImgRoute,
                        freeBoardImg.freeBoardImgName,
                        freeBoardImg.freeBoardImgUuid,
                        freeBoard.users.id,
                        freeBoard.users.userAccount,
                        freeBoard.users.userNickName,
                        userFile.id.coalesce(0L),
                        userFile.route.coalesce("0"),
                        userFile.name.coalesce("0"),
                        userFile.uuid.coalesce("0")
                ))
                .from(freeBoard)
                .leftJoin(freeBoardImg).on(freeBoard.id.eq(freeBoardImg.freeBoard.id))
                .leftJoin(freeBoard.users).on(freeBoard.users.id.eq(users.id))
                .leftJoin(users.userFile,userFile)
                .where(freeBoard.id.eq(id))
                .fetch();

        freeBoardDetailDtos.forEach(r -> System.out.println(r.toString()+"freeBoardDetailDtos 조회"));

        return freeBoardDetailDtos;
    }

    /**
     * 자유게시판 갯수 조회
     * @param keyword 검색
     * @return 갯수
     */
    private Long getCount(String keyword) {
        Long count = jpaQueryFactory
                .select(freeBoard.count())
                .from(freeBoard)
                .where(freeBoardTitleEq(keyword))
                .fetchOne();
        return count;
    }

    /**
     * 자유게시판 cate 조회
     * @param searchForm 검색
     * @return 최신순, 인기순, 댓글순, 기본값 freeBoardId 순
     */
    private OrderSpecifier<?> getDynamicSoft(SearchForm searchForm){

        System.out.println(searchForm.getCate());
        switch (searchForm.getCate()){
            case "freeBoardRd" :
                System.out.println("최신순 조회");
                if (freeBoard.freeBoardMd == null){
                    return freeBoard.freeBoardRd.desc();
                }else{
                    return freeBoard.freeBoardMd.desc();
                }
            case "freeBoardCommentCount" :
                System.out.println("댓글순 조회");
                return freeBoard.freeBoardComment.size().desc();
            case "freeBoardViewCount" :
                System.out.println("인기순 조회");
                return freeBoard.freeBoardViewCount.desc();
            default:
                return freeBoard.id.desc();
        }
    }

    /**
     * 자유게시판 리스트 조회
     * @param pageable 페이징
     * @param searchForm 검색
     * @return
     */
    @Override
    public Page<FreeBoardListDto> findFreeBoardListBySearch(Pageable pageable,SearchForm searchForm) {

        //검색 조건 설정
        BooleanExpression keywordTitle = freeBoardTitleEq(searchForm.getKeyword());
        //정렬 기준 동적 설정 조회
        System.out.println(getDynamicSoft(searchForm)+"조회!!");

        List<FreeBoardDto> content = jpaQueryFactory
                .select(new QFreeBoardDto(
                        freeBoard.id,
                        freeBoard.freeBoardTitle,
                        freeBoard.freeBoardContent,
                        freeBoard.freeBoardRd,
                        freeBoard.freeBoardMd,
                        freeBoard.freeBoardViewCount,
                        freeBoard.freeBoardCommentCount,
                        freeBoard.users.id,
                        freeBoard.users.userAccount,
                        freeBoard.users.userNickName,
                        userFile.id.coalesce(0L),
                        userFile.route.coalesce("0"),
                        userFile.name.coalesce("0"),
                        userFile.uuid.coalesce("0")
                ))
                .from(freeBoard)
                .where(keywordTitle)
                .leftJoin(users.userFile,userFile)
//                .orderBy(freeBoard.id.desc())
                .orderBy(getDynamicSoft(searchForm))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //페이징을 위한 전체 데이터 수 조회
        Long count = getCount(searchForm.getKeyword());

        List<FreeBoardListDto> contents
                = content.stream().map(freeBoardDto -> {

            //댓글수 쿼리
            Long commentCount = jpaQueryFactory
                    .select(freeBoardComment.id.count())
                    .from(freeBoardComment)
                    .where(freeBoardComment.freeBoard.id.eq(freeBoardDto.getId()))
                    .fetchOne();

            System.out.println(commentCount+"댓글수 입니다.");

            List<FreeBoardImgDto> freeboardImgDto = jpaQueryFactory
                    .select(new QFreeBoardImgDto(
                            freeBoardImg.id,
                            freeBoardImg.freeBoardImgRoute,
                            freeBoardImg.freeBoardImgName,
                            freeBoardImg.freeBoardImgUuid,
                            freeBoard.id
                    ))
                    .from(freeBoardImg)
                    .leftJoin(freeBoardImg.freeBoard, freeBoard)
                    .where(freeBoard.id.eq(freeBoardDto.getId()))
                    .fetch();

            List<FreeBoardImgDto> fimgDto = freeboardImgDto.stream()
                    .map(fimgDtos -> new FreeBoardImgDto(
                            fimgDtos.getId(),
                            fimgDtos.getFreeBoardImgRoute(),
                            fimgDtos.getFreeBoardImgName(),
                            fimgDtos.getFreeBoardImgUuid(),
                            fimgDtos.getFreeBoarId()
                    ))
                    .collect(toList());

            return new FreeBoardListDto(
                    freeBoardDto.getId(),
                    freeBoardDto.getFreeBoardTitle(),
                    freeBoardDto.getFreeBoardContent(),
                    freeBoardDto.getFreeBoardRd(),
                    freeBoardDto.getFreeBoardMd(),
                    freeBoardDto.getFreeBoardViewCount(),
                    freeBoardDto.getUserId(),
                    freeBoardDto.getUserAccount(),
                    freeBoardDto.getUserNickName(),
                    freeBoardDto.getUserFileId(),
                    freeBoardDto.getRoute(),
                    freeBoardDto.getName(),
                    freeBoardDto.getUuid(),
                    commentCount,
                    fimgDto
            );
        }).collect(Collectors.toList());

        System.out.println(contents.toString()+"리스트");

        return new PageImpl<>(contents, pageable,count);
    }

    /**
     * BooleanExpression
     * : null 반환 시 자동으로 조건절에서 제거 된다.
     * (단, 모든 조건이 null이 발생 시 전체 엔티티를 불러오게 되므로
     * 대장애가 발생할 수 있음)
     *
     * StringUtils
     *  : 파라미터 값으로 null을 주더라도 절대 NullPointException을
     *  발생시키지 않는다!
     * (null이 입력되는 경우, 메소드에 따라 알맞은 결과를 리턴.)
     *
     * StringUtils.hasText(String)	// boolean
     * StringUtils.containsText(CharSequence)	// boolean
     */
    private BooleanExpression freeBoardTitleEq(String keyword){
        return StringUtils.hasText(keyword) ? freeBoard.freeBoardTitle.containsIgnoreCase(keyword) : null;
    }

    /**
     * 자유게시판 이미지 조회
     */
    @Override
    public List<FreeBoardImgDto> findFreeBoardImgByFreeBoardId(Long freeBoardId) {
        return jpaQueryFactory.select(new QFreeBoardImgDto(
                freeBoardImg.id,
                freeBoardImg.freeBoardImgRoute,
                freeBoardImg.freeBoardImgName,
                freeBoardImg.freeBoardImgUuid,
                freeBoard.id
        ))
                .from(freeBoardImg)
                .where(freeBoardImg.freeBoard.id.eq(freeBoardId))
                .fetch();
    }

    /**
     * 자유게시판 인기글 TOP3 조회
     */
    @Override
    public List<FreeBoardDto> findFreeBoardRankByIdId() {
        List<FreeBoardDto> content = jpaQueryFactory
                .select(new QFreeBoardDto(
                        freeBoard.id,
                        freeBoard.freeBoardTitle,
                        freeBoard.freeBoardContent,
                        freeBoard.freeBoardRd,
                        freeBoard.freeBoardMd,
                        freeBoard.freeBoardViewCount,
                        freeBoard.freeBoardCommentCount,
                        freeBoard.users.id,
                        freeBoard.users.userAccount,
                        freeBoard.users.userNickName,
                        userFile.id.coalesce(0L),
                        userFile.route.coalesce("0"),
                        userFile.name.coalesce("0"),
                        userFile.uuid.coalesce("0")
                ))
                .from(freeBoard)
                .leftJoin(users.userFile,userFile)
                .orderBy(freeBoard.freeBoardViewCount.desc())
                .limit(3)
                .fetch();
        return content;
    }


    @Override
    public Page<MyFreeBoardResultDto> findByUserId(Pageable pageable, Long userId) {
        System.out.println(userId+ " 조회해야되는 아이디 입니다.");
        List<MyFreeBoardListDto> query = em.createQuery(
                "SELECT NEW com.example.dw.domain.dto.community.MyFreeBoardListDto(" +
                        "f.id, f.freeBoardTitle, f.freeBoardContent, u.id, u.userAccount, u.userNickName, " +
                        "uf.id, uf.route, uf.uuid, uf.name, " +
                        "(SELECT COUNT(fc) FROM FreeBoardComment fc WHERE fc.freeBoard.id = f.id) as commentCount, " +
                        "fi.id, fi.freeBoardImgRoute, fi.freeBoardImgName, fi.freeBoardImgUuid ) " +
                        "FROM FreeBoard f " +
                        "LEFT JOIN f.users u " +
                        "LEFT JOIN u.userFile uf " +
                        "LEFT JOIN f.freeBoardImg fi " +
                        "WHERE f.users.id = :userId and fi.id = (select Min(fi2.id) from FreeBoardImg fi2 where f.id = fi2.freeBoard.id) " +
                        "ORDER BY f.id DESC", MyFreeBoardListDto.class)
                .setParameter("userId", userId)
                .getResultList();

        System.out.println(query.toString()+"입니다");

        List<MyFreeBoardResultDto> result =  query.stream().map(
                o-> new MyFreeBoardResultDto(
                        o.getId(),
                        o.getFreeBoardTitle(),
                        o.getFreeBoardContent(),
                        o.getUserId(),
                        o.getUserAccount(),
                        o.getUserNickName(),
                        new UserFileDto(o.getUserFileId(),o.getRoute(),o.getName(),o.getUuid(),o.getUserId()),
                        o.getCommentCount(),
                        new FreeBoardImgDto(o.getFreeBoardImgId(),o.getFreeBoardImgRoute(),o.getFreeBoardImgName(),o.getFreeBoardImgUuid(),o.getId()))
        ).collect(toList());


        Long count = jpaQueryFactory
                .select(freeBoard.count())
                .from(freeBoard)
                .where(users.id.eq(userId))
                .fetchOne();


        return new PageImpl<>(result,pageable,count);
    }
}