package com.example.dw.repository.order;

import com.example.dw.domain.dto.order.OrderListResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    //해당 유저가 주문한 주문내역 전체 조회
    Page<OrderListResultDto> findAllbyId(Pageable pageable, Long userId);
}
