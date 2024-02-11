package com.example.dw.api;


import com.example.dw.domain.dto.community.FreeBoardListDto;
import com.example.dw.domain.form.SearchForm;
import com.example.dw.repository.freeBoard.FreeBoardRepositoryCustom;
import com.example.dw.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities/*")
public class FreeBoardApiController {

    private final FreeBoardService freeBoardService;

    /**
     * 자유게시판 파일처리
     * fileFreeImg 경로
     * return getEmpImg 파일 경로를 받아와서 해당 이미지 파일의 바이트 배열을 반환
     */
    @Value("${file.free}")
    private String fileFreeImg;

    @GetMapping("/freeImg")
    public byte[] getEmpImg(String fileFullPath) throws IOException {
        return FileCopyUtils.copyToByteArray(new File(fileFreeImg, fileFullPath));
    }

    /**
     * 유저 이미지 파일처리
     * userImg 경로
     * return getEmpImg 파일 경로를 받아와서 해당 이미지 파일의 바이트 배열을 반환
     */
    @Value("${file.user}")
    private String userImg;

    @GetMapping("/freeUserImg")
    public byte[] getUserImg(String userImgPath) throws IOException {
        return FileCopyUtils.copyToByteArray(new File(userImg, userImgPath));
    }

    /**
     * 자유게시판 리스트
     * @param page 페이징
     * @param searchForm 검색
     * @return 자유게시판 리스트
     */
    @GetMapping("/freeBoardList/{page}")
    public Page<FreeBoardListDto> freeBoardDtoList(
            @PathVariable("page") int page, SearchForm searchForm) {

        System.out.println(searchForm.getCate());
        System.out.println(searchForm.getKeyword());

        Pageable pageable = PageRequest.of(page, 5);
        return freeBoardService.freeBoardListDtos(pageable, searchForm);
    }
}