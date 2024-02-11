package com.example.dw.repository.goods;

import com.example.dw.domain.dto.goods.*;
import com.example.dw.domain.entity.goods.GoodsCategory;
import com.example.dw.domain.form.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ShopRepositoryCustom {

    // 쇼핑 페이지 전체 리스트, 페이징, 검색
    Page<GoodsListDto> findGoodsListAll(Pageable pageable, SearchForm searchForm);
    //간식
    Page<GoodsListDto> findGoodsAList(Pageable pageable, SearchForm searchForm);
    //영양제
    Page<GoodsListDto> findGoodsBList(Pageable pageable, SearchForm searchForm);
    //위생용품
    Page<GoodsListDto> findGoodsCList(Pageable pageable, SearchForm searchForm);
    //이동장
    Page<GoodsListDto> findGoodsDList(Pageable pageable, SearchForm searchForm);
    //장난감
    Page<GoodsListDto> findGoodsEList(Pageable pageable, SearchForm searchForm);
    //산책용품
    Page<GoodsListDto> findGoodsFList(Pageable pageable, SearchForm searchForm);
    //상품상세
    Optional<GoodsDetailDto> findGoodsById(Long id);
    //상품추가정보
    Optional<GoodsAddInfoDto> findGoodsAddInfoById(Long id);
    //싱품리뷰리스트
    List<GoodsReviewListDto> findGoodsReviewById(Long id);
    //상품문의
    List<GoodsQueDto> findGoodsQueId(Long id);
    //상품상세이미지
    List<GoodsDetailImgDto> findGoodsDetailImg(Long goodsId);
    //카트Dto
    CartDto findCartIdByUserId(Long userId);
    //상품번호체크
    boolean checkGoodsId(Long goodsId, Long userId, Long cartId);
    //카트아이템번호
    List<GoodsCartItemDto> findGoodsCartItemById(Long cartId, Long userId);
}
