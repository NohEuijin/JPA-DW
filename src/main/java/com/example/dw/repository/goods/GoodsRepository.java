package com.example.dw.repository.goods;

import com.example.dw.domain.entity.goods.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

        //goods(상품) 기본키로 상품 검색
        Optional<Goods> findById(Long id);

        /**
         * 판매량 조회
         * @param sale 판매량
         * @param id 상품 번호
         */
        @Modifying
        @Query("Update Goods g set g.saleCount =g.saleCount+:sale where g.id=:id")
        void updateSaleCount(@Param("sale") int sale, @Param("id") Long id);
}
