package com.taisheng.now.bussiness.watch.bean.post;

import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.watch.bean.result.ChiyaoBeann;

import java.util.ArrayList;
import java.util.List;

public class SetChiyaoPostBean extends BasePostBean {


    public String deviceId;

//    public String id;
//    public String frequency;
//    public String isOpen;
//    public String isOpenWeek1;
//    public String isOpenWeek2;
//    public String isOpenWeek3;
//    public String isOpenWeek4;
//    public String isOpenWeek5;
//    public String isOpenWeek6;
//    public String isOpenWeek7;
//    public String startTime;
//    public String takepillsData;
//    public String takepillsNum;
//    public String takepillsText;

    public String remindType;
    public List<ChiyaoBeann> watchRemindList = new ArrayList<>();

}
