package com.taisheng.now.bussiness.market.gouwuche;

/**
 * Created by AYD on 2016/11/22.
 * <p>
 * 购物车
 */
/**
 * Copyright 2019 bejson.com
 */


import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2019-10-24 11:15:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class NewShoppingCartBean {

    public String productId;
    public String goodsId;
    public String goodsSn;
    public String updateTime;
    public int resultNumber;
    public String userId;
    public List<String> specifications;
    public int number;
    public String picUrl;
    public int deleted;
    public String createTime;
    public BigDecimal price;
    public String checked;
    public String id;
    public String goodsName;

    public int getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(int resultNumber) {
        this.resultNumber = resultNumber;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductId() {
        return productId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }
    public String getGoodsSn() {
        return goodsSn;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public List<String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public String getPicUrl() {
        return picUrl;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
    public int getDeleted() {
        return deleted;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateTime() {
        return createTime;
    }



    public void setChecked(String checked) {
        this.checked = checked;
    }
    public String getChecked() {
        return checked;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsName() {
        return goodsName;
    }

}