package com.taisheng.now.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.taisheng.now.application.SampleAppLike;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;
import okhttp3.Call;
import okhttp3.Response;

/**
 * app工具类
 */
public class AppUtils {

    /**
     * 获取应用的版本名称
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),  PackageManager.GET_ACTIVITIES);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取应用的版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),  PackageManager.GET_ACTIVITIES);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getPackageName(Context context) {
        String packageName = null;
        try {
            packageName = context.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 全屏
     */
    public static void fullScreen(Activity context) {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 横竖屏判断
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static String getEnCachePath() {
        File externalCacheDir = SampleAppLike.mcontext.getExternalCacheDir();
        String path = "";
        if (externalCacheDir == null) {
            externalCacheDir =  SampleAppLike.mcontext.getCacheDir();
            if (externalCacheDir != null) {
                path = externalCacheDir.getAbsolutePath();//绝对路径
            } else {
                path = Environment.getDataDirectory().getPath();//公共文件 相对路径
            }
        } else {
            path = externalCacheDir.getAbsolutePath();
        }
        return path;

    }

    public static String getFilePath() {
        File externalCacheDir =  SampleAppLike.mcontext.getFilesDir();
        String path = externalCacheDir.getPath() + "/robust";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        } else {

        }
        return path;
    }

    /**
     * 查看服务是否开启
     */
    public static Boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * app是否在后台运行
     * @param context
     * @return
     */
    public static boolean isAppInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) { // Android5.0及以后的检测方法
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    /**
     * 读取app包名
     *
     * @param context
     */
    private void loadApps(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(intent, 0);
        //for循环遍历ResolveInfo对象获取包名和类名
        for (int i = 0; i < apps.size(); i++) {
            ResolveInfo info = apps.get(i);
            String packageName = info.activityInfo.packageName;
            CharSequence cls = info.activityInfo.name;
            CharSequence name = info.activityInfo.loadLabel(context.getPackageManager());
            Log.e("！！！！！", name + "----" + packageName + "----" + cls);
        }
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     *  判断是否为平板
     *  @param context
     *  @return 平板返回 True，手机返回 False
     */
    public static boolean isPad2(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            return true;
        }
        return false;
    }
}
