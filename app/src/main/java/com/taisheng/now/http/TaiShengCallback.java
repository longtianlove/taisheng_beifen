package com.taisheng.now.http;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.taisheng.now.Constants;
import com.taisheng.now.Environment;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.user.LoginActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.push.XMPushManagerInstance;
import com.taisheng.now.util.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;


import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by long on 17/4/7.
 */
@SuppressLint("WrongConstant")
public abstract class TaiShengCallback<T extends BaseBean> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() >= 200 && response.code() < 300) {
            T message = response.body();
//            if (message != null) {
//
//                //如果token过期，直接跳转到登录页面
//                message.parseResult();
//
//                }
//                //如果宠物不存在了，直接退到添加宠物页面
//                if (EC_PET_NOT_EXIST == ret) {
//                    MainActivity.getLocationWithOneMinute=false;
////                    ToastUtil.showTost("请先填写您的宠物信息");
//                    onFail(call, null);
//
//                    PetInfoInstance.getInstance().clearPetInfo();
////                    DeviceInfoInstance.getInstance().clearDeviceInfo();
//
//
//                    Intent intent = new Intent(PetAppLike.mcontext, AddPetInfoActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    PetAppLike.mcontext.startActivity(intent);
//
//                    return;
//                }

            if (message != null) {
                if(message.code== Constants.TOKEN_DIFFERENCE){
                    //注销小米账号
                    XMPushManagerInstance.getInstance().stop();
                    ToastUtil.showTost("身份过期，请重新登录");
                    UserInstance.getInstance().clearUserInfo();
                    Intent intent = new Intent(SampleAppLike.mcontext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SampleAppLike.mcontext.startActivity(intent);
                    return;
                }
                if (SampleAppLike.environment == Environment.Release) {
                    //上线状态下：此处加了统一对网络请求的异常捕获，不让用户崩溃。然后上传异常信息到bugly。
                    try {
                        onSuccess(response, message);
                    } catch (Exception e) {
//                        DialogUtil.closeProgress();
                        Log.e("taishengcallback",e.getMessage());
                        CrashReport.postCatchedException(e);
//                        Toast.makeText(PetAppLike.mcontext, "系统错误，请稍后……", Toast.LENGTH_SHORT).show();
//                        Log.e(PetAppLike.TAG, "callback出错了" + e.getMessage());
                    }
                } else {
                    onSuccess(response, message);
                }
                return;
            }
        }

        ToastUtil.showTost("网络错误");

        onFail(call, null);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail(call, t);
    }

    public abstract void onSuccess(Response<T> response, T message);

    public abstract void onFail(Call<T> call, Throwable t);
}
