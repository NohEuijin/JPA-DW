package com.example.dw.service;


import com.example.dw.domain.dto.goods.*;
import com.example.dw.domain.entity.goods.Cart;
import com.example.dw.domain.entity.goods.CartItem;
import com.example.dw.domain.entity.goods.GoodsQue;
import com.example.dw.domain.entity.user.Users;
import com.example.dw.domain.form.CartForm;
import com.example.dw.domain.form.CartItemForm;
import com.example.dw.domain.form.GoodsQandaWritingForm;
import com.example.dw.domain.form.SearchForm;
import com.example.dw.repository.goods.*;
import com.example.dw.repository.user.UsersRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final HttpSession httpSession;
    private final UsersRepository usersRepository;
    private final GoodsQueRepository goodsQueRepository;
    private final GoodsRepository goodsRepository;
    private final ShopRepositoryCustom shopRepositoryCustom;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * @param category  쇼핑 카테고리
     * @param pageable  페이징
     * @param searchForm  검색
     * @return 카테고리별 쇼핑 목록
     */
    @Transactional
    public Page<GoodsListDto> getGoodsListByCategory(
            String category,
            Pageable pageable,
            SearchForm searchForm) {
        switch (category) {
            case "a": // 간식
                return shopRepositoryCustom.findGoodsAList(pageable, searchForm);
            case "b": // 영양제
                return shopRepositoryCustom.findGoodsBList(pageable, searchForm);
            case "c": // 위생용품
                return shopRepositoryCustom.findGoodsCList(pageable, searchForm);
            case "d": // 이동장
                return shopRepositoryCustom.findGoodsDList(pageable, searchForm);
            case "e": // 장난감
                return shopRepositoryCustom.findGoodsEList(pageable, searchForm);
            case "f": // 산책용품
                return shopRepositoryCustom.findGoodsFList(pageable, searchForm);
            default: // 전체
                return shopRepositoryCustom.findGoodsListAll(pageable, searchForm);
        }
    }

    /**
     * 문의 글 모달 글 작성하기
     * @param goodsQandaWritingForm 쇼핑 문의 글 폼
     * @return 저장된 문의 글의 번호
     */
    @Transactional
    public Long writeModal(GoodsQandaWritingForm goodsQandaWritingForm){
        // 세션에서 사용자 ID 가져오기
        Long userId = (Long) httpSession.getAttribute("userId");

        // 해당 사용자 ID로 Users 엔티티 가져오기
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // GoodsQue 엔티티 생성 시 queContent 설정
        GoodsQue savedGoodsQue = goodsQueRepository.save(
                GoodsQue.builder()
                        .queContent(goodsQandaWritingForm.getQueContent())
                        .users(user)
                        .goods(goodsRepository.findById(goodsQandaWritingForm.getGoodsId())
                                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다.")))
                        .build());

        return savedGoodsQue.getId();
    }

    /**
     * @param goodsId 상품 번호
     * @return 상품 상세 정보
     */
    @Transactional
    public Optional<GoodsDetailDto> goodsDetail(Long goodsId){
        return shopRepositoryCustom.findGoodsById(goodsId);
    }

    /**
     * @param goodsId 상품 번호
     * @return 상품 상세 이미지
     */
    @Transactional
    public List<GoodsDetailImgDto> goodsDetailImgs(Long goodsId){
        return shopRepositoryCustom.findGoodsDetailImg(goodsId);
    }

    /**
     * @param goodsId 상품 번호
     * @return 상품 리뷰 목록
     */
    @Transactional
    public List<GoodsReviewListDto> goodsReviewList(Long goodsId){
        return shopRepositoryCustom.findGoodsReviewById(goodsId);
    }

    /**
     * @param goodsId 상품 번호
     * @return 상품 문의 목록
     */
    @Transactional
    public List<GoodsQueDto> goodsQnaList(Long goodsId){
        return shopRepositoryCustom.findGoodsQueId(goodsId);
    }

    /**
     * @param goodsId 상품 번호
     * @return 상품의 추가 정보
     */
    @Transactional
    public Optional<GoodsAddInfoDto> goodsAddInfo(Long goodsId){
        return shopRepositoryCustom.findGoodsAddInfoById(goodsId);
    }

    /**
     * @param cartForm 카트 생성 폼
     * @return 카트 번호
     */
    @Transactional
    public Long cartRegister(CartForm cartForm){
        Cart cart = cartRepository.save(cartForm.toEntity());
        return cart.getId();
    }

    /**
     * 장바구니 번호 생성 및 아이템(상품) 수량 업데이트
     * @param userId 유저 번호
     * @param cartItemForm 장바구니 아이템(상품)을 저장할 폼
     */
    @Transactional
    public void cartItemRegister(Long userId, CartItemForm cartItemForm){

        try {
            //유저Id를 이용하여 장바구니 생성
            CartDto cartDto = shopRepositoryCustom.findCartIdByUserId(userId);

            //장바구니 정보가 없다면 새로운 장바구니 생성 후 유저Id를 다시 받아옴
            if (cartDto == null) {
                CartForm cartForm = new CartForm();
                cartForm.setUserId(userId);
                Long newCartId = cartRegister(cartForm);
                cartItemForm.setCartId(newCartId);
            } else {
                //장바구니 존재시, 해당 장바구니로 진행
                cartItemForm.setCartId(cartDto.getId());
            }

            //장바구니에 상품이 존재 하는 지 확인
            boolean itemExistsInCart = shopRepositoryCustom.checkGoodsId(cartItemForm.getGoodsId(), userId, cartItemForm.getCartId());

            //장바구니에 상품이 존재하면 수량 업데이트
            if (itemExistsInCart) {
                CartItem cartItem = cartItemRepository.findByCartIdAndGoodsId(cartItemForm.getCartId(), cartItemForm.getGoodsId());
                cartItem.itemCount(cartItemForm);
            } else {
                //상품이 장바구니에 존재 하지 않는 경우 새로운 상품 -> 장바구니에 추가
                cartItemRepository.save(cartItemForm.toEntity());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 장바구니 아이템(상품) 삭제
     * @param cartItemId 카트 아이템 번호
     */
    @Transactional
    public void deleteCartItem(Long cartItemId){
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * 장바구니 아이템(상품) 전체 삭제
     */
    public void deleteAllCartItems() {
        cartItemRepository.deleteAll();
    }

    /**
     * flatMap() : 리스트의 리스트가 있을 때 이를 평탄화하여 단일 리스트로 만들 수 있다.
     * @param userId 장바구니 정보를 검색하기 위한 유저 번호
     * @return 장바구니 상품 목록 DTO
     */
    @Transactional
    public GoodsCartListDto findCartItems(Long userId){
        //장바구니 정보 검색
        CartDto cartDto = shopRepositoryCustom.findCartIdByUserId(userId);
        //검색된 장바구니에 속한, 각 상품의 세부 정보 검색
        List<GoodsCartItemDto> cartItems = shopRepositoryCustom.findGoodsCartItemById(cartDto.getId(), userId);
        //객체를 그룹화(장바구니 ID, 사용자 ID)
        Map<GoodsCartListDto, List<CartItemDetails>> groupedItems = cartItems.stream()
                .collect(groupingBy(o -> new GoodsCartListDto(o.getCartId(), o.getUserId()),
                        mapping(o -> new CartItemDetails(
                                        o.getId(), o.getCartItemQuantity(), o.getGoodsId(), o.getGoodsName(), o.getGoodsPrice(),
                                        o.getGoodsMainImgId(), o.getGoodsMainImgName(), o.getGoodsMainImgPath(), o.getGoodsMainImgUuid()),
                                toList())));
        //그룹화된 상품들을 합쳐 하나의 목록으로 만든다.
        List<CartItemDetails> mergedItems = groupedItems.values().stream()
                .flatMap(cartItemDetails -> cartItemDetails.stream())
                .collect(Collectors.toList());
        return new GoodsCartListDto(cartDto.getId(), userId, mergedItems);
    }
}
