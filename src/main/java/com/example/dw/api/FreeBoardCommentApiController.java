package com.example.dw.api;


import com.example.dw.domain.dto.community.FreeBoardCommentDto;
import com.example.dw.domain.form.FreeBoardCommentForm;
import com.example.dw.repository.freeBoard.FreeBoardCommentRepository;
import com.example.dw.repository.user.UsersRepository;
import com.example.dw.service.FreeBoardCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/replies/*")
@Slf4j
public class FreeBoardCommentApiController {

    private final FreeBoardCommentService freeBoardCommentService;

    /**
     * 자유게시판 댓글 등록
     */
    @PostMapping("/writeComment")
    public void register(@RequestBody FreeBoardCommentForm freeBoardCommentForm) {
                freeBoardCommentService.register(freeBoardCommentForm);
    }

    /**
     * 자유게시판 댓글 불러오기
     * @param freeBoardId 해당 자유게시판 번호
     * @return 댓글 목록
     */
    @GetMapping("/readComment/{freeBoardId}")
    public List<FreeBoardCommentDto> showReplyList(
            @PathVariable("freeBoardId") Long freeBoardId){
      return freeBoardCommentService.getList(freeBoardId);
    }

    /**
     * 자유게시판 댓글 수정
     */
    @PatchMapping("/modifyComment/{freeBoardCommentId}")
    public void modify(@RequestBody FreeBoardCommentForm freeBoardCommentForm) {
        freeBoardCommentService.modify(freeBoardCommentForm);
    }

    /**
     * 자유게시판 댓글 삭제
     * @param freeBoardCommentId 해당 자유게시판 댓글 번호
     */
    @DeleteMapping("/deleteComment/{freeBoardCommentId}")
    public void delete(@PathVariable Long freeBoardCommentId) {
        freeBoardCommentService.remove(freeBoardCommentId);
    }
}
