package com.example.dw.repository.order;

import com.example.dw.domain.dto.order.*;
import com.example.dw.domain.entity.order.OrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static com.example.dw.domain.entity.order.QOrders.orders;
import static com.example.dw.domain.entity.user.QUsers.users;
import static com.example.dw.domain.entity.order.QOrderItem.orderItem;
import static com.example.dw.domain.entity.goods.QGoodsMainImg.goodsMainImg;
import static com.example.dw.domain.entity.goods.QGoods.goods;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryImp implements OrderRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * @param pageable 페이징
     * @param userId 유저번호
     * @return 리뷰 리스트 조회
     */
    @Override
    public Page<OrderListResultDto> findAllbyId(Pageable pageable, Long userId) {

        List<OrderDto> content = jpaQueryFactory
                .select(new QOrderDto(
                        orders.id,
                        orders.orderRegisterDate,
                        users.id
                ))
                .from(orders)
                .leftJoin(orders.users,users)
                .where(orders.users.id.eq(userId))
                .orderBy(orders.orderRegisterDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        List<OrderListResultDto> result =
                content.stream().map(orderDto -> {

                    List<OrderItemDto> orderItemDtos = jpaQueryFactory
                            .select(new QOrderItemDto(
                                    orderItem.id,
                                    orderItem.orderQuantity,
                                    orderItem.orderPrice,
                                    orderItem.goods.id,
                                    orderItem.goods.goodsName,
                                    goodsMainImg.id,
                                    goodsMainImg.goodsMainImgName,
                                    goodsMainImg.goodsMainImgPath,
                                    goodsMainImg.goodsMainImgUuid,
                                    orderItem.state

                            ))
                            .from(orderItem)
                            .leftJoin(orderItem.goods.goodsMainImg,goodsMainImg)
                            .where(orderItem.orders.id.eq(orderDto.getId()))
                            .fetch();

                    List<OrderItemDto> orderItemDtoList = orderItemDtos.stream().map(
                            orderItemDtoLists -> new OrderItemDto(
                                    orderItemDtoLists.getId(),
                                    orderItemDtoLists.getOrderQuantity(),
                                    orderItemDtoLists.getOrderPrice(),
                                    orderItemDtoLists.getGoodsid(),
                                    orderItemDtoLists.getGoodsName(),
                                    orderItemDtoLists.getGoodsMainImgId(),
                                    orderItemDtoLists.getGoodsMainImgName(),
                                    orderItemDtoLists.getGoodsMainImgPath(),
                                    orderItemDtoLists.getGoodsMainImgUuid(),
                                    orderItemDtoLists.getState()
                            )).collect(toList());

                    return new OrderListResultDto(
                            orderDto.getId(),
                            orderDto.getOrderRegisterDate(),
                            orderDto.getUserId(),
                            orderItemDtoList
                    );

                }).collect(toList());

        Long counts = jpaQueryFactory
                .select(orders.id.count())
                .from(orders)
                .where(orders.users.id.eq(userId))
                .fetchOne();

        System.out.println(counts);

        return new PageImpl<>(result, pageable,counts);
    }
}
