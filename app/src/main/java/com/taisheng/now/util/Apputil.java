package com.taisheng.now.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


import com.taisheng.now.SampleAppLike;

import java.util.List;

/**
 * Created by long on 2017/4/17.
 */
@SuppressLint("WrongConstant")
public class Apputil {

    //SD卡下通用的存储路径：SD卡路径下：Android/data/包名；
    public static String sdNormalPath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + Apputil.getPackageName(SampleAppLike.mcontext);
    public static String imei;






    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        int versionCode = 0;
        PackageManager pm = SampleAppLike.mcontext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(SampleAppLike.mcontext), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {// android.os.DeadObjectException  by yangwenxin 5.1.0
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }


    public static String getVersionName(Context context) {
        String versionName = "";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(context), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        if (context == null) {
            return "context为null";
        }
        int pid = android.os.Process.myPid();
        @SuppressLint("WrongConstant") ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = mActivityManager.getRunningAppProcesses();
        if (mActivityManager != null && runningAppProcessInfos != null && runningAppProcessInfos.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcessInfos) {
                if (appProcess != null && appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return "";
    }


    /**
     * 手机获取imei的方法
     */
    public static String getIMEI(Context context) {
        if (!TextUtils.isEmpty(imei)) return imei;
        try {
            TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(imei) || "000000000000000".equals(imei) || "0".equals(imei)) {
            // 获取手机imei
//            imei = SPUtil.getImeikey();
        }
        if (TextUtils.isEmpty(imei)) {
            StringBuilder stringBuilder = new StringBuilder("35");
            for (int i = 0; i < 13; i++) {
                stringBuilder.append(String.valueOf((int) (Math.random() * 10)));
            }
            //获取手机imei
//            SPUtil.putImeikey(stringBuilder.toString());
        }
        return imei;
    }

    /**
     * 获取操作系统信息
     */
    public static String getOSName() {
        return "Android-" + android.os.Build.VERSION.SDK_INT;
    }

//    /**
//     * 获取版本号
//     */
//    public static int getVersionCode() {
//        int versionCode = 0;
//        PackageManager pm = PetAppLike.mcontext.getPackageManager();
//        try {
//            PackageInfo pi = pm.getPackageInfo(getPackageName(PetAppLike.mcontext), 0);
//            versionCode = pi.versionCode;
//        } catch (Exception e) {// android.os.DeadObjectException  by yangwenxin 5.1.0
//            e.printStackTrace();
//        }
//        return versionCode;
//    }


    //判断当前个数能不能继续写
    public static boolean isGoingWrite(String s) {
        char[] mchars = s.toCharArray();
        int sum = 0;
        for (char one : mchars) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(one);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                sum += 2;
            }else{
                sum+=1;
            }
        }
        if(sum<=12){
            return true;
        }else{
            return false;
        }
    }
//当前的sum
    public static int nowlenght(String s){
        char[] mchars = s.toCharArray();
        int sum = 0;
        for (char one : mchars) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(one);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                sum += 2;
            }else{
                sum+=1;
            }
        }
       return sum;
    }

}
