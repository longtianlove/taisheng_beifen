package com.taisheng.now.bussiness.watch;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.article.SecretFragment;
import com.taisheng.now.bussiness.doctor.DoctorFragment;
import com.taisheng.now.bussiness.first.FirstFragment;
import com.taisheng.now.bussiness.market.MarketFragment;
import com.taisheng.now.bussiness.me.MeFragment;
import com.taisheng.now.bussiness.message.MessageFragment;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.result.ShiShiCollecgtionResultBean;
import com.taisheng.now.bussiness.watch.watchfirst.WatchFirstFragment;
import com.taisheng.now.bussiness.watch.watchme.WatchMeFragment;
import com.taisheng.now.bussiness.watch.watchyujing.WatchYujingFragment;
import com.taisheng.now.chat.ChatManagerInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.SPUtil;

import retrofit2.Call;
import retrofit2.Response;

//import android.support.v4.app.FragmentTransaction;

@SuppressLint("WrongConstant")
public class WatchMainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static int mTabID[] = {
            R.id.tab_first,
            R.id.tab_doctor,
            R.id.tab_message
    };

    private ImageView iv_tab_first, iv_tab_doctor, iv_tab_message;
    private TextView tv_tab_first, tv_tab_doctor, tv_tab_message;

    private View mTabs[] = {null, null, null};

    private WatchFirstFragment firstFragment;
    private WatchYujingFragment watchYujingFragment;
    private WatchMeFragment messageFragment;


    View toolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_main);
        ChatManagerInstance.getInstance().init();
        //进入主页
        SPUtil.putHome(true);
        initView();
        if (savedInstanceState != null) {
            firstFragment = (WatchFirstFragment) getSupportFragmentManager().findFragmentByTag(FirstFragment.class.getName());
            watchYujingFragment = (WatchYujingFragment) getSupportFragmentManager().findFragmentByTag(DoctorFragment.class.getName());
            messageFragment = (WatchMeFragment) getSupportFragmentManager().findFragmentByTag(WatchMeFragment.class.getName());

            if (firstFragment == null) {
                firstFragment = new WatchFirstFragment();
            }
            if (watchYujingFragment == null) {
                watchYujingFragment = new WatchYujingFragment();
            }
            if (messageFragment == null) {
                messageFragment = new WatchMeFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .show(firstFragment)
                    .hide(watchYujingFragment)
                    .hide(messageFragment).commit();
        } else {
            firstFragment = new WatchFirstFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment, FirstFragment.class.getName())
                    .show(firstFragment).commit();
        }


        for (int i = 0; i < 3; i++) {
            mTabs[i] = findViewById(mTabID[i]);
            mTabs[i].setOnClickListener(this);
        }

        iv_tab_first.setSelected(true);
        toolBar.setVisibility(View.GONE);

//        EventBus.getDefault().register(this);


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        toolBar = findViewById(R.id.toolBar);
        iv_tab_first = (ImageView) findViewById(R.id.iv_tab_first);
        iv_tab_doctor = (ImageView) findViewById(R.id.iv_tab_doctor);
        iv_tab_message = findViewById(R.id.iv_tab_message);

        tv_tab_first = (TextView) findViewById(R.id.tv_tab_first);
        tv_tab_doctor = (TextView) findViewById(R.id.tv_tab_doctor);
        tv_tab_message = findViewById(R.id.tv_tab_message);


    }

    private void hideAllTabIcon(FragmentTransaction transaction) {
        if (null != firstFragment) {
            transaction.hide(firstFragment);
        }
        if (null != watchYujingFragment) {
            transaction.hide(watchYujingFragment);
        }
        if (null != messageFragment) {
            transaction.hide(messageFragment);
        }

        iv_tab_first.setSelected(false);
        tv_tab_first.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));
        iv_tab_doctor.setSelected(false);
        tv_tab_doctor.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));

        iv_tab_message.setSelected(false);
        tv_tab_message.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));


    }

    public static int select_index;

    public void showFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllTabIcon(transaction);
        toolBar.setVisibility(View.VISIBLE);


        switch (index) {
            case 0:

                if (firstFragment == null) {
                    firstFragment = new WatchFirstFragment();
                    transaction.add(R.id.fragment_container, firstFragment, FirstFragment.class.getName());
                }
                transaction.show(firstFragment).commit();
                toolBar.setVisibility(View.GONE);

                iv_tab_first.setSelected(true);
                tv_tab_first.setTextColor(getResources().getColor(R.color.tv_tab_color_select));

                select_index = 0;
                break;
            case 1:
                if (watchYujingFragment == null) {
                    watchYujingFragment = new WatchYujingFragment();
                    transaction.add(R.id.fragment_container, watchYujingFragment, DoctorFragment.class.getName());
                }
                select_index = 1;


                transaction
                        .show(watchYujingFragment).commit();
                toolBar.setVisibility(View.GONE);
                iv_tab_doctor.setSelected(true);
                tv_tab_doctor.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
                break;

            case 4:
                if (messageFragment == null) {
                    messageFragment = new WatchMeFragment();
                    transaction.add(R.id.fragment_container, messageFragment, MessageFragment.class.getName());
                }
//                getLocationWithOneMinute = false;
                select_index = 4;
                transaction
                        .show(messageFragment).commit();
                toolBar.setVisibility(View.GONE);
                iv_tab_message.setSelected(true);

                tv_tab_message.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_first:
                showFragment(0);
                break;
            case R.id.tab_doctor:
                showFragment(1);
                break;

            case R.id.tab_message:
                showFragment(4);
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        if ("HealthCheckResultActivity".equals(intent.getStringExtra("fromwhere"))) {
            showFragment(0);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        ShishiCollectionBean bean = new ShishiCollectionBean();
//        bean.userId = UserInstance.getInstance().getUid();
//        bean.token = UserInstance.getInstance().getToken();
//        bean.clientId = WatchInstance.getInstance().deviceId;
//        ApiUtils.getApiService().getcollection(bean).enqueue(new TaiShengCallback<BaseBean<ShiShiCollecgtionResultBean>>() {
//            @Override
//            public void onSuccess(Response<BaseBean<ShiShiCollecgtionResultBean>> response, BaseBean<ShiShiCollecgtionResultBean> message) {
//                switch (message.code) {
//                    case Constants.HTTP_SUCCESS:
////                        public String watchBpxyHigh;
////                        public String watchBpxyLow;
////                        public String stepNum;
////                        public String watchHeart;
//                        WatchInstance.getInstance().watchBpxyHigh=message.result.watchBpxyHigh;
//                        WatchInstance.getInstance().watchBpxyLow=message.result.watchBpxyLow;
//                        WatchInstance.getInstance().stepNum=message.result.stepNum;
//                        WatchInstance.getInstance().watchHeart=message.result.watchHeart;
//
//
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<ShiShiCollecgtionResultBean>> call, Throwable t) {
//
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }


//    @Override
//    public void onBackPressed() {
//        DialogUtil.showTwoButtonDialog(this, "确定要退出泰晟健康吗？", "取消","退出", new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                },
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        finish();
//                    }
//                }
//        );
//    }


//
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }
//
//    public boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] leftTop = {0, 0};
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            int right = left + v.getWidth();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击的是输入框区域，保留点击EditText的事件
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }
}
    
    

