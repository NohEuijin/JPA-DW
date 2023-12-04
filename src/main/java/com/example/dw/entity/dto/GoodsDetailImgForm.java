package com.example.dw.entity.dto;

import com.example.dw.entity.goods.Goods;
import com.example.dw.entity.goods.GoodsDetailImg;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GoodsDetailImgForm {

    private Long id;
    private String goodsDetailImgName;
    private String goodsDetailImgPath;
    private String goodsDetailImgUuid;
    private Goods goods;


    @Builder
    public GoodsDetailImgForm(Long id, String goodsDetailImgName, String goodsDetailImgPath, String goodsDetailImgUuid, Goods goods) {
        this.id = id;
        this.goodsDetailImgName = goodsDetailImgName;
        this.goodsDetailImgPath = goodsDetailImgPath;
        this.goodsDetailImgUuid = goodsDetailImgUuid;
        this.goods=goods;

    }


    public GoodsDetailImg toEntity(){
        return GoodsDetailImg.builder()
                .id(id)
                .goodsDetailImgName(goodsDetailImgName)
                .goodsDetailImgPath(goodsDetailImgPath)
                .goodsDetailImgUuid(goodsDetailImgUuid)
                .goods(goods)
                .build();
    }
}
