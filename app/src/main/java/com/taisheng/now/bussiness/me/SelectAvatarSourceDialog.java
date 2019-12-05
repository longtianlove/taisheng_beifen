package com.taisheng.now.bussiness.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.taisheng.now.R;
import com.taisheng.now.util.DensityUtil;


/**
 * Created by simon on 15/8/8.
 */
public class SelectAvatarSourceDialog extends Activity implements View.OnClickListener
{
    public static String TAG_MODE ="mode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pick_photo);
        initParams();
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_take_photo).setOnClickListener(this);
        findViewById(R.id.btn_pick_from_library).setOnClickListener(this);
    }
    private void initParams(){
        //获取到当前Activity的Window
        Window dialog_window = getWindow();
        //获取到LayoutParams
        WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();
        int margin=getResources().getDimensionPixelSize(R.dimen.dialog_change_avater_margin)*2;
        dialog_window_attributes.width= DensityUtil.getScreenWidth(this);
        dialog_window_attributes.gravity= Gravity.BOTTOM;
        dialog_window.setAttributes(dialog_window_attributes);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() != R.id.btn_cancel) {
            Intent data = new Intent();
            data.putExtra(TAG_MODE, v.getId());
            setResult(Activity.RESULT_OK, data);
        }else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }
}