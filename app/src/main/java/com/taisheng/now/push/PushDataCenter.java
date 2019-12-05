package com.taisheng.now.push;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.taisheng.now.util.SPUtil;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by long on 2017/5/15.
 */

public class PushDataCenter {
    private static PushDataCenter instance;

    private PushDataCenter() {

    }

    public static PushDataCenter getInstance() {
        if (instance == null) {
            instance = new PushDataCenter();
        }
        return instance;
    }

    public static final String USER = "user";
    public static final String DEVICE = "device";
    public static final String PET = "pet";
    public static final String EXTRA = "extra";

    public static class User {
        public static final String REMOTE_LOGIN = "remote-login";
    }

    public static class Device {
        public static final String OFFLINE = "offline";
        public static final String ONLINE = "online";
        public static final String COMMON_BATTERY = "common-battery";
        public static final String LOW_BATTERY = "low-battery";
        public static final String ULTRA_LOW_BATTERY = "ultra-low-battery";

    }

    public static class Pet {
        public static final String LOCATIONCHANGE = "location-change";
        public static final String NOT_HOME = "not-home";//宠物离开家了
        public static final String AT_HOME = "home";//宠物在家了
        public static final String OUTDOOR_IN_PROTECTED = "outdoor_in_protected";//回到户外保护范围
        public static final String OUTDOOR_OUT_PROTECTED = "outdoor_out_protected";//脱离户外保护范围
    }

    RemoteMessageBean formatBean;

    public void notifyData(String message) {

        if (!SPUtil.getHomePage()) {
            //如果不能进主页,直接不处理了
            return;
        }


//        ToastUtil.showTost("收到小米推送消息：" + message);
//        formatBean = JSON.parseObject(message, RemoteMessageBean.class);
//        if (formatBean == null) {
//            return;
//        }
//        long pet_id;
//        if(formatBean.data.get("pet_id") instanceof Integer){
//            pet_id=((Integer)formatBean.data.get("pet_id")).longValue();
//        }else{
//            pet_id = (long) formatBean.data.get("pet_id");
//
//        }
//        if (formatBean.data != null && pet_id == UserInstance.getInstance().pet_id) {
//            switch (formatBean.type) {
//                case USER:
//                    dealUser();
//                    break;
//                case DEVICE:
//                    dealDevice();
//                    break;
//                case PET:
//                    dealPet();
//                    break;
//                case EXTRA:
//                    dealExtra();
//                    break;
//            }
//        } else if (formatBean.data != null && pet_id != UserInstance.getInstance().pet_id) {
//            switch (formatBean.type) {
//                case PET:
//                    dealOtherPet(pet_id);
//                    break;
//            }
//        }else{
//            Log.e("xiaomipush-error",formatBean.getClass()+"");
//        }
    }



}
