package com.taisheng.now.bussiness.bean.post;

public class UpdateCartNumberPostBean extends BasePostBean {
    //    "goodsId": "string",
//            "number": 0,
//            "operateType": 0,
//            "productId": "string",
    public String goodsId;
    public int number;


    /**
     * 操作类型 1增加   2减少
     */
    public int operateType;


    public String productId;

}
