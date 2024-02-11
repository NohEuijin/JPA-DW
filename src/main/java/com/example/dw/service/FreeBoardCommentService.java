package com.example.dw.service;

import com.example.dw.domain.dto.community.FreeBoardCommentDto;
import com.example.dw.domain.entity.freeBoard.FreeBoardComment;
import com.example.dw.domain.form.FreeBoardCommentForm;
import com.example.dw.repository.freeBoard.FreeBoardCommentCustom;
import com.example.dw.repository.freeBoard.FreeBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeBoardCommentService {

    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final FreeBoardCommentCustom freeBoardCommentCustom;

    /**
     * 자유게시판 댓글 등록하기
     * @param freeBoardCommentForm 자유게시판 댓글 폼
     */
    public void register(FreeBoardCommentForm freeBoardCommentForm){
        freeBoardCommentRepository.save(freeBoardCommentForm.toEntity());
    }

    /**
     * 자유게시판 댓글 불러오기
     * @param freeBoardId 해당 자유게시판 번호
     * @return 댓글 목록
     */
    public List<FreeBoardCommentDto> getList(Long freeBoardId){
        if (freeBoardId == null) {
            throw new IllegalArgumentException("존재하지 않는 게시물 번호");
        }
        return freeBoardCommentCustom.findFreeBoardCommentsByFreeBoardId(freeBoardId);
    }

    /**
     * 자유게시판 댓글 수정하기
     * @param freeBoardCommentForm 자유게시판 댓글 폼
     */
    public void modify(FreeBoardCommentForm freeBoardCommentForm) {
        FreeBoardComment modifyComment = freeBoardCommentRepository.findById(freeBoardCommentForm.getId())
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        // 수정할 내용으로 엔티티 업데이트
        modifyComment.update(freeBoardCommentForm);
        // 엔티티 저장
        freeBoardCommentRepository.save(modifyComment);
    }

    /**
     * 자유게시판 댓글 삭제하기
     * @param freeBoardCommentId 해당 자유게시판 댓글 번호
     */
    public void remove(Long freeBoardCommentId) {
        // 해당 댓글을 가져와서 확인
        FreeBoardComment removeComment = freeBoardCommentRepository.findById(freeBoardCommentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        // 엔티티 삭제
        freeBoardCommentRepository.delete(removeComment);
    }
}