package com.example.dw.repository.order;


import com.example.dw.domain.dto.order.OrderItemReviewListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderReviewRepositoryCustom {

    Page<OrderItemReviewListDto> findAllReview(Pageable pageable, Long id);
}
