package com.taisheng.now.bussiness.healthfiles;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.adapter.DoctorTabAdapter;
import com.taisheng.now.bussiness.doctor.DoctorsFenleiFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.util.DensityUtil;
import com.th.j.commonlibrary.utils.IndicatorLineUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthCheckHistoryActivity extends BaseIvActivity {
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private ZhongyitizhiFragment zhongyitizhiFragment1;
    private ZhongyitizhiFragment zhongyitizhiFragment2;
//    private ZhongyitizhiFragment zhongyitizhiFragment3;
    private DoctorTabAdapter adapter;


    @Override
    public void initView() {
        setContentView(R.layout.activity_health_check_history);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        orderTabSet();
    }

    @Override
    public void addData() {
        tvAge.setText(UserInstance.getInstance().getAge());
        tvHeight.setText(UserInstance.getInstance().getHeight());
        if (Constants.FEMALE == UserInstance.getInstance().userInfo.sex) {
            tvSex.setText(getString(R.string.girl));
        } else {
            tvSex.setText(getString(R.string.boy));
        }
        tvWeight.setText(UserInstance.getInstance().healthInfo.weight);
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }

    /**
     * 设置tab控件添加tab数据页面
     */
    private void orderTabSet() {
        List<String> listTitle = new ArrayList<>();
        List<Fragment> tabFragments = new ArrayList<>();

        tabFragments = new ArrayList<>();
        zhongyitizhiFragment1 = new ZhongyitizhiFragment();
        zhongyitizhiFragment1.assessmentType = "1";
        zhongyitizhiFragment2 = new ZhongyitizhiFragment();
        zhongyitizhiFragment2.assessmentType = "2";
//        zhongyitizhiFragment3 = new ZhongyitizhiFragment();
//        zhongyitizhiFragment3.assessmentType = "3";

        tabFragments.add(zhongyitizhiFragment1);
        tabFragments.add(zhongyitizhiFragment2);
//        tabFragments.add(zhongyitizhiFragment3);


        final String[] stringArray = getResources().getStringArray(R.array.health_tab);
        for (int i = 0; i < stringArray.length; i++) {
            listTitle.add(stringArray[i]);
        }
        adapter = new DoctorTabAdapter(getSupportFragmentManager(), tabFragments, listTitle);
        vpContent.setAdapter(adapter);
        vpContent.setCurrentItem(0);
        tlTab.setupWithViewPager(vpContent);
        tlTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpContent.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                }
            }
        });
        IndicatorLineUtil.changeTabIndicatorWidth(tlTab, 20);

    }

}
