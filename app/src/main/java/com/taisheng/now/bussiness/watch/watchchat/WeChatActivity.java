package com.taisheng.now.bussiness.watch.watchchat;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.watch.WatchMainActivity;
import com.taisheng.now.evbusbean.WeChatMsg;
import com.taisheng.now.view.biaoqing.Watch_EmotionMainFragment;
import com.taisheng.now.view.chenjinshi.StatusBarUtil;
import com.taisheng.now.yuyin.manager.MediaManager;

import org.greenrobot.eventbus.EventBus;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeChatActivity extends BaseHActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_input_content)
    TextView tvInputContent;
    @BindView(R.id.fl_emotion_view_main)
    FrameLayout flEmotionViewMain;
    private Watch_EmotionMainFragment emotionMainFragment;

    @Override
    public void initView() {
        setContentView(R.layout.activity_wechat);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        initBiaoqinng();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.chat_news));
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new WeChatMsg(WatchMainActivity.nowPosition));
                WeChatActivity.this.finish();
            }
        });
    }



    void initBiaoqinng() {
        emotionMainFragment = Watch_EmotionMainFragment.newInstance(Watch_EmotionMainFragment.class, null);
        emotionMainFragment.bindToContentView(tvInputContent);//绑定当前页面控件
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotion_view_main, emotionMainFragment);
//        transaction.addToBackStack(null);//fragment添加至回退栈中
        transaction.commit();
    }

    @Override
    protected void onPause() {
        MediaManager.release();//保证在退出该页面时，终止语音播放
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        /**
         * 按下返回键，如果表情显示，则隐藏，没有显示则回退页面
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            EventBus.getDefault().post(new WeChatMsg(WatchMainActivity.nowPosition));
            WeChatActivity.this.finish();
            super.onBackPressed();
        }
    }

}
