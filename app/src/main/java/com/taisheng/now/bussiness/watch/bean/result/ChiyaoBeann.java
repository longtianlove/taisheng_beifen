package com.taisheng.now.bussiness.watch.bean.result;

import com.taisheng.now.bussiness.login.UserInstance;

public class ChiyaoBeann {


//    public String dateTime;
//    public String clientId;
//    public String takepillsNum;
//    public String frequency;
//    public String takepillsData;
//    public String takepillsText;
//    public String isOpen;
//    public String createTime;
//    public String isOpenWeek1;
//    public String startTime;
//    public String isOpenWeek2;
//    public String id;
//    public String isOpenWeek3;
//    public String isOpenWeek4;
//    public String isOpenWeek5;
//    public String takepillsType;
//    public String isOpenWeek6;
//    public String isOpenWeek7;


    public String frequency;
    public String isOpen;
    public String isOpenWeek1 = "1";
    public String isOpenWeek2 = "1";
    public String isOpenWeek3 = "1";
    public String isOpenWeek4 = "1";
    public String isOpenWeek5 = "1";
    public String isOpenWeek6 = "1";
    public String isOpenWeek7 = "1";
    public String remindText;
    public String remindTime;
    public String remindType;
    public String voiceData;
    public String userId= UserInstance.getInstance().getUid();
}
