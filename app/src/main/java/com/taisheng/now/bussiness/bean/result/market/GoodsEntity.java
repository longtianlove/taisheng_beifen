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
public class GoodsEntity {

    public String id;
    public String goodsSn;
    public String name;
    public String categoryId;
    public String brandId;
    public List<String> gallery;
    public String keywords;
    public String brief;
    public int onSale;
    public int sortOrder;
    public String picUrl;
    public String shareUrl;
    public int newGoods;
    public int hotGoods;
    public String unit;
    public BigDecimal counterPrice;
    public BigDecimal retailPrice;
    public String detail;
    public String createTime;
    public String updateTime;
    public int deleted;
    public int scoreGoods;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }


}
