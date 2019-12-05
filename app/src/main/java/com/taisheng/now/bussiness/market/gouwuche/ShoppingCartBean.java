package com.taisheng.now.bussiness.market.gouwuche;

import java.math.BigDecimal;

/**
 * Created by AYD on 2016/11/22.
 * <p>
 * 购物车
 */
public class ShoppingCartBean {

    public  String id;
    public  String imageUrl;
    public  String shoppingName;

    public String goodsId;

    public  int dressSize;
    public  String attribute;

    public BigDecimal price;

    public boolean isChoosed;
    public boolean isCheck = false;
    public  int count;

    public String productId;



    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public ShoppingCartBean() {
    }

    public ShoppingCartBean(String id, String shoppingName, String attribute, int dressSize,
                            BigDecimal price, int count) {
        this.id = id;
        this.shoppingName = shoppingName;
        this.attribute = attribute;
        this.dressSize = dressSize;
        this.price = price;
        this.count = count;

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShoppingName() {
        return shoppingName;
    }

    public void setShoppingName(String shoppingName) {
        this.shoppingName = shoppingName;
    }


    public int getDressSize() {
        return dressSize;
    }

    public void setDressSize(int dressSize) {
        this.dressSize = dressSize;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}
