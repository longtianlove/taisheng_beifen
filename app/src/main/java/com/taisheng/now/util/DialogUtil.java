package com.taisheng.now.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.CustomProgress;
import com.taisheng.now.view.dialog.ToChatDialog;
import com.taisheng.now.view.dialog.ZixunDialog;


/**
 * Created by long on 2017/4/25.
 */

public class DialogUtil {
    private static CustomProgress mCustomProgress;

    /**
     * 显示带文本的加载进度对话框
     */
    public static void showProgress(Context context, String str) {
        try {
            if ("".equals(str)) {
                str = "";
            }
            if (mCustomProgress == null) {
                mCustomProgress = CustomProgress.show(context, str, false, null);
            } else {
                mCustomProgress.setMessage(str);
                mCustomProgress.show();
            }
        } catch (Exception e) {
            Log.e("showProgress", e.getMessage());
        }
    }

    /**
     * 关掉加载进度对话框
     */
    public static void closeProgress() {
        if (mCustomProgress != null && mCustomProgress.isShowing()) {
            try {//bug fixxed with umeng at 5.0.1 by long
                mCustomProgress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCustomProgress = null;
        }
    }

    public static Dialog zixunDialog;

    /**
     * 如果Activity destory了不能运行dialog bug fixxed with umeng at 5.0.1 by long
     *
     * @param context
     * @return
     */
    public static boolean canShowDialog(Context context) {
        if (context == null) return false;
        if (context instanceof BaseActivity) {
            BaseActivity act = (BaseActivity) context;
            if (act.isDestroy()) {
                return false;
            }
        }
        return true;
    }

    public static View ll_shipin, ll_yuyin, ll_quxiao;

    //咨询弹窗
    public static void showzixunDialog(Context context, final View.OnClickListener shipinlistener, final View.OnClickListener yunyinlistener, final View.OnClickListener quxiaolistener) {
        if (!canShowDialog(context)) return;
        closeAllDialog();
        if (zixunDialog == null) {
            zixunDialog = new ZixunDialog(context);
            ll_shipin = zixunDialog.findViewById(R.id.ll_shipin);
            ll_shipin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zixunDialog.isShowing()) {
                        zixunDialog.dismiss();
                    }
                    if (shipinlistener != null) {
                        shipinlistener.onClick(v);
                    }

                }
            });
            ll_yuyin = zixunDialog.findViewById(R.id.ll_yuyin);
            ll_yuyin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zixunDialog.isShowing()) {
                        zixunDialog.dismiss();
                    }
                    if (yunyinlistener != null) {
                        yunyinlistener.onClick(v);
                    }

                }
            });
            ll_quxiao = zixunDialog.findViewById(R.id.ll_quxiao);
            ll_quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zixunDialog.isShowing()) {
                        zixunDialog.dismiss();
                    }
                    if (quxiaolistener != null) {
                        quxiaolistener.onClick(v);
                    }

                }
            });
            zixunDialog.setCancelable(true);
            zixunDialog.setCanceledOnTouchOutside(true);
        }
        zixunDialog.show();
    }


    public static View ll_shipin1, ll_yuyin1, ll_wenzi1, ll_quxiao1;
    public static Dialog zixunDialog1;

    //咨询弹窗
    public static void showToChatDialog(Context context, final View.OnClickListener wenziListener, final View.OnClickListener shipinlistener, final View.OnClickListener yunyinlistener, final View.OnClickListener quxiaolistener) {
        if (!canShowDialog(context)) return;
        closeAllDialog();
        if (zixunDialog1 == null) {
            zixunDialog1 = new ToChatDialog(context);
            ll_wenzi1 = zixunDialog1.findViewById(R.id.ll_wenzi);
            ll_wenzi1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zixunDialog1.isShowing()) {
                        zixunDialog1.dismiss();
                    }
                    if (wenziListener != null) {
                        wenziListener.onClick(v);
                    }
                }
            });
            ll_shipin1 = zixunDialog1.findViewById(R.id.ll_shipin);
            ll_shipin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zixunDialog1.isShowing()) {
                        zixunDialog1.dismiss();
                    }
                    if (shipinlistener != null) {
                        shipinlistener.onClick(v);
                    }

                }
            });
            ll_yuyin1 = zixunDialog1.findViewById(R.id.ll_yuyin);
            ll_yuyin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zixunDialog1.isShowing()) {
                        zixunDialog1.dismiss();
                    }
                    if (yunyinlistener != null) {
                        yunyinlistener.onClick(v);
                    }

                }
            });
            ll_quxiao1 = zixunDialog1.findViewById(R.id.ll_quxiao);
            ll_quxiao1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zixunDialog1.isShowing()) {
                        zixunDialog1.dismiss();
                    }
                    if (quxiaolistener != null) {
                        quxiaolistener.onClick(v);
                    }

                }
            });
            zixunDialog1.setCancelable(true);
            zixunDialog1.setCanceledOnTouchOutside(true);
        }
        zixunDialog1.show();
    }


    //关闭所有弹窗
    public static void closeAllDialog() {
        if (zixunDialog != null && zixunDialog.isShowing()) {
            try {
                zixunDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        zixunDialog = null;
        if (zixunDialog1 != null && zixunDialog1.isShowing()) {
            try {
                zixunDialog1.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        zixunDialog1 = null;

    }


    //申请退款
    public static void showshenqingfukuuan(final Context context) {
        if (!canShowDialog(context)) return;
//        closeAllDialog();
        final Dialog dialog = new AppDialog(context, R.layout.dialog_shenqingtuikuan, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, R.style.mystyle, Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(0);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 两个按钮弹窗
     *
     * @param context
     * @param messageString
     * @param cancleText
     * @param confirmText
     * @param onCancleClickListener
     * @param onConfirmClickListener
     */
    public static void showTwoButtonDialog(final Context context, String messageString, String cancleText, String confirmText, final View.OnClickListener onCancleClickListener, final View.OnClickListener onConfirmClickListener) {
        if (!canShowDialog(context)) return;
//        closeAllDialog();
        final Dialog dialog = new AppDialog(context, R.layout.dialog_two_button, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, R.style.mystyle, Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(0);
        TextView two_button_message = (TextView) dialog.findViewById(R.id.two_button_message);
        Button two_button_cancle = (Button) dialog.findViewById(R.id.two_button_cancle);
        Button two_button_confirm = (Button) dialog.findViewById(R.id.two_button_confirm);
        two_button_cancle.setText(cancleText);
        two_button_confirm.setText(confirmText);
        two_button_message.setText(messageString);

        two_button_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (onCancleClickListener != null) {
                    onCancleClickListener.onClick(v);
                }
            }
        });
        two_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (onConfirmClickListener != null) {
                    onConfirmClickListener.onClick(v);
                }
            }
        });

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


}
