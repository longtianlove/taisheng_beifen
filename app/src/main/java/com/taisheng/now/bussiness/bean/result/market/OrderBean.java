package com.taisheng.now.bussiness.bean.result.market;

import java.math.BigDecimal;
import java.util.List;

public class OrderBean {
    public String orderId;
    public String orderSn;
    public BigDecimal totalPrice;
    public int goodsNumber;
    public List<OrderGoodsBean> list;
    public String status;
}
