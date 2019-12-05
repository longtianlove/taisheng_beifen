package com.taisheng.now.bussiness.watch.bean.result;

import java.util.ArrayList;
import java.util.List;

public class SosListResultBean {
    public List<SosLIstBean> records=new ArrayList<>();
    public int total;
    public int size;
    public int current;
    public boolean searchCount;
    public int pages;
}
