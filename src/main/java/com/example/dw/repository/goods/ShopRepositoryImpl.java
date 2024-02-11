package com.example.dw.repository.goods;

import com.example.dw.domain.dto.goods.*;
import com.example.dw.domain.entity.goods.GoodsCategory;
import com.example.dw.domain.form.SearchForm;
import com.querydsl.core.types.Expression;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.example.dw.domain.entity.freeBoard.QFreeBoard.freeBoard;
import static com.example.dw.domain.entity.goods.QCart.cart;
import static com.example.dw.domain.entity.goods.QCartItem.cartItem;
import static com.example.dw.domain.entity.goods.QGoods.goods;
import static com.example.dw.domain.entity.goods.QGoodsDetailImg.goodsDetailImg;
import static com.example.dw.domain.entity.goods.QGoodsMainImg.goodsMainImg;
import static com.example.dw.domain.entity.goods.QGoodsQue.goodsQue;
import static com.example.dw.domain.entity.goods.QGoodsQueReply.goodsQueReply;
import static com.example.dw.domain.entity.order.QGoodsReviewReply.goodsReviewReply;
import static com.example.dw.domain.entity.user.QUsers.users;
import static com.example.dw.domain.entity.order.QOrderReview.orderReview;
import static com.example.dw.domain.entity.order.QOrderItem.orderItem;
import static com.example.dw.domain.entity.order.QOrderReviewImg.orderReviewImg;
import static com.example.dw.domain.entity.order.QOrders.orders;
import static java.util.stream.Collectors.groupingBy;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 상품이름 으로 키워드 검색용
     * @param keyword 키워드 검색
     * @return 키워드가 비어있는지 조건을 확인 하여 반환
     */
    private BooleanExpression goodsNameEq(String keyword){
        return StringUtils.hasText(keyword) ? goods.goodsName.containsIgnoreCase(keyword) : null;
    }

    /**
     * 쇼핑 리스트 검색 조건
     * @param searchForm 검색
     * @return 정렬 조건 반환
     */
    private OrderSpecifier<?> getDynamicSoft(SearchForm searchForm){

        System.out.println(searchForm.getCate());
        switch (searchForm.getCate()){
            case "goodsRegisterDate" :
                System.out.println("최신순 조회");
                //등록 날짜 내림차순
                return goods.goodsRegisterDate.desc();
            case "goodsPriceUp" :
                System.out.println("높은 가격 순 조회");
                return goods.goodsPrice.desc();
            case "goodsPriceDown" :
                System.out.println("낮은 가격 순 조회");
                return goods.goodsPrice.asc();
            case "goodsPopularitySort" :
                System.out.println("인기순 조회");
                //판매량 내림차순
                return goods.saleCount.desc();
            default:
                return goods.saleCount.desc();
        }
    }

    /**
     * @param pageable 페이징
     * @param searchForm 검색 조건
     * @return 상품 카테고리 조건 별 검색
     */
    @Override
    public Page<GoodsListDto> findGoodsListAll(Pageable pageable, SearchForm searchForm) {
        return findGoodsListByCategory(pageable, searchForm, null);
    }
    @Override
    public Page<GoodsListDto> findGoodsAList(Pageable pageable, SearchForm searchForm) {
        return findGoodsListByCategory(pageable, searchForm, GoodsCategory.간식);
    }
    @Override
    public Page<GoodsListDto> findGoodsBList(Pageable pageable, SearchForm searchForm) {
        return findGoodsListByCategory(pageable, searchForm, GoodsCategory.영양제);
    }
    @Override
    public Page<GoodsListDto> findGoodsCList(Pageable pageable, SearchForm searchForm) {
        return findGoodsListByCategory(pageable, searchForm, GoodsCategory.위생용품);
    }
    @Override
    public Page<GoodsListDto> findGoodsDList(Pageable pageable, SearchForm searchForm) {
        return findGoodsListByCategory(pageable, searchForm, GoodsCategory.이동장);
    }
    @Override
    public Page<GoodsListDto> findGoodsEList(Pageable pageable, SearchForm searchForm) {
        return findGoodsListByCategory(pageable, searchForm, GoodsCategory.장난감);
    }
    @Override
    public Page<GoodsListDto> findGoodsFList(Pageable pageable, SearchForm searchForm) {
        return findGoodsListByCategory(pageable, searchForm, GoodsCategory.산책용품);
    }

    /**
     * @param pageable 페이징
     * @param searchForm 검색 조건
     * @param category 카테고리 조건
     * @return 카테고리를 기준으로 상품 목록을 검색, 페이징 결과 반환
     */
    Page<GoodsListDto> findGoodsListByCategory(
            Pageable pageable, SearchForm searchForm, GoodsCategory category) {

        // 상품 이름 별 키워드 검색
        BooleanExpression keywordTitle = goodsNameEq(searchForm.getKeyword());
        // 동적 정렬 설정
        OrderSpecifier<?> dynamicOrder = getDynamicSoft(searchForm);
        // 상품 카테고리 별 검색
        BooleanExpression categoryExpression = category != null ? goods.goodsCategory.eq(category) : null;

        List<GoodsListDto> contents = jpaQueryFactory
                .select(new QGoodsListDto(
                        goods.id,
                        goods.goodsName,
                        goods.goodsQuantity,
                        goods.goodsPrice,
                        goods.goodsMade,
                        goods.goodsRegisterDate,
                        goods.goodsModifyDate,
                        goods.goodsCategory.stringValue(),
                        goodsMainImg.id,
                        goodsMainImg.goodsMainImgName,
                        goodsMainImg.goodsMainImgPath,
                        goodsMainImg.goodsMainImgUuid,
                        jpaQueryFactory.select(
                                orderReview.rating.avg().coalesce(0.0)
                        )
                                .from(orderReview)
                                .where(orderReview.orderItem.goods.id.eq(goods.id)),
                        jpaQueryFactory.select(
                                orderReview.count()
                        )
                                .from(orderReview)
                                .where(orderReview.orderItem.goods.id.eq(goods.id))
                ))
                .from(goods)
                .leftJoin(goods.goodsMainImg, goodsMainImg)
                .where(keywordTitle, categoryExpression)
                .orderBy(dynamicOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // 페이징을 위한 전체 데이터 수 조회
        Long count = getCount(searchForm.getKeyword());

        return new PageImpl<>(contents, pageable, count);
    }

    /**
     * @param id 상품 번호
     * @return 쇼핑 - 상품 상세 조회
     */
    @Override
    public Optional<GoodsDetailDto> findGoodsById(Long id){
        return Optional.ofNullable(jpaQueryFactory
                .select(new QGoodsDetailDto(
                        goods.id,
                        goods.goodsName,
                        goods.goodsQuantity,
                        goods.goodsPrice,
                        goods.goodsMade,
                        goods.goodsDetailContent,
                        goods.goodsRegisterDate,
                        goods.goodsModifyDate,
                        goods.goodsCategory.stringValue(),
                        goodsMainImg.id,
                        goodsMainImg.goodsMainImgName,
                        goodsMainImg.goodsMainImgPath,
                        goodsMainImg.goodsMainImgUuid,
                        jpaQueryFactory.select(
                                orderReview.rating.avg().coalesce(0.0)
                        )
                                .from(orderReview)
                                .where(orderReview.orderItem.goods.id.eq(goods.id)),
                        jpaQueryFactory.select(
                                orderReview.count()
                        )
                                .from(orderReview)
                                .where(orderReview.orderItem.goods.id.eq(goods.id))
                ))
                .from(goods)
                .leftJoin(goods.goodsMainImg, goodsMainImg)
                .where(goods.id.eq(id))
                .fetchOne());
    }

    /**
     * @param goodsId 상품 번호
     * @return 상품 상세 이미지 조회
     */
    @Override
    public List<GoodsDetailImgDto> findGoodsDetailImg(Long goodsId) {
        return jpaQueryFactory.select(new QGoodsDetailImgDto(
                goodsDetailImg.id,
                goodsDetailImg.goodsDetailImgName,
                goodsDetailImg.goodsDetailImgPath,
                goodsDetailImg.goodsDetailImgUuid
        ))
                .from(goodsDetailImg)
                .where(goodsDetailImg.goods.id.eq(goodsId))
                .fetch();
    }

    /**
     * @param id 상품 번호
     * @return 쇼핑 - 상품 추가 정보 조회
     */
    @Override
    public Optional<GoodsAddInfoDto> findGoodsAddInfoById(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .select(new QGoodsAddInfoDto(
                        goods.id,
                        goods.goodsName,
                        goods.goodsMade,
                        goods.goodsCertify
                ))
                .from(goods)
                .where(goods.id.eq(id))
                .fetchOne()
        );
    }

    /**
     * @param id 상품 번호
     * @return 쇼핑 - 상품 리뷰 조회
     */
    @Override
    public List<GoodsReviewListDto> findGoodsReviewById(Long id) {
        List<GoodsReviewDto> reviewDtoList = jpaQueryFactory
                .select(new QGoodsReviewDto(
                        orderReview.id,
                        orderReview.title,
                        orderReview.content,
                        orderReview.reviewRd,
                        orderReview.rating,
                        goodsReviewReply.id,
                        goodsReviewReply.goodsReviewReplyContent,
                        goodsReviewReply.goodsReviewReplyRD,
                        goodsReviewReply.goodsReviewReplyMD,
                        goods.id,
                        orderItem.id,
                        orders.users.id,
                        orders.users.userAccount,
                        orders.users.userNickName,
                        orderReviewImg.id,
                        orderReviewImg.reviewimgFileName,
                        orderReviewImg.reviewimgPath,
                        orderReviewImg.reviewimgUuid
                ))
                .from(orderReview)
                .leftJoin(orderReview.goodsReviewReply, goodsReviewReply)
                .leftJoin(orderReview.orderReviewImgList, orderReviewImg)
                .leftJoin(orderReview.orderItem, orderItem)
                .leftJoin(orderItem.orders, orders)
                .leftJoin(orders.users, users)
                .where(goods.id.eq(id))
                .orderBy(orderReview.id.desc(), orderReviewImg.id.asc())
                .fetch();


        Map<Long, List<GoodsReviewDto>> result = reviewDtoList.stream().
                collect(groupingBy(
                dto -> dto.getId()
                )
        );

        List<GoodsReviewListDto> rr = result.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .map(  dto ->
                        {

                                List < GoodsReviewDto > resultDtos = dto.getValue();

                                List<GoodsReviewImgDto> imgs = resultDtos.stream().map(
                                        img -> new GoodsReviewImgDto(img.getReviewimgId(),
                                                img.getReviewimgFileName(),
                                                img.getReviewimgPath(),
                                                img.getReviewimgUuid())
                                ).collect(Collectors.toList());

                            GoodsReviewListDto resultDto = resultDtos.stream().findFirst()
                                    .map(
                                            review -> new GoodsReviewListDto(
                                                    review.getId(),
                                                    review.getTitle(),
                                                    review.getContent(),
                                                    review.getReviewRd(),
                                                    review.getRating(),
                                                    review.getGoodsReviewReplyId(),
                                                    review.getGoodsReviewReplyContent(),
                                                    review.getGoodsReviewReplyRD(),
                                                    review.getGoodsReviewReplyMD(),
                                                    review.getGoodsId(),
                                                    review.getOrderItemId(),
                                                    review.getUserId(),
                                                    review.getUserAccount(),
                                                    review.getUserNickName(),
                                                    imgs
                                                    )
                                    ).orElse(null);

                            return resultDto;
                        }
                ).collect(Collectors.toList());

                return rr;
    }

    /**
     * @param id 상품 번호
     * @return 쇼핑 - 상품 문의 조회
     */
    @Override
    public List<GoodsQueDto> findGoodsQueId(Long id) {
        return jpaQueryFactory
                .select(new QGoodsQueDto(
                        goodsQue.id,
                        goodsQue.queContent,
                        goodsQue.queRegisterDate,
                        goodsQue.queModifyDate,
                        goodsQueReply.id,
                        goodsQueReply.queReplyContent,
                        goodsQueReply.queReplyRegisterDate,
                        goodsQueReply.queReplyModifyDate,
                        users.id,
                        users.userAccount,
                        users.userNickName
                ))
                .from(goods)
                .leftJoin(goods.goodsQues, goodsQue)
                .leftJoin(goodsQue.goodsQueReply, goodsQueReply)
                .leftJoin(goodsQue.users, users)
                .where(goods.id.eq(id))
                .orderBy(goodsQue.id.desc())
                .fetch();
    }

    /**
     * @param keyword 키워드 검색
     * @return 리스트 갯수 조회 값
     */
    private Long getCount(String keyword) {
        Long count = jpaQueryFactory
                .select(goods.count())
                .from(goods)
                .where(goodsNameEq(keyword))
                .fetchOne();
        return count;
    }

    /**
     * @param userId 유저 번호
     * @return 카트 Dto 조회
     */
    @Override
    public CartDto findCartIdByUserId(Long userId) {
        return jpaQueryFactory.select(new QCartDto(
                cart.id,
                cart.users.id
        ))
                .from(cart)
                .leftJoin(cart.users, users)
                .where(cart.users.id.eq(userId))
                .fetchOne();
    }

    /**
     * 장바구니 아이템으로 들어가기 위해 체크
     * @param goodsId 상품 번호
     * @param userId 유저 번호
     * @param cartId 카트(장바구니) 번호
     * @return 상품 번호, 사용자 번호, 장바구니 번호를 체크 하여 true, false 반환
     */
    @Override
    public boolean checkGoodsId(Long goodsId, Long userId, Long cartId) {
        CartItemCheckDto check =
                jpaQueryFactory.select(new QCartItemCheckDto(
                        cartItem.id,
                        cartItem.goods.id,
                        cartItem.cart.id,
                        cartItem.cart.users.id
                ))
                        .from(cartItem)
                        .leftJoin(cartItem.goods, goods)
                        .leftJoin(cartItem.cart, cart)
                        .leftJoin(cart.users, users)
                        .where(cartItem.goods.id.eq(goodsId),
                                cart.users.id.eq(userId))
                        .fetchOne();

        if(check==null){
            return false;
        }else if(check.getGoodsId()==goodsId){
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param cartId 카트 번호
     * @param userId 유저 번호
     * @return 카트 아이템 조회
     */
    @Override
    public List<GoodsCartItemDto> findGoodsCartItemById(Long cartId, Long userId) {
        return jpaQueryFactory
                .select(new QGoodsCartItemDto(
                        cartItem.id,
                        cartItem.cartItemQuantity,
                        cart.id,
                        cart.users.id,
                        goods.id,
                        goods.goodsName,
                        goods.goodsQuantity,
                        goods.goodsPrice,
                        goodsMainImg.id,
                        goodsMainImg.goodsMainImgName,
                        goodsMainImg.goodsMainImgPath,
                        goodsMainImg.goodsMainImgUuid
                ))
                .from(cartItem)
                .leftJoin(cartItem.goods, goods)
                .leftJoin(cartItem.goods.goodsMainImg, goodsMainImg)
                .leftJoin(cartItem.cart, cart)
                .leftJoin(cart.users, users)
                .where(cart.id.eq(cartId).and(
                        cart.users.id.eq(userId)
                ))
                .fetch();
    }
}