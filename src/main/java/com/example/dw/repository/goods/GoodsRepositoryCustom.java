package com.example.dw.repository.goods;

import com.example.dw.domain.dto.admin.AdminGoodsQnaListDto;
import com.example.dw.domain.dto.admin.AdminGoodsQueDetailDto;
import com.example.dw.domain.dto.admin.AdminGoodsQueReplyDto;
import com.example.dw.domain.dto.admin.goods.AdminGoods;
import com.example.dw.domain.dto.admin.goods.AdminGoodsReview;
import com.example.dw.domain.dto.goods.IndexGoodsByCateDto;
import com.example.dw.domain.entity.goods.GoodsCategory;
import com.example.dw.domain.form.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GoodsRepositoryCustom {

    //메인페이지 카테고리별 상품 리스트
    List<IndexGoodsByCateDto> indexGoodsListByCategory(GoodsCategory goodsCategory);

    //관리자 상품 리스트
    Page<AdminGoods.AdminGoodsList> findGoodsAll(Pageable pageable, SearchForm searchForm);
    
    //관리자 상품 상세
    AdminGoods.AdminGoodsDetail findGoodsById(Long id);

    //관리자 상품 상세 - 상품 관련 문의사항 리스트
    Page<AdminGoodsQnaListDto> getQnaList(Long goodsId, Pageable pageable, String state);

    //관리자 상품 리뷰 리스트
    Page<AdminGoodsReview.AdminGoodsRelatedReview> getReviewList(Long goodsId, Pageable pageable, String state);

    //관리자 상품 문의 리스트
    Page<AdminGoodsQnaListDto> getQnaList(Pageable pageable, String qnaState, String cate, String keyword);

    //관리자 상품 문의 상세
    Optional<AdminGoodsQueDetailDto> getQnaDetail(Long qnaId);

    //관리자 상품 문의 답변 가져오기
    AdminGoodsQueReplyDto getReplyList(Long qnaId);
}