package com.taisheng.now.bussiness.watch.bean.post;

import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.watch.bean.result.TongxunluliistBean;

import java.util.ArrayList;
import java.util.List;

public class SetphonbookPostBean extends BasePostBean {
    public String deviceId;
    public String type;
    public List<TongxunluliistBean> list=new ArrayList<>();
}
