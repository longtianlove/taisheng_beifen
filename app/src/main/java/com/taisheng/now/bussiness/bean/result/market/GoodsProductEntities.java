package com.taisheng.now.bussiness.bean.result.market;
/**
 * Copyright 2019 bejson.com
 */

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2019-10-24 9:22:7
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class GoodsProductEntities {

    public String id;
    public String goodsId;
    public List<String> specifications;
    public BigDecimal price;
    public int number;
    public String url;
    public String createTime;
    public String updateTime;
    public int deleted;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsId() {
        return goodsId;
    }

    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }
    public List<String> getSpecifications() {
        return specifications;
    }



    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
    public int getDeleted() {
        return deleted;
    }

}