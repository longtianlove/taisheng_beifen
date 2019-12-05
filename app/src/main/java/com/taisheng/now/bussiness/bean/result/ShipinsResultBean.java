package com.taisheng.now.bussiness.bean.result;

import com.taisheng.now.bussiness.bean.post.MoreShipinPostBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2019/7/8.
 */

public class ShipinsResultBean {
    public List<ShipinBean> records=new ArrayList<>();
    public int total;
    public int size;
    public int current;
    public boolean searchCount;
    public int pages;


}
