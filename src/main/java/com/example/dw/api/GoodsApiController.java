package com.example.dw.api;

import com.example.dw.domain.dto.goods.*;
import com.example.dw.domain.form.*;
import com.example.dw.service.GoodsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops/*")
public class GoodsApiController {

    private final GoodsService goodsService;

    /**
     * 쇼핑 이미지 처리
     */
    @Value("${file.dir}")
    private String fileShopImg;

    @GetMapping("/shopImg")
    public byte[] getEmpImg(String fileFullPath) throws IOException {
        return FileCopyUtils.copyToByteArray(new File(fileShopImg, fileFullPath));
    }

    /**
     * @param category 쇼핑 카테고리
     * @param page 페이징
     * @param searchForm 검색
     * @return 카테고리별 쇼핑 목록
     */
    @GetMapping("/shop/{category}/{page}")
    public Page<GoodsListDto> findShopListByCategory(
            @PathVariable("category") String category,
            @PathVariable("page") int page, SearchForm searchForm){
        Pageable pageable = PageRequest.of(page, 12);
        return goodsService.getGoodsListByCategory(category, pageable, searchForm);
    }

    /**
     * 쇼핑 상세 - 설명(이미지)
     * @return 이미지
     */
    @GetMapping("/shopDetilImgs/{goodsId}")
    public List<GoodsDetailImgDto> findDetailImgs(@PathVariable("goodsId") Long goodsId){
        return goodsService.goodsDetailImgs(goodsId);
    }

    /**
     * 쇼핑 상세 - 추가정보
     * @return 추가정보
     */
    @GetMapping("/shopAddInfo/{goodsId}")
    public Optional<GoodsAddInfoDto> findAddInfo(@PathVariable("goodsId") Long goodsId) {
        return goodsService.goodsAddInfo(goodsId);
    }

    /**
     * 쇼핑 상세 - 리뷰
     * @return 리뷰 목록
     */
    @GetMapping("/shopReview/{goodsId}")
    public List<GoodsReviewListDto> findReviewList(@PathVariable("goodsId") Long goodsId){
        System.out.println(goodsService.goodsReviewList(goodsId).toString());
        return goodsService.goodsReviewList(goodsId);
    }

    /**
     * 쇼핑 상세 - 문의
     * @return 문의 목록
     */
    @GetMapping("/shopQnaList/{goodsId}")
    public List<GoodsQueDto> findQnaList(@PathVariable("goodsId") Long goodsId){
        System.out.println(goodsService.goodsQnaList(goodsId).toString());
        return goodsService.goodsQnaList(goodsId);
    }

    /**
     *  쇼핑 - 모달 창 - 문의 글 작성
     * @param goodsQandaWritingForm 문의 글 작성 폼
     * @return 모달에서 작성된 문의 글
     */
    @PostMapping("/shopQandaWriteModal")
    public Long shopQandaWriteModal(GoodsQandaWritingForm goodsQandaWritingForm) {
        return goodsService.writeModal(goodsQandaWritingForm);
    }

    /**
     * 유저의 쇼핑 아이템 번호를 부여하는 메소드
     * @param userId 유저 번호
     * @param cartItemForm 쇼핑 아이템 카트
     */
    @GetMapping("/shopCart/{userId}")
    public void findCartList(@PathVariable("userId") Long userId,
                                           CartItemForm cartItemForm){
        System.out.println("api goodsid" + cartItemForm.getGoodsId());
        System.out.println("api cartid" + cartItemForm.getCartId());

        goodsService.cartItemRegister(userId, cartItemForm);
    }
    /**
     * 쇼핑  - 카트 물품 저장
     * @param userId 유저 번호
     * @return 유저의 카트 상품들
     */
    @GetMapping("/shopCartList/{userId}")
    public GoodsCartListDto findCartList(@PathVariable("userId") Long userId){
       return goodsService.findCartItems(userId);
    }
    /**
     * 카트 물건 - 삭제
     * @param cartItemId 카트 아이템 번호
     */
    @GetMapping("/delete/{cartItemId}")
    public void deleteCartItem(@PathVariable("cartItemId")Long cartItemId){
        goodsService.deleteCartItem(cartItemId);
    }

    /**
     * 카트 물건 - 전체 삭제
     */
    @GetMapping("/deleteAll")
    public void deleteCart() {
        goodsService.deleteAllCartItems();
    }

    /**
     * 장바구니에 아이템(상품) 저장하기
     * @param goodsPayListFrom 장바구니 리스트를 저장할 폼
     * @param session 장바구니 아이템(상품) 저장을 위한 세션
     */
    @PostMapping("/cartGoods")
    public void cartGoods(@RequestBody List<GoodsPayListFrom> goodsPayListFrom, HttpSession session){

        List<GoodsPayListFrom> goodsPayList = (List<GoodsPayListFrom>)session.getAttribute("goodsPayList");

        if(goodsPayList == null){
            goodsPayList = new ArrayList<>();
        }
        for (GoodsPayListFrom goodsPayListDto : goodsPayListFrom) {
            boolean found = false;
            // goodsId를 기준으로 기존 리스트에서 아이템 찾기
            for (GoodsPayListFrom existingItem : goodsPayList) {
                if (existingItem.getGoodsId().equals(goodsPayListDto.getGoodsId())) {
                    // 기존 아이템이 존재하면 업데이트
                    existingItem.setGoodsQuantity(goodsPayListDto.getGoodsQuantity());
                    found = true;
                    break;
                }
            }
            // 기존 리스트에 해당 goodsId가 없으면 새로 추가
            if (!found) {
                goodsPayList.add(goodsPayListDto);
            }
        }
        System.out.println(goodsPayList);
        session.setAttribute("goodsPayList", goodsPayList);
    }

    /**
     * 쇼핑 - 바로구매 아이템(상품) 저장하기
     * @param goodsPaySingleFrom 단건 아이템(상품)을 저장하기 위한 폼
     * @param session 장바구니 아이템(상품) 저장을 위한 세션
     */
    @PostMapping("/payGoods")
    public void payGoods(@RequestBody List<GoodsPaySingleFrom> goodsPaySingleFrom, HttpSession session){

        List<GoodsPaySingleFrom> goodsPaySingle = (List<GoodsPaySingleFrom>) session.getAttribute("goodsPaySingle");

        if (goodsPaySingle == null) {
            goodsPaySingle = new ArrayList<>();
        }
        for(GoodsPaySingleFrom goodsPaySingleDto : goodsPaySingleFrom) {
            goodsPaySingle.add(goodsPaySingleDto);
        }
        System.out.println(goodsPaySingle);
        session.setAttribute("goodsPaySingle", goodsPaySingle);
    }

    /**
     * 장바구니 -> 주문서(장바구니 세션(상품) 정보 가져가기)
     * @param session 장바구니 아이템(상품) 저장을 위한 세션
     * @return 장바구니에 저장된 세션(상품)
     */
    @GetMapping("/goodsPickList")
    public List<GoodsPayListFrom> payGoodsList(HttpSession session){
        return (List<GoodsPayListFrom>) session.getAttribute("goodsPayList");
    }

    /**
     * 바로 구매 하기
     * 장바구니 -> 주문서(장바구니 세션(상품) 단건 가져가기)
     * @param httpSession 장바구니 아이템(상품) 단건을 저장하기 위한 세션
     * @return 장바구니에 단건으로 저장된 세션(상품)
     */
    @GetMapping("/goodsSinglePickList")
    public List<GoodsPaySingleFrom> payGoodsSingle(HttpSession httpSession) {
        List<GoodsPaySingleFrom> goodsPaySingleFrom = (List<GoodsPaySingleFrom>) httpSession.getAttribute("goodsPaySingle");

        //주문서 비우고 새로 저장
        if (goodsPaySingleFrom != null) {
            //세션삭제
            httpSession.removeAttribute("goodsPaySingle");
            System.out.println("세션삭제 후 새로 저장");
        }
        //저장 후 시간 받아오기
        for(GoodsPaySingleFrom goodsPaySingleFroms : goodsPaySingleFrom){
            goodsPaySingleFroms.setInputTime(LocalDateTime.now());
        }
        //시간 순 정렬 하기
        goodsPaySingleFrom.sort(Comparator.comparing((GoodsPaySingleFrom goodsPaySingleForm )->goodsPaySingleForm.getInputTime()).reversed());

        int max = 1;
        //리스트 배열 자르기
        if(goodsPaySingleFrom.size()>max){
            goodsPaySingleFrom=goodsPaySingleFrom.subList(0, max);
        }
        httpSession.setAttribute("goodsPaySingle", goodsPaySingleFrom);

        System.out.println("싱글 주문내역 가져오기 : "+goodsPaySingleFrom);
        return goodsPaySingleFrom;
    }
}