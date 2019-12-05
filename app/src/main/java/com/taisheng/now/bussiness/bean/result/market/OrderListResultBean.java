package com.taisheng.now.bussiness.bean.result.market;

import com.taisheng.now.bussiness.bean.result.ArticleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2019/7/12.
 */

public class OrderListResultBean {



    public List<OrderBean> records=new ArrayList<>();
    public int total;
    public int size;
    public int current;
    public boolean searchCount;
    public int pages;
}
