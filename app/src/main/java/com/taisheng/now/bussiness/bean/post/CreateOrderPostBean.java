package com.taisheng.now.bussiness.bean.post;

import com.taisheng.now.bussiness.bean.result.xiadanshangpinBean;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderPostBean extends BasePostBean {

    public List<xiadanshangpinBean> goodsList = new ArrayList<>();

    public String addressId;

    public String flag;

    public String postFeeId;

    public String couponId;

    public String message;
}
