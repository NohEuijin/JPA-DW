package com.example.dw.repository.goods;

import com.example.dw.domain.dto.goods.GoodsMainImgDto;
import com.example.dw.domain.entity.goods.GoodsMainImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsMainImgRepository extends JpaRepository<GoodsMainImg, Long> {


    //상품번호로 삭제
    void deleteById(Long id);


    Optional<GoodsMainImgDto> findByGoodsId(Long goodsId);
}
