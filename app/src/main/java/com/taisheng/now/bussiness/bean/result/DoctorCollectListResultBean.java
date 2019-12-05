package com.taisheng.now.bussiness.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2019/7/12.
 */

public class DoctorCollectListResultBean {

    public List<DoctorBean> records = new ArrayList<>();
    public int total;
    public int size;
    public int current;
    public boolean searchCount;
    public int pages;
}
