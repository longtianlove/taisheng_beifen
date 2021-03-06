package com.taisheng.now.bussiness.watch;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.watch.location.WatchLocationFragment;
import com.taisheng.now.bussiness.watch.watchchat.WatchChatFragment;
import com.taisheng.now.bussiness.watch.watchchat.WeChatActivity;
import com.taisheng.now.bussiness.watch.watchfirst.WatchFirstFragment;
import com.taisheng.now.bussiness.watch.watchme.WatchMeFragment;
import com.taisheng.now.bussiness.watch.watchme.WatchMeYujingxinxiXiangqingActivity;
import com.taisheng.now.bussiness.watch.watchyujing.ThreadUtil;
import com.taisheng.now.bussiness.watch.watchyujing.WatchYujingFragment;
import com.taisheng.now.chat.ChatManagerInstance;
import com.taisheng.now.evbusbean.WeChatMsg;
import com.taisheng.now.map.TrackInstance;
import com.taisheng.now.push.PushDataCenter;
import com.th.j.commonlibrary.wight.BottomBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressLint("WrongConstant")
public class WatchMainActivity extends BaseIvActivity implements BottomBar.OnItemListener {

    @BindView(R.id.fl_mains_watch)
    FrameLayout flMainsWatch;
    @BindView(R.id.bottom_watch)
    BottomBar bottomWatch;

    @Override
    public void initView() {
        setContentView(R.layout.watch_main);
        ButterKnife.bind(this);
        ChatManagerInstance.getInstance().init();
        EventBus.getDefault().register(this);
        ThreadUtil.open_gps_donot_check_Thread(300000);

    }

    @Override
    public void initData() {
        bottomWatch.setOnItemListener(this);
        initBottom();
    }

    @Override
    public void addData() {
        tvTitle.setText(getString(R.string.device06));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.device07));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMainActivity.this, WatchsListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }

    public  static int nowPosition=0;

    @Override
    public void onItem(int i) {
        switch (i) {
            case 0:
                setTopWhite();
                tvTitle.setText(getString(R.string.device06));
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText(getString(R.string.device07));
                nowPosition=0;
                break;
            case 1:
                setTopWhite();
                tvTitle.setText(getString(R.string.device02));
                tvRight.setVisibility(View.GONE);
                nowPosition=1;
                break;
            case 2:
                setTopWhite();
                tvTitle.setText("");
                tvRight.setVisibility(View.GONE);
                Intent intent = new Intent(WatchMainActivity.this, WeChatActivity.class);
                startActivity(intent);
                break;
            case 3:
                setTopWhite();
                tvTitle.setText(getString(R.string.device04));
                tvRight.setVisibility(View.GONE);
                nowPosition=3;
                break;
            case 4:
                Drawable drawable = getResources().getDrawable(R.drawable.icon_back);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvLeft.setCompoundDrawables(drawable, null, null, null);
                tvTitle.setTextColor(ContextCompat.getColor(this, R.color.color333333));
                tvTitle.setText(getString(R.string.device05));
                tvRight.setVisibility(View.GONE);
                nowPosition=4;
                break;
        }

    }

    private void setTopWhite() {
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back_new);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable, null, null, null);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    /**
     * 初始化
     */
    private void initBottom() {
        WatchFirstFragment firstFragment = new WatchFirstFragment();
        WatchLocationFragment watchLocationFragment = new WatchLocationFragment();
        WatchChatFragment chatFragment = new WatchChatFragment();
        WatchYujingFragment watchYujingFragment = new WatchYujingFragment();
        WatchMeFragment messageFragment = new WatchMeFragment();

        bottomWatch.init(getSupportFragmentManager(), R.id.fl_mains_watch, this)
                .addItem(getString(R.string.device01), R.drawable.watch_main_bottom_select, R.drawable.watch_main_bottom_jiankangjiance_normal, firstFragment, false)
                .addItem(getString(R.string.device02), R.drawable.watch_main_weizhi_select, R.drawable.watch_main_weizhi_normal, watchLocationFragment, false)
                .addItem(getString(R.string.device03), R.drawable.watch_main_weiliao_select, R.drawable.watch_main_weiliao_normal, chatFragment, false)
                .addItem(getString(R.string.device04), R.drawable.watch_main_bottom_yujingxinxi_select, R.drawable.watch_main_bottom_yujingxinxi_normal, watchYujingFragment, false)
                .addItem(getString(R.string.device05), R.drawable.watch_main_bottom_gerenzhongxin_select, R.drawable.watch_main_bottom_gerenzhongxin_normal, messageFragment, false)
                .defaultIndext(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0, sticky = true)
    public void getYujingxinxi(EventManage.tongzhiWeidu event) {
        if ("0".equals(event.weidu)) {
            bottomWatch.setCancelRedPoint(3);
        } else {
            bottomWatch.setRedPoint(3);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishClose(WeChatMsg weChatMsg) {
        bottomWatch.defaultIndext(nowPosition);
        switch (nowPosition) {
            case 0:
                setTopWhite();
                tvTitle.setText(getString(R.string.device06));
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText(getString(R.string.device07));
                nowPosition=0;
                break;
            case 1:
                setTopWhite();
                tvTitle.setText(getString(R.string.device02));
                tvRight.setVisibility(View.GONE);
                nowPosition=1;
                break;
            case 2:
                setTopWhite();
                tvTitle.setText("");
                tvRight.setVisibility(View.GONE);
                Intent intent = new Intent(WatchMainActivity.this, WeChatActivity.class);
                startActivity(intent);
                break;
            case 3:
                setTopWhite();
                tvTitle.setText(getString(R.string.device04));
                tvRight.setVisibility(View.GONE);
                nowPosition=3;
                break;
            case 4:
                Drawable drawable = getResources().getDrawable(R.drawable.icon_back);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvLeft.setCompoundDrawables(drawable, null, null, null);
                tvTitle.setTextColor(ContextCompat.getColor(this, R.color.color333333));
                tvTitle.setText(getString(R.string.device05));
                tvRight.setVisibility(View.GONE);
                nowPosition=4;
                break;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        bottomWatch.defaultIndext(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PushDataCenter.fromXiaomi) {
            bottomWatch.defaultIndext(3);
            setTopWhite();
            tvTitle.setText(getString(R.string.device04));
            tvRight.setVisibility(View.GONE);
            PushDataCenter.fromXiaomi = false;
            if (PushDataCenter.formatBean != null) {
                Intent intent = new Intent(WatchMainActivity.this, WatchMeYujingxinxiXiangqingActivity.class);
                intent.putExtra("id", PushDataCenter.formatBean.warningId);
                intent.putExtra("warningType", PushDataCenter.formatBean.warningType);
                intent.putExtra("message", PushDataCenter.formatBean.warningContent);
                intent.putExtra("createTime", PushDataCenter.formatBean.createTime);
                startActivity(intent);
            }
        }
        TrackInstance.getInstance().init(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
    
    

