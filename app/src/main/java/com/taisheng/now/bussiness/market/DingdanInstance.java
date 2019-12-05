package com.taisheng.now.bussiness.market;

import com.taisheng.now.bussiness.bean.result.JifenzhuanquBean;
import com.taisheng.now.bussiness.bean.result.xiadanshangpinBean;
import com.taisheng.now.bussiness.user.UserInstance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DingdanInstance {

    private static DingdanInstance dingdanInstance;


    private DingdanInstance() {
    }

    public static DingdanInstance getInstance() {
        if (dingdanInstance == null) {
            dingdanInstance = new DingdanInstance();
        }
        return dingdanInstance;
    }


    public List<xiadanshangpinBean> putongshangpindingdanList = new ArrayList<>();

    public List<xiadanshangpinBean> jifenshangpindingdanList = new ArrayList<>();





    public String addressId;


    //从哪里来
    public String flag;

    //什么类型的产品，是否可以积分兑换 0是 1否',
    public int scoreGoods;

    public String postFeeId;

    public String couponId;


    //    优惠券返回值问题
    public String tv_discount;


    public String zongjia;


    //订单id
    public String orderId;


    public String gangzhifu_orderId;
    public BigDecimal gangzhifu_zongjia;


    //地址相关
    //进列表新增地址，还是手动在DizhiActivity中新增地址
    public String fromDizhi = "1";
    public String name;
    public String phone;
    public String address;

//    public String defaultAddress="0";




    public int youfei=10;

}
