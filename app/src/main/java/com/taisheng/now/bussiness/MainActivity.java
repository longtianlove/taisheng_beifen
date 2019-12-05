package com.taisheng.now.bussiness;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.FragmentTransaction;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.doctor.DoctorFragment;
import com.taisheng.now.bussiness.first.FirstFragment;
import com.taisheng.now.bussiness.market.MarketFragment;
import com.taisheng.now.bussiness.me.MeFragment;
import com.taisheng.now.bussiness.article.SecretFragment;
import com.taisheng.now.bussiness.message.MessageFragment;
import com.taisheng.now.chat.ChatManagerInstance;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.SPUtil;

@SuppressLint("WrongConstant")
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static int mTabID[] = {
            R.id.tab_first,
            R.id.tab_doctor,
            R.id.tab_message,
            R.id.tab_secret,
            R.id.tab_me};

    private ImageView iv_tab_first, iv_tab_doctor,iv_tab_message, iv_tab_secret,iv_tab_me;
private TextView tv_tab_first,tv_tab_doctor,tv_tab_message,tv_tab_secret,tv_tab_me;

    private View mTabs[] = {null, null,null,null, null};

    private FirstFragment firstFragment;
    private DoctorFragment doctorFragment;
    private MessageFragment messageFragment;
    public MarketFragment marketFragment;
    private MeFragment meFragment;

    View toolBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        setContentView(R.layout.main);
        ChatManagerInstance.getInstance().init();
        //进入主页
        SPUtil.putHome(true);
        initView();
        if (savedInstanceState != null) {
            firstFragment = (FirstFragment) getSupportFragmentManager().findFragmentByTag(FirstFragment.class.getName());
            doctorFragment = (DoctorFragment) getSupportFragmentManager().findFragmentByTag(DoctorFragment.class.getName());
            messageFragment= (MessageFragment) getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getName());
            marketFragment = (MarketFragment) getSupportFragmentManager().findFragmentByTag(SecretFragment.class.getName());
            meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag(MeFragment.class.getName());
            if (firstFragment == null) {
                firstFragment = new FirstFragment();
            }
            if (doctorFragment == null) {
                doctorFragment = new DoctorFragment();
            }
            if(messageFragment==null){
                messageFragment=new MessageFragment();
            }
            if(marketFragment ==null){
                marketFragment =new MarketFragment();
            }
            if (meFragment == null) {
                meFragment = new MeFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .show(firstFragment)
                    .hide(doctorFragment)
                    .hide(messageFragment)
                    .hide(marketFragment)
                    .hide(meFragment).commit();
        } else {
            firstFragment = new FirstFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment, FirstFragment.class.getName())
                    .show(firstFragment).commit();
        }


        for (int i = 0; i < 5; i++) {
            mTabs[i] = findViewById(mTabID[i]);
            mTabs[i].setOnClickListener(this);
        }

        iv_tab_first.setSelected(true);
        toolBar.setVisibility(View.VISIBLE);

//        EventBus.getDefault().register(this);


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        toolBar=findViewById(R.id.toolBar);
        iv_tab_first= (ImageView) findViewById(R.id.iv_tab_first);
        iv_tab_doctor= (ImageView) findViewById(R.id.iv_tab_doctor);
        iv_tab_message=findViewById(R.id.iv_tab_message);
        iv_tab_secret= (ImageView) findViewById(R.id.iv_tab_secret);
        iv_tab_me= (ImageView) findViewById(R.id.iv_tab_me);
        tv_tab_first= (TextView) findViewById(R.id.tv_tab_first);
        tv_tab_doctor= (TextView) findViewById(R.id.tv_tab_doctor);
        tv_tab_message=findViewById(R.id.tv_tab_message);
        tv_tab_secret= (TextView) findViewById(R.id.tv_tab_secret);
        tv_tab_me= (TextView) findViewById(R.id.tv_tab_me);




    }

    private void hideAllTabIcon(FragmentTransaction transaction) {
        if (null != firstFragment) {
            transaction.hide(firstFragment);
        }
        if (null != doctorFragment) {
            transaction.hide(doctorFragment);
        }
        if(null!=messageFragment){
            transaction.hide(messageFragment);
        }
        if(null!= marketFragment){
            transaction.hide(marketFragment);
        }
        if (null != meFragment) {
            transaction.hide(meFragment);
        }
        iv_tab_first.setSelected(false);
        tv_tab_first.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));
        iv_tab_doctor.setSelected(false);
        tv_tab_doctor.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));

        iv_tab_message.setSelected(false);
        tv_tab_message.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));

        iv_tab_secret.setSelected(false);
        tv_tab_secret.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));

        iv_tab_me.setSelected(false);
        tv_tab_me.setTextColor(getResources().getColor(R.color.tv_tab_color_normal));


    }

    public static int select_index;

    public void showFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllTabIcon(transaction);
        toolBar.setVisibility(View.VISIBLE);


        switch (index) {
            case 0:

                if (firstFragment == null) {
                    firstFragment = new FirstFragment();
                    transaction.add(R.id.fragment_container, firstFragment, FirstFragment.class.getName());
                }
                transaction.show(firstFragment).commit();

                iv_tab_first.setSelected(true);
                tv_tab_first.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
//                firstFragment.videoPlayer.onVideoResume();
//                firstFragment.videoPlayer.onVideoPause();




//                getLocationWithOneMinute = false;
                select_index = 0;
                break;
            case 1:
                if (doctorFragment == null) {
                    doctorFragment = new DoctorFragment();
                    transaction.add(R.id.fragment_container, doctorFragment, DoctorFragment.class.getName());
                }
                select_index = 1;


                transaction
                        .show(doctorFragment).commit();
                toolBar.setVisibility(View.GONE);
                iv_tab_doctor.setSelected(true);
                tv_tab_doctor.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
                firstFragment.videoPlayer.onVideoPause();
                break;
            case 2:
                if(marketFragment ==null){
                    marketFragment =new MarketFragment();
                    transaction.add(R.id.fragment_container, marketFragment,SecretFragment.class.getName());
                }
                select_index=2;
                transaction.show(marketFragment).commit();
                iv_tab_secret.setSelected(true);
                tv_tab_secret.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
                toolBar.setVisibility(View.GONE);
                firstFragment.videoPlayer.onVideoPause();
                break;
            case 3:

                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fragment_container, meFragment, MeFragment.class.getName());
                }
//                getLocationWithOneMinute = false;
                select_index = 3;
                transaction
                        .show(meFragment).commit();
                iv_tab_me.setSelected(true);
                tv_tab_me.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
                toolBar.setVisibility(View.GONE);
                firstFragment.videoPlayer.onVideoPause();
                break;
            case 4:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.fragment_container, messageFragment, MessageFragment.class.getName());
                }
//                getLocationWithOneMinute = false;
                select_index = 4;
                transaction
                        .show(messageFragment).commit();
                toolBar.setVisibility(View.GONE);
                iv_tab_message.setSelected(true);

                tv_tab_message.setTextColor(getResources().getColor(R.color.tv_tab_color_select));
                firstFragment.videoPlayer.onVideoPause();
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
            case R.id.tab_secret:
                showFragment(2);
                break;
            case R.id.tab_me:
                showFragment(3);
                break;
            case R.id.tab_message:
                showFragment(4);
                break;
        }
    }











    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if("HealthCheckResultActivity".equals(intent.getStringExtra("fromwhere"))){
            showFragment(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();



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


    @Override
    public void onBackPressed() {
        DialogUtil.showTwoButtonDialog(this, "确定要退出泰晟健康吗？", "取消","退出", new View.OnClickListener() {

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
    
    

