package com.example.dw.domain.entity.goods;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGoods is a Querydsl query type for Goods
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoods extends EntityPathBase<Goods> {

    private static final long serialVersionUID = 1739350835L;

    public static final QGoods goods = new QGoods("goods");

    public final EnumPath<GoodsCategory> goodsCategory = createEnum("goodsCategory", GoodsCategory.class);

    public final StringPath goodsCertify = createString("goodsCertify");

    public final StringPath goodsDetailContent = createString("goodsDetailContent");

    public final ListPath<GoodsDetailImg, QGoodsDetailImg> goodsDetailImg = this.<GoodsDetailImg, QGoodsDetailImg>createList("goodsDetailImg", GoodsDetailImg.class, QGoodsDetailImg.class, PathInits.DIRECT2);

    public final StringPath goodsMade = createString("goodsMade");

    public final ListPath<GoodsMainImg, QGoodsMainImg> goodsMainImg = this.<GoodsMainImg, QGoodsMainImg>createList("goodsMainImg", GoodsMainImg.class, QGoodsMainImg.class, PathInits.DIRECT2);

    public final StringPath goodsModifyDate = createString("goodsModifyDate");

    public final StringPath goodsName = createString("goodsName");

    public final NumberPath<Long> goodsPrice = createNumber("goodsPrice", Long.class);

    public final NumberPath<Long> goodsQuantity = createNumber("goodsQuantity", Long.class);

    public final StringPath goodsRegisterDate = createString("goodsRegisterDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QGoods(String variable) {
        super(Goods.class, forVariable(variable));
    }

    public QGoods(Path<? extends Goods> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGoods(PathMetadata metadata) {
        super(Goods.class, metadata);
    }

}

