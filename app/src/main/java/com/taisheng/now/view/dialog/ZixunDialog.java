package com.taisheng.now.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.taisheng.now.R;
import com.taisheng.now.util.DensityUtil;

/**
 * Created by dragon on 2019/7/2.
 */

public class ZixunDialog extends Dialog {

    public View ll_shipin, ll_yuyin, ll_quxiao;
    Activity mactivity;

    public ZixunDialog(@NonNull Context context) {
        super(context, R.style.MyDialogStyleBottom);
        mactivity= (Activity) context;
        initView();
        initParams();
    }

    public ZixunDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected ZixunDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_zixun, null);
        setContentView(view);
        ll_shipin = view.findViewById(R.id.ll_shipin);
        ll_yuyin = view.findViewById(R.id.ll_yuyin);
        ll_quxiao = view.findViewById(R.id.ll_quxiao);
    }
    private void initParams(){
        //获取到当前Activity的Window
        Window dialog_window = getWindow();
        //获取到LayoutParams
        WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();
        int margin=mactivity.getResources().getDimensionPixelSize(R.dimen.dialog_change_avater_margin)*2;
        dialog_window_attributes.width= DensityUtil.getScreenWidth(mactivity);
        dialog_window_attributes.gravity= Gravity.BOTTOM;
        dialog_window.setAttributes(dialog_window_attributes);
    }
}
