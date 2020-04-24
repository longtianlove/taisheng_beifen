package com.taisheng.now.http;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.taisheng.now.Constants;
import com.taisheng.now.Environment;
import com.taisheng.now.application.SampleAppLike;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.login.LoginActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.push.XMPushManagerInstance;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;


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
        DialogUtil.new_closeProgress();
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
                if (message.code == Constants.USER_NOTEXIST) {
                    //注销小米账号
                    XMPushManagerInstance.getInstance().stop();
                    ToastUtil.showTost("用户不存在");
                    UserInstance.getInstance().clearUserInfo();
                    Intent intent = new Intent(SampleAppLike.mcontext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SampleAppLike.mcontext.startActivity(intent);
                    return;
                }
                if (message.code == Constants.USER_FREEZE) {
                    //注销小米账号
                    XMPushManagerInstance.getInstance().stop();
                    ToastUtil.showTost("用户已被冻结");
                    UserInstance.getInstance().clearUserInfo();
                    Intent intent = new Intent(SampleAppLike.mcontext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SampleAppLike.mcontext.startActivity(intent);
                    return;
                }
                if (message.code == Constants.USER_ACCOUNT_SOURCE_ERROR) {
                    //注销小米账号
                    XMPushManagerInstance.getInstance().stop();
                    ToastUtil.showTost("用户来源错误");
                    UserInstance.getInstance().clearUserInfo();
                    Intent intent = new Intent(SampleAppLike.mcontext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SampleAppLike.mcontext.startActivity(intent);
                    return;
                }
                if (message.code == Constants.TOKEN_DIFFERENCE || message.code == Constants.USER_DATABASE_TOKEN_NOT_EXIST) {
                    //注销小米账号
                    XMPushManagerInstance.getInstance().stop();
                    ToastUtil.showTost("身份过期，请重新登录");
                    UserInstance.getInstance().clearUserInfo();
                    Intent intent = new Intent(SampleAppLike.mcontext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SampleAppLike.mcontext.startActivity(intent);
                    return;
                }
                if (message.code == Constants.DEVICE_OFFLINE) {
                    ToastUtil.showTost("手表不在线");
                    return;
                }

                if (message.code == Constants.DEVICE_NOT_EXIST) {
                    ToastUtil.showTost("手表信息不存在");
                    return;
                }


                if (SampleAppLike.environment == Environment.Release) {
                    //上线状态下：此处加了统一对网络请求的异常捕获，不让用户崩溃。然后上传异常信息到bugly。
                    try {
                        onSuccess(response, message);
//                        if (Constants.HTTP_SUCCESS != message.code) {
//                            ToastUtil.showAtCenter("服务器异常---" + message.code);
//                        }
                    } catch (Exception e) {
//                        DialogUtil.closeProgress();
                        Log.e("taishengcallback", e.getMessage());
                        CrashReport.postCatchedException(e);
//                        Toast.makeText(PetAppLike.mcontext, "系统错误，请稍后……", Toast.LENGTH_SHORT).show();
//                        Log.e(PetAppLike.TAG, "callback出错了" + e.getMessage());
                    }
                } else {
                    onSuccess(response, message);
//                    if (Constants.HTTP_SUCCESS != message.code) {
//                        ToastUtil.showAtCenter("服务器异常---" + message.code);
//                    }
                }
                return;
            }
        }

        ToastUtil.showTost("网络错误");

        onFail(call, null);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        DialogUtil.new_closeProgress();
        onFail(call, t);
    }

    public abstract void onSuccess(Response<T> response, T message);

    public abstract void onFail(Call<T> call, Throwable t);
}
