package com.example.dw.repository.freeBoard;

import com.example.dw.domain.dto.community.FreeBoardCommentDto;
import com.example.dw.domain.dto.community.QFreeBoardCommentDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.dw.domain.entity.freeBoard.QFreeBoardComment.freeBoardComment;
import static com.example.dw.domain.entity.user.QUserFile.userFile;
import static com.example.dw.domain.entity.user.QUsers.users;

@Repository
@RequiredArgsConstructor
public class FreeBoardCommentRepositoryImpl implements FreeBoardCommentCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 자유게시판 댓글 리스트 조회
     * @param freeBoardId 자유게시판 번호
     */
    @Override
    public List<FreeBoardCommentDto> findFreeBoardCommentsByFreeBoardId(Long freeBoardId){
        return jpaQueryFactory.select(new QFreeBoardCommentDto(
                freeBoardComment.id,
                freeBoardComment.freeBoardCommentContent,
                freeBoardComment.freeBoardCommentRd,
                freeBoardComment.freeBoardCommentMd,
                freeBoardComment.freeBoard.id,
                users.id,
                users.userAccount,
                users.userNickName,
                userFile.id,
                userFile.route,
                userFile.uuid,
                userFile.name
        ))
                .from(freeBoardComment)
                .leftJoin(freeBoardComment.users,users)
                .leftJoin(users.userFile,userFile)
                .where(freeBoardComment.freeBoard.id.eq(freeBoardId))
                .orderBy(freeBoardComment.id.desc())
                .fetch();
    }
}
