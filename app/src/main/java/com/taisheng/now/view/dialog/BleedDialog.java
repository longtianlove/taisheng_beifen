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
import com.taisheng.now.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2019/7/2.
 */

public class BleedDialog extends Dialog {

    public View ll_shipin, ll_yuyin, ll_quxiao;
    Activity mactivity;


    WheelView mWvNumber;
    View tv_cancel;
    View tv_ok;
    List<String> mItems = new ArrayList<String>();

    public BleedDialog(@NonNull Context context) {
        super(context, R.style.MyDialogStyleBottom);
        mactivity= (Activity) context;
        initView();
        initParams();
    }

    public BleedDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected BleedDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_bleed, null);
        setContentView(view);

        mWvNumber = (WheelView) view.findViewById(R.id.wv_number);
        tv_cancel=view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_ok=view.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mItems.get(mWvNumber.getSeletedIndex());
                mOnPickNumberListener.onConfirmNumber(str);
                dismiss();
            }
        });
        mItems.add("AB");
        mItems.add("A");
        mItems.add("B");
        mItems.add("O");
        mItems.add("其他");
        mWvNumber.setItems(mItems);
        mWvNumber.setSeletion(0);

    }
    private void initParams(){
        //获取到当前Activity的Window
        Window dialog_window = getWindow();
        //获取到LayoutParams
        WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();
        dialog_window_attributes.width= DensityUtil.getScreenWidth(mactivity);
        dialog_window_attributes.gravity= Gravity.BOTTOM;
        dialog_window.setAttributes(dialog_window_attributes);
    }
    public interface OnPickNumberListener {
        void onConfirmNumber(String number);
    }

    OnPickNumberListener mOnPickNumberListener;

    public void setOnPickNumberListener(OnPickNumberListener listener) {
        mOnPickNumberListener = listener;
    }
}
