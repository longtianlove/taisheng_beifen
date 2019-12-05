package com.taisheng.now;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.previewlibrary.ZoomMediaLoader;
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

    @Override
    public void onCreate() {
        super.onCreate();

        mcontext=getApplication();

        environment=Environment.Release;
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
        }




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




//     public void aboutBugly(){
//        Context context = getApplicationContext();
//// 获取当前包名
//        String packageName = context.getPackageName();
//// 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//// 设置是否为上报进程
//        CrashReport.UserStrategy strategy = now CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        //初始化bugly
//        CrashReport.initCrashReport(getApplicationContext(), "5eb6432b7a", true);
//
//    }
//    /**
//     * 获取进程号对应的进程名
//     *
//     * @param pid 进程号
//     * @return 进程名
//     */
//    private static String getProcessName(int pid) {
//        BufferedReader reader = null;
//        try {
//            reader = now BufferedReader(now FileReader("/proc/" + pid + "/cmdline"));
//            String processName = reader.readLine();
//            if (!TextUtils.isEmpty(processName)) {
//                processName = processName.trim();
//            }
//            return processName;
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        } finally {
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException exception) {
//                exception.printStackTrace();
//            }
//        }
//        return null;
//    }

//    private boolean shouldInit() {
//        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
//        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
//        String mainProcessName = getPackageName();
//        int myPid = android.os.Process.myPid();
//        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
//            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
