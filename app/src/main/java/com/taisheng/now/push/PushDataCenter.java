package com.taisheng.now.push;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.taisheng.now.Constants;
import com.taisheng.now.application.SampleAppLike;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.WatchMainActivity;
import com.taisheng.now.bussiness.watch.bean.post.GetDeviceInfoPostBean;
import com.taisheng.now.bussiness.watch.bean.result.WatchListBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DateUtils;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.view.AppDialog;


import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Response;

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

    public static RemoteMessageBean formatBean;
    public static boolean fromXiaomi=false;

    public void notifyData(String message) {

        if (!SPUtil.getHomePage()) {
            //如果不能进主页,直接不处理了
            return;
        }

//        ToastUtil.showTost("收到小米推送消息：" + message);
        formatBean = JSON.parseObject(message, RemoteMessageBean.class);
        if (formatBean == null) {
            return;
        }
        formatBean.createTime= DateUtils.timeStamp2Date(formatBean.createTime,"yyyy-MM-dd HH:mm:ss");

        GetDeviceInfoPostBean bean = new GetDeviceInfoPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = formatBean.deviceId;
        ApiUtils.getApiService().getDeviceInfo(bean).enqueue(new TaiShengCallback<BaseBean<WatchListBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<WatchListBean>> response, BaseBean<WatchListBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        WatchListBean bean1 = message.result;
                        WatchInstance.getInstance().deviceId = bean1.deviceId;
                        SPUtil.putDeviced(bean1.deviceId);
                        WatchInstance.getInstance().deviceNickName = bean1.nickName;
                        WatchInstance.getInstance().relationShip = bean1.deviceRelation;
                        WatchInstance.getInstance().realName = bean1.holderName;
                        WatchInstance.getInstance().idcard = bean1.idcard;
                        WatchInstance.getInstance().phoneNumber = bean1.mobilePhone;
                        WatchInstance.getInstance().createTime = bean1.createTime;
                        WatchInstance.getInstance().headUrl = bean1.url;
                        fromXiaomi=true;
                        Intent intent = new Intent(SampleAppLike.getCurrentActivity(), WatchMainActivity.class);
                        SampleAppLike.getCurrentActivity().startActivity(intent);
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<WatchListBean>> call, Throwable t) {

            }
        });


    }


}
