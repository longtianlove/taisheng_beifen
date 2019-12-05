package com.taisheng.now.bussiness.bean.result.market;

/**
 * Copyright 2019 bejson.com
 */
import java.util.List;

/**
 * Auto-generated: 2019-10-24 9:22:7
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    public GoodsEntity goodsEntity;
    public List<GoodsAttributeEntities> goodsAttributeEntities;
    public List<GoodsProductEntities> goodsProductEntities;
    public List<GoodsSpecificationEntities> goodsSpecificationEntities;
    public int collected;
    public void setGoodsEntity(GoodsEntity goodsEntity) {
        this.goodsEntity = goodsEntity;
    }
    public GoodsEntity getGoodsEntity() {
        return goodsEntity;
    }

    public void setGoodsAttributeEntities(List<GoodsAttributeEntities> goodsAttributeEntities) {
        this.goodsAttributeEntities = goodsAttributeEntities;
    }
    public List<GoodsAttributeEntities> getGoodsAttributeEntities() {
        return goodsAttributeEntities;
    }

    public void setGoodsProductEntities(List<GoodsProductEntities> goodsProductEntities) {
        this.goodsProductEntities = goodsProductEntities;
    }
    public List<GoodsProductEntities> getGoodsProductEntities() {
        return goodsProductEntities;
    }

    public void setGoodsSpecificationEntities(List<GoodsSpecificationEntities> goodsSpecificationEntities) {
        this.goodsSpecificationEntities = goodsSpecificationEntities;
    }
    public List<GoodsSpecificationEntities> getGoodsSpecificationEntities() {
        return goodsSpecificationEntities;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }
    public int getCollected() {
        return collected;
    }

}
