package com.taisheng.now.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.taisheng.now.application.SampleAppLike;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.wight.MToast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.RequiresApi;

/**
 * @author 韩晓康
 * @date :2019/3/7  10:36
 * @description: ui工具类
 */
public class Uiutils {
    private static Toast toast = null;

    /**
     * 任意线程showToast
     * @param msg
     */

    public static void showToast(final String msg) {
        if (isNotificationEnabled(SampleAppLike.mcontext)) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                if (toast == null) {
                    toast = Toast.makeText(SampleAppLike.mcontext, msg, Toast.LENGTH_SHORT);

                } else {
                    toast.setText(msg);
                }

            } else {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (toast == null) {
                            toast = Toast.makeText(SampleAppLike.mcontext, msg, Toast.LENGTH_SHORT);
                        } else {
                            toast.setText(msg);
                        }
                    }
                };
                SampleAppLike.handler.post(runnable);
            }
            toast.show();
        } else {
            if (SampleAppLike.getCurrentActivity()!=null) {
                if (SampleAppLike.getCurrentActivity().toString() != null) {
                    MToast.makeText(SampleAppLike.getCurrentActivity(), msg, MToast.LENGTH_SHORT).show();//如果没有开通通知权限就使用自定义的Toast
                }
            }
        }

    }

    /**
     * dp转px
     *
     * @param context
     * @param
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static int px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static float px2sp(Context context, float pxValue) {
        //    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//        return (int) (pxValue / fontScale + 0.5f);
        return (float) ((pxValue / 2.0) + 0.5);
    }

    public static float px2sp2(Context context, float pxValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
//        return (float) ((pxValue / 2.0) + 0.5);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 透明状态栏
     */
    public static void transStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 关闭软件盘
     *
     * @param activity
     */
    public static void setSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void setSoftInput2(Activity activity) {
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int aimWidth) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        int bmscle = width / height;
        int screenWidth = aimWidth - 100;
        int newHeight = screenWidth / bmscle;
        // 计算缩放比例
        float scaleWidth = ((float) screenWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, screenWidth, newHeight);
        // 得到新的图片
        Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        LogUtilH.e("width>" + width + "screenWidth->" + screenWidth + "bitmap->" + bitmap.getWidth());
        return bitmap;
    }

    //获取顶部statusBar高度
    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }


    public static int getBottonBarHeight(Activity context) {

        int bottonbarH = 0;
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            int dpi = dm.heightPixels;

            int height = context.getWindowManager().getDefaultDisplay().getHeight();
            bottonbarH = dpi - height;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bottonbarH;
    }


    /**
     * 判断是否显示了导航栏
     * (说明这里的context 一定要是activity的context 否则类型转换失败)
     *
     * @param context
     * @return
     */
    public static boolean isShowNavBar(Context context) {
        if (null == context) {
            return false;
        }
/**
 * 获取应用区域高度
 */
        Rect outRect1 = new Rect();
        try {
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        int activityHeight = outRect1.height();
/**
 * 获取状态栏高度
 */
        int statuBarHeight = getStatusBarHeight();
/**
 * 屏幕物理高度 减去 状态栏高度
 */
        int remainHeight = getRealHeight() - statuBarHeight;
/**
 * 剩余高度跟应用区域高度相等 说明导航栏没有显示 否则相反
 */
        if (activityHeight == remainHeight) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 获取真实屏幕高度
     *
     * @return
     */
    public static int getRealHeight() {
        WindowManager wm = (WindowManager) SampleAppLike.mcontext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = SampleAppLike.mcontext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = SampleAppLike.mcontext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置view的高度
     * 父布局是LinearLayout
     */
    public static void setViewHeightL(final Activity context, final View view1,
                                      final View view) {
        view1.post(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int getHeight = view1.getHeight();
                        int statusBarHeight = Uiutils.getStatusBarHeight();
                        if (statusBarHeight == 0) {
                            statusBarHeight = 38;
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (getHeight + statusBarHeight + 10));
                        view.setLayoutParams(layoutParams);
                    }
                });

            }
        });
    }

    public static void setViewHeightL3(final Activity context, final View view) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int statusBarHeight = Uiutils.getStatusBarHeight();
                if (statusBarHeight == 0) {
                    statusBarHeight = 38;
                }
                LogUtilH.e("*****" + Uiutils.px2dp(context, statusBarHeight));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (statusBarHeight));
                view.setLayoutParams(layoutParams);
            }
        });
    }

    public static void setViewHeight2(final Activity context, final View view1,
                                      final View view) {
        view1.post(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int getHeight = view1.getHeight();
                        int statusBarHeight = Uiutils.getStatusBarHeight();
                        if (statusBarHeight == 0) {
                            statusBarHeight = 38;
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getHeight);
                        view.setLayoutParams(layoutParams);
                    }
                });

            }
        });
    }

    /**
     * 设置view的高度
     * 父布局是RelativeLayout
     */
    public static void setViewHeightR(final Activity context, final View view1,
                                      final View view) {
        view1.post(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int getHeight = view1.getHeight();
                        int statusBarHeight = Uiutils.getStatusBarHeight();
                        if (statusBarHeight == 0) {
                            statusBarHeight = 38;
                        }
                        LogUtilH.e(statusBarHeight + "---------statusBarHeight");
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (getHeight + statusBarHeight));
                        view.setLayoutParams(layoutParams);
                    }
                });

            }
        });
    }

    public static void setViewHeightR2(final Activity context, final View view1,
                                       final View view) {
        view1.post(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int getHeight = view1.getHeight();
                        int statusBarHeight = Uiutils.getStatusBarHeight();
                        if (statusBarHeight == 0) {
                            statusBarHeight = 38;
                        }
                        LogUtilH.e(statusBarHeight + "---------statusBarHeight");
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (getHeight));
                        view.setLayoutParams(layoutParams);
                    }
                });

            }
        });
    }

    /**
     * orderFragment专用
     */
    public static void setViewHeightR(final Activity context, final View view1,
                                      final View view, final View view2) {
        view.post(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int statusBarHeight = Uiutils.getStatusBarHeight();
                        if (statusBarHeight == 0) {
                            statusBarHeight = 38;
                        }
                        int measuredHeight = view1.getHeight();
                        int getHeight = view.getHeight();
                        int v = (int) Uiutils.px2dp(context, (statusBarHeight));

                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (measuredHeight + getHeight + v));
                        view.setLayoutParams(layoutParams);

                        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (measuredHeight + getHeight + v));
                        view2.setLayoutParams(layoutParams2);

                    }
                });

            }
        });
    }



    /**
     * 用来判断是否开启通知权限（判断原生的Toast是否可以使用）
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
