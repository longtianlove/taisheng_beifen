package com.taisheng.now.bussiness.watch.bean.result;

import com.taisheng.now.bussiness.bean.result.ArticleBean;

import java.util.ArrayList;
import java.util.List;

public class XueYaDayResultBean {
    public List<XueYaDayBean> records=new ArrayList<>();
    public int total;
    public int size;
    public int current;
    public boolean searchCount;
    public int pages;
}
