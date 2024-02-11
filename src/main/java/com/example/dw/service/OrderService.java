package com.example.dw.service;

import com.example.dw.domain.entity.goods.Cart;
import com.example.dw.domain.entity.order.Orders;
import com.example.dw.domain.form.*;
import com.example.dw.repository.goods.CartItemRepository;
import com.example.dw.repository.goods.CartRepository;
import com.example.dw.repository.goods.GoodsRepository;
import com.example.dw.repository.order.OrderItemRepository;
import com.example.dw.repository.order.OrderListRepository;
import com.example.dw.repository.order.OrderRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderListRepository orderListRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final GoodsRepository goodsRepository;

    /**
     * 주문서 작성
     * @param orderForm 주문서 저장을 위한 폼
     * @param session 세션
     * @throws IOException 입출력 예외 처리
     */
    @Transactional
    public void register(OrderForm orderForm, HttpSession session) throws IOException {

        Long userId = (Long)session.getAttribute("userId");
        orderForm.setUserId(userId);
        try {
            Orders order = orderRepository.save(orderForm.toEntity());
            // 저장된 주문의 ID 가져오기
            Long orderId = order.getId();
            // 해당 사용자의 장바구니 ID 검색
            Cart cartId = cartRepository.findCartIdByUsersId(userId);
            //세션에서 주문한 상품 목록 가져오기
            List<GoodsPayListFrom> goodsPayListDtoList = (List<GoodsPayListFrom>)session.getAttribute("goodsPayList");
            // 주문한 상품 목록(반복)
            for(GoodsPayListFrom goodsPayListFrom : goodsPayListDtoList)
            {
                goodsPayListFrom.setOrderId(orderId);
                //주문 상품 저장
                orderItemRepository.save(goodsPayListFrom.toEntity());
                //해당 카트의 모든 상품 삭제
                cartItemRepository.deleteByCartId(cartId.getId());
                //상품 판매량 업데이트
                goodsRepository.updateSaleCount(Integer.valueOf(goodsPayListFrom.getGoodsQuantity()),
                        Long.valueOf(goodsPayListFrom.getGoodsId()));
            }
            // 주문 목록 생성 및 저장
            OrderListForm orderListForm = new OrderListForm();
            orderListForm.setOrderId(orderId);
            orderListRepository.save(orderListForm.toEntity());
            //세션 주문 상품 삭제
            session.removeAttribute("goodsPayList");
            System.out.println("카트 삭제 완료");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 주문서 단건 작성
     * @param orderForm 주문서 저장을 위한 폼
     * @param session 세션
     * @throws IOException 입출력 예외 처리
     */
    @Transactional
    public void registerSingle(OrderForm orderForm, HttpSession session) throws IOException {

        Long userId = (Long)session.getAttribute("userId");
        orderForm.setUserId(userId);
        try {
            Orders order = orderRepository.save(orderForm.toEntity());

            Long orderId = order.getId();

            List<GoodsPaySingleFrom> goodsPaySingleDto = (List<GoodsPaySingleFrom>)session.getAttribute("goodsPaySingle");
            for(GoodsPaySingleFrom goodsPaySingleFrom : goodsPaySingleDto)
            {
                goodsPaySingleFrom.setOrderId(orderId);
                //주문 상품 저장
                orderItemRepository.save(goodsPaySingleFrom.toEntity());
            }
            OrderListForm orderListForm = new OrderListForm();
            orderListForm.setOrderId(orderId);
            orderListRepository.save(orderListForm.toEntity());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
