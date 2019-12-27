package com.taisheng.now.bussiness.watch.watchchat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.ConnectDoctorPostBean;
import com.taisheng.now.bussiness.bean.result.ConnectDoctorResultBean;
import com.taisheng.now.bussiness.doctor.DoctorCommentActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.shipin.TRTCMainActivity;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.DoubleClickUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.biaoqing.EmotionMainFragment;
import com.taisheng.now.view.chenjinshi.StatusBarUtil;
import com.tencent.trtc.TRTCCloudDef;

import retrofit2.Call;
import retrofit2.Response;

public class WeChatActivity extends FragmentActivity implements  ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView mTvContent;
    EmotionMainFragment emotionMainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findViewById(R.id.title_left_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvContent = (TextView) findViewById(R.id.tv_input_content);

        initBiaoqinng();
    }

    void initBiaoqinng(){
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, null);
        emotionMainFragment.bindToContentView(mTvContent);//绑定当前页面控件
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotion_view_main, emotionMainFragment);
//        transaction.addToBackStack(null);//fragment添加至回退栈中
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSystemBar();
    }

    public void setSystemBar(){
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
    }


    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        /**
         * 按下返回键，如果表情显示，则隐藏，没有显示则回退页面
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }
}
