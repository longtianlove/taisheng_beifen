package com.taisheng.now.bussiness;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.R;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.bussiness.me.FillInMessageActivity;
import com.taisheng.now.bussiness.me.FillInMessageSecondActivity;
import com.taisheng.now.bussiness.user.LoginActivity;
import com.taisheng.now.push.XMPushManagerInstance;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.util.SPUtil;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by long on 2017/4/13.
 */

public class SplashActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        checkPermission();
    }

    private int times = 0;
    private final int REQUEST_PHONE_PERMISSIONS = 0;

    private void checkPermission() {
        times++;
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.BLUETOOTH);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);


            if (permissionsList.size() != 0) {
                if (times == 1) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_PHONE_PERMISSIONS);
                } else {
                    new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("提示")
                            .setMessage("获取不到授权，APP将无法正常使用，请允许APP获取权限！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                                REQUEST_PHONE_PERMISSIONS);
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).show();
                }
            } else {
                toWhere();
            }
        } else {
            toWhere();
        }
    }

    //判断跳转逻辑
    void toWhere() {
        if (TextUtils.isEmpty(SPUtil.getUid())) {
            SPUtil.putAPP_VERSION(Apputil.getVersionCode() + "");
            SampleAppLike.mainHandler = new Handler(getMainLooper());
            SampleAppLike.mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } else if (TextUtils.isEmpty(SPUtil.getRealname())) {
            SampleAppLike.mainHandler = new Handler(getMainLooper());
            SampleAppLike.mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//            SPUtil.putAPP_VERSION(Apputil.getVersionCode() + "");
//            EventBus.getDefault().register(this);
//            //获取基本信息
//            UserInstance.getInstance().getUserInfo();
                    MiPushClient.registerPush(SampleAppLike.mcontext, XMPushManagerInstance.APP_ID, XMPushManagerInstance.APP_KEY);
                    SPUtil.putAPP_VERSION(Apputil.getVersionCode() + "");
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, FillInMessageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } else if (TextUtils.isEmpty(SPUtil.getHEIGHT())) {
            SampleAppLike.mainHandler = new Handler(getMainLooper());
            SampleAppLike.mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//            SPUtil.putAPP_VERSION(Apputil.getVersionCode() + "");
//            EventBus.getDefault().register(this);
//            //获取基本信息
//            UserInstance.getInstance().getUserInfo();
                    MiPushClient.registerPush(SampleAppLike.mcontext, XMPushManagerInstance.APP_ID, XMPushManagerInstance.APP_KEY);
                    SPUtil.putAPP_VERSION(Apputil.getVersionCode() + "");
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, FillInMessageSecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } else {

            SampleAppLike.mainHandler = new Handler(getMainLooper());
            SampleAppLike.mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    MiPushClient.registerPush(SampleAppLike.mcontext, XMPushManagerInstance.APP_ID, XMPushManagerInstance.APP_KEY);
                    SPUtil.putAPP_VERSION(Apputil.getVersionCode() + "");
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }

    }


//    //网络获取用户信息成功
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
//    public void next(EventManage.getUserInfoEvent event) {
//
//        EventBus.getDefault().unregister(this);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//
//        }
//        Intent intent = new Intent();
//        intent.setClass(SplashActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
