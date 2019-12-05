package com.taisheng.now.bussiness.bean.result.market;

import com.taisheng.now.bussiness.bean.result.ArticleBean;

import java.util.ArrayList;
import java.util.List;

public class DizhilistResultBean {
    public List<DizhilistBean> records=new ArrayList<>();
    public int total;
    public int size;
    public int current;
    public boolean searchCount;
    public int pages;
}
