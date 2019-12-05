package com.taisheng.now.push;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;


import com.taisheng.now.EventManage;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.util.LogUtil;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by long on 17/5/1.
 */

public class XMPushManagerInstance {
    private static XMPushManagerInstance instance;

    private XMPushManagerInstance() {

    }

    public static XMPushManagerInstance getInstance() {
        if (instance == null) {
            instance = new XMPushManagerInstance();
        }
        return instance;
    }

    public static Context mcontext;

    /**
     * 。其中AppId和AppKey是客户端的身份标识，在客户端SDK初始化时使用；
     * AppSecret是服务器端的身份标识，在使用Server SDK向客户端发送消息时使用。
     */
    public static final String APP_ID = "2882303761518045531";
    public static final String APP_KEY = "5931804539531";
    private static final String APP_SECRET = "rKXnrcxuyauk+w9b1r1Uyw==";



    private void openLogger() {
        //打开Logcat调试日志
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                LogUtil.e("content=" + content + " throwable=" + t);
            }

            @Override
            public void log(String content) {
                LogUtil.e("content=" + content);
            }
        };
        Logger.setLogger(SampleAppLike.mcontext, newLogger);
    }

    public void init() {
        try {
            EventBus.getDefault().register(this);
        }catch (Exception e){
            EventBus.getDefault().unregister(this);
            EventBus.getDefault().register(this);
        }
        mcontext=SampleAppLike.mcontext;
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(SampleAppLike.mcontext, APP_ID, APP_KEY);

        }
        openLogger();
    }

    private boolean shouldInit() {
        @SuppressLint("WrongConstant") ActivityManager am = ((ActivityManager) SampleAppLike.mcontext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = SampleAppLike.mcontext.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }



    public void resume() {
        MiPushClient.resumePush(SampleAppLike.mcontext, null);
    }

    public void stop() {
//        MiPushClient.pausePush(PetAppLike.mcontext, null);
        String uid = UserInstance.getInstance().getUid() + "";
            MiPushClient.unsetAlias(SampleAppLike.mcontext, uid, null);
        MiPushClient.unregisterPush(SampleAppLike.mcontext);
        EventBus.getDefault().unregister(this);
    }

//    public static void register(String deviceId,OnResult onResult){
//        MiPushClient.setAlias(PetAppLike.mcontext,deviceId,null);
//        XMPushManagerInstance.onRegisterResult = onResult;
//    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void setUserAccount(EventManage.XMPushRegister event) {
        String uid = UserInstance.getInstance().getUid() + "";
            MiPushClient.setAlias(SampleAppLike.mcontext, uid, null);
    }

    public void againSetUserAccount(EventManage.setAliasFail event){
        String uid = UserInstance.getInstance().getUid() + "";
            MiPushClient.setAlias(SampleAppLike.mcontext, uid, null);
    }

    public String getRegId() {
        return MiPushClient.getRegId(SampleAppLike.mcontext);
    }

}
