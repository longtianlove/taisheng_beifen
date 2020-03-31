package com.taisheng.now.bussiness;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.doctor.DoctorFragment;
import com.taisheng.now.bussiness.first.FirstFragment;
import com.taisheng.now.bussiness.market.MarketFragment;
import com.taisheng.now.bussiness.me.MeFragment;
import com.taisheng.now.bussiness.message.MessageFragment;
import com.taisheng.now.chat.ChatManagerInstance;
import com.taisheng.now.evbusbean.HomeChange;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.SPUtil;
import com.th.j.commonlibrary.wight.BottomBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressLint("WrongConstant")
public class MainActivity extends BaseIvActivity implements BottomBar.OnItemListener {

    @BindView(R.id.fl_mains)
    FrameLayout flMains;
    @BindView(R.id.bottom)
    BottomBar bottom;
    private FirstFragment firstFragment;

    @Override
    public void initView() {
        setContentView(R.layout.main);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        ChatManagerInstance.getInstance().init();
        SPUtil.putHome(true);
        EventBus.getDefault().register(this);
        bottom.setOnItemListener(this);
        llTop.setVisibility(View.GONE);
        initBottom();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }

    /**
     * 初始化
     */
    private void initBottom() {

        firstFragment = new FirstFragment();
        DoctorFragment doctorFragment = new DoctorFragment();
        MarketFragment marketFragment = new MarketFragment();
        MessageFragment messageFragment = new MessageFragment();
        MeFragment meFragment = new MeFragment();

        bottom.init(getSupportFragmentManager(), R.id.fl_mains, this)
                .addItem(getString(R.string.home13), R.drawable.main_bottom_first_select, R.drawable.main_bottom_first_normal, firstFragment, false)
                .addItem(getString(R.string.home14), R.drawable.main_bottom_doctor_select, R.drawable.main_bottom_doctor_normal, doctorFragment, false)
                .addItem(getString(R.string.home15), R.drawable.main_bottom_market_select, R.drawable.main_bottom_market_normal, marketFragment, false)
                .addItem(getString(R.string.home16), R.drawable.main_bottom_chat_select, R.drawable.main_bottom_chat_normal, messageFragment, false)
                .addItem(getString(R.string.home17), R.drawable.main_bottom_me_select, R.drawable.main_bottom_me_normal, meFragment, false)
                .defaultIndext(0);
    }

    @Override
    public void onItem(int i) {
        switch (i) {
            case 0:
                llTop.setVisibility(View.GONE);
                ivRight.setVisibility(View.GONE);
                break;
            case 1:
                llTop.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.GONE);
                ivRightT.setVisibility(View.GONE);
                tvLeft.setVisibility(View.INVISIBLE);
                tvTitle.setText(getString(R.string.doctor01));
                firstFragment.videoPlayer.onVideoPause();
                break;
            case 2:
                llTop.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.home15));
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setBackgroundResource(R.drawable.icon_gouwuche);
                tvLeft.setVisibility(View.INVISIBLE);
                tvRight.setVisibility(View.GONE);
                ivRightT.setVisibility(View.GONE);
                firstFragment.videoPlayer.onVideoPause();
                break;
            case 3:
                llTop.setVisibility(View.VISIBLE);
                tvLeft.setVisibility(View.INVISIBLE);
                tvTitle.setText(getString(R.string.home16));
                tvRight.setVisibility(View.GONE);
                ivRightT.setVisibility(View.GONE);
                tvLeft.setVisibility(View.INVISIBLE);
                tvRight.setText(getString(R.string.delete));
                ivRight.setVisibility(View.GONE);
                firstFragment.videoPlayer.onVideoPause();
                break;
            case 4:
                llTop.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.personal_center));
                ivRight.setVisibility(View.GONE);
                ivRight.setBackgroundResource(R.drawable.icon_set);
                tvLeft.setVisibility(View.INVISIBLE);
                tvRight.setVisibility(View.GONE);
                ivRightT.setVisibility(View.GONE);
                firstFragment.videoPlayer.onVideoPause();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fenlei(HomeChange change) {
        switch (change.getCode()) {
            case 1:
                llTop.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.GONE);
                ivRightT.setVisibility(View.GONE);
                tvLeft.setVisibility(View.INVISIBLE);
                tvTitle.setText(getString(R.string.doctor01));
                break;
            case 2:
                llTop.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.home15));
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setBackgroundResource(R.drawable.icon_gouwuche);
                tvLeft.setVisibility(View.INVISIBLE);
                ivRightT.setVisibility(View.GONE);
                break;
        }
        firstFragment.videoPlayer.onVideoPause();
        bottom.defaultIndext(change.getCode());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("HealthCheckResultActivity".equals(intent.getStringExtra("fromwhere"))) {
            bottom.defaultIndext(0);
        }
    }

    @Override
    public void onBackPressed() {
        DialogUtil.showTwoButtonDialog(this, getString(R.string.home18), getString(R.string.cancal), getString(R.string.exit), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                },
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}
    
    

