package com.taisheng.now.application;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.multidex.MultiDex;
import me.jessyan.autosize.AutoSizeConfig;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.request.target.ViewTarget;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.previewlibrary.ZoomMediaLoader;
import com.taisheng.now.Constants;
import com.taisheng.now.Environment;
import com.taisheng.now.R;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.util.ImageLoader;
import com.tencent.bugly.Bugly;

import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;



public class SampleAppLike extends DefaultApplicationLike {
    public static String TAG = "com.taisheng.now";
    public SampleAppLike(Application application, int tinkerFlags,
                         boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                         long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    public static Application mcontext;
    public static Environment environment;//当前环境
    public static Handler mainHandler;
    public static Handler handler = new Handler();
    private static volatile Activity mCurrentActivity;


    @Override
    public void onCreate() {
        super.onCreate();
        mcontext=getApplication();
        environment=Environment.Debug;
        ViewTarget.setTagId(R.id.glide_tag);//图片加载错误处理
//        WeChatManagerInstance.getInstance().registToWx(mcontext);
        if (isMainProcess(getApplication())) {
            Fresco.initialize(mcontext);

            /***** Beta高级设置 *****/
            /**
             * true表示app启动自动初始化升级模块;
             * false不会自动初始化;
             * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
             * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
             */
            Beta.autoInit = true;

            /**
             * true表示初始化时自动检查升级;
             * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
             */
            Beta.autoCheckUpgrade = true;

            /**
             * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
             */
            Beta.upgradeCheckPeriod = 60 * 1000;

            /**
             * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
             */
            Beta.initDelay = 1 * 1000;

            /**
             * 设置通知栏大图标，largeIconId为项目中的图片资源;
             */
            Beta.largeIconId = R.drawable.icon_app;

            /**
             * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
             */
            Beta.smallIconId = R.drawable.icon_app;

            /**
             * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
             * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
             */
            Beta.defaultBannerId = R.drawable.icon_app;

//            /**
//             * 设置sd卡的Download为更新资源保存目录;
//             * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
//             */
//            Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            /**
             * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
             */
            Beta.showInterruptedStrategy = true;

            /**
             * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
             * 不设置会默认所有activity都可以显示弹窗;
             */
            Beta.canShowUpgradeActs.add(MainActivity.class);



            // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
            // 调试时，将第三个参数改为true
            // 调试时，将第三个参数改为true
            Bugly.init(getApplication(), Constants.BUGLY_APP_ID, true);

            ZoomMediaLoader.getInstance().init(new ImageLoader());


            //在使用SDK各组件之前初始化context信息，传入ApplicationContext
            SDKInitializer.initialize(mcontext);
            //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
            //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
            SDKInitializer.setCoordType(CoordType.BD09LL);


            registerActivityLifecycleCallback(new SwitchBackgroundCallbacks());


        }
        //适配页面
        AutoSizeConfig.getInstance().getUnitsManager()//适配
                .setSupportDP(true)
                .setSupportSP(true);
        AutoSizeConfig.getInstance().setCustomFragment(true);





    }

    public boolean isMainProcess(Context context) {
        return Apputil.getCurProcessName(context).equals("com.taisheng.now");
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    public static synchronized Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public static synchronized void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }

    private class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setCurrentActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            setCurrentActivity(null);
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
