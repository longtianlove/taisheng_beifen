package com.taisheng.now.bussiness.watch;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.bean.post.YuJingListPostBean;
import com.taisheng.now.bussiness.watch.bean.result.YujingResultBean;
import com.taisheng.now.bussiness.watch.bean.result.Yujingbean;
import com.taisheng.now.bussiness.watch.location.WatchLocationFragment;
import com.taisheng.now.bussiness.watch.watchchat.WeChatActivity;
import com.taisheng.now.bussiness.watch.watchfirst.WatchFirstFragment;
import com.taisheng.now.bussiness.watch.watchme.WatchMeFragment;
import com.taisheng.now.bussiness.watch.watchyujing.ThreadUtil;
import com.taisheng.now.bussiness.watch.watchyujing.WatchYujingFragment;
import com.taisheng.now.chat.ChatManagerInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.map.TrackInstance;
import com.taisheng.now.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Response;

//import android.support.v4.app.FragmentTransaction;

@SuppressLint("WrongConstant")
public class WatchMainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static int mTabID[] = {
            R.id.tab_first,
            R.id.tab_loaction,
            R.id.tab_doctor,
            R.id.tab_message
    };

    private ImageView iv_tab_first,iv_tab_location, iv_tab_doctor, iv_tab_message;
    private TextView tv_tab_first,tv_tab_location, tv_tab_doctor, tv_tab_message;

    private View mTabs[] = {null, null, null,null};

    private WatchFirstFragment firstFragment;
    private WatchLocationFragment watchLocationFragment;
    private WatchYujingFragment watchYujingFragment;
    private WatchMeFragment messageFragment;


    View toolBar;


    View iv_weidu;

    View tab_wechat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_main);
        ChatManagerInstance.getInstance().init();
        //进入主页
        SPUtil.putHome(true);
        initView();
        if (savedInstanceState != null) {
            firstFragment = (WatchFirstFragment) getSupportFragmentManager().findFragmentByTag(WatchFirstFragment.class.getName());
            watchLocationFragment= (WatchLocationFragment) getSupportFragmentManager().findFragmentByTag(WatchLocationFragment.class.getName());
            watchYujingFragment = (WatchYujingFragment) getSupportFragmentManager().findFragmentByTag(WatchYujingFragment.class.getName());
            messageFragment = (WatchMeFragment) getSupportFragmentManager().findFragmentByTag(WatchMeFragment.class.getName());

            if (firstFragment == null) {
                firstFragment = new WatchFirstFragment();
            }
            if(watchLocationFragment==null){
                watchLocationFragment=new WatchLocationFragment();
            }
            if (watchYujingFragment == null) {
                watchYujingFragment = new WatchYujingFragment();
            }
            if (messageFragment == null) {
                messageFragment = new WatchMeFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .show(firstFragment)
                    .hide(watchLocationFragment)
                    .hide(watchYujingFragment)
                    .hide(messageFragment).commit();
        } else {
            firstFragment = new WatchFirstFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment, WatchFirstFragment.class.getName())
                    .show(firstFragment).commit();
        }


        for (int i = 0; i < 4; i++) {
            mTabs[i] = findViewById(mTabID[i]);
            mTabs[i].setOnClickListener(this);
        }

        iv_tab_first.setSelected(true);
        toolBar.setVisibility(View.GONE);

        EventBus.getDefault().register(this);
        ThreadUtil.open_gps_donot_check_Thread(300000);
        TrackInstance.getInstance().init(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        toolBar = findViewById(R.id.toolBar);
        iv_tab_first = (ImageView) findViewById(R.id.iv_tab_first);
        iv_tab_location=findViewById(R.id.iv_tab_location);
        iv_tab_doctor = (ImageView) findViewById(R.id.iv_tab_doctor);
        iv_tab_message = findViewById(R.id.iv_tab_message);

        tv_tab_first = (TextView) findViewById(R.id.tv_tab_first);
        tv_tab_location=findViewById(R.id.tv_tab_location);
        tv_tab_doctor = (TextView) findViewById(R.id.tv_tab_doctor);
        tv_tab_message = findViewById(R.id.tv_tab_message);

        iv_weidu = findViewById(R.id.iv_weidu);
        tab_wechat=findViewById(R.id.tab_wechat);
        tab_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WatchMainActivity.this, WeChatActivity.class);
                startActivity(intent);
            }
        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0, sticky = true)
    public void getYujingxinxi(EventManage.getYujingxinxi event) {

        initData();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0, sticky = true)
    public void getYujingxinxi(EventManage.tongzhiWeidu event) {
        if ("0".equals(event.weidu)) {
            iv_weidu.setVisibility(View.INVISIBLE);
        } else {
            iv_weidu.setVisibility(View.VISIBLE);
        }


    }


    void initData() {
        YuJingListPostBean bean = new YuJingListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = 1;
        bean.pageSize = 10;
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().getWatchWarningAll(bean).enqueue(new TaiShengCallback<BaseBean<YujingResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<YujingResultBean>> response, BaseBean<YujingResultBean> message) {


                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            Yujingbean bean = message.result.records.get(0);
                            if ("0".equals(bean.status)) {
                                iv_weidu.setVisibility(View.INVISIBLE);
                            } else {
                                iv_weidu.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<YujingResultBean>> call, Throwable t) {

            }
        });

    }

    private void hideAllTabIcon(FragmentTransaction transaction) {
        if (null != firstFragment) {
            transaction.hide(firstFragment);
        }
        if(null!=watchLocationFragment){
            transaction.hide(watchLocationFragment);
        }
        if (null != watchYujingFragment) {
            transaction.hide(watchYujingFragment);
        }
        if (null != messageFragment) {
            transaction.hide(messageFragment);
        }

        iv_tab_first.setSelected(false);
        tv_tab_first.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));

        iv_tab_location.setSelected(false);
        tv_tab_location.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));

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
                    transaction.add(R.id.fragment_container, firstFragment, WatchFirstFragment.class.getName());
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
                    transaction.add(R.id.fragment_container, watchYujingFragment, WatchYujingFragment.class.getName());
                }
                select_index = 1;


                transaction
                        .show(watchYujingFragment).commit();
                toolBar.setVisibility(View.GONE);
                iv_tab_doctor.setSelected(true);
                tv_tab_doctor.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
                break;
            case 2:
                if (watchLocationFragment == null) {
                    watchLocationFragment = new WatchLocationFragment();
                    transaction.add(R.id.fragment_container, watchLocationFragment, WatchLocationFragment.class.getName());
                }
                select_index = 2;


                transaction
                        .show(watchLocationFragment).commit();
                toolBar.setVisibility(View.GONE);
                iv_tab_location.setSelected(true);
                tv_tab_location.setTextColor(getResources().getColor(R.color.tv_tab_color_select));

                break;

            case 4:
                if (messageFragment == null) {
                    messageFragment = new WatchMeFragment();
                    transaction.add(R.id.fragment_container, messageFragment, WatchMeFragment.class.getName());
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
            case R.id.tab_loaction:
                showFragment(2);
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
//        bean.deviceId = WatchInstance.getInstance().deviceId;
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
        EventBus.getDefault().unregister(this);
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
    
    

