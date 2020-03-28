package com.taisheng.now.bussiness.market.youhuijuan;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.adapter.DoctorTabAdapter;
import com.taisheng.now.bussiness.doctor.DoctorsFenleiFragment;
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

/**
 * Created by dragon on 2019/6/28.
 */

public class MyYouhuijuanActivity extends BaseIvActivity {


    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private DoctorTabAdapter adapter;
    private JiangyaoshiyongKanjuanFragment daishiyongFragment;
    private JiangyaoshiyongKanjuanFragment yishiyongFragment;
    private JiangyaoshiyongKanjuanFragment yiguoqiFragmnt;

    @Override
    public void initView() {
        setContentView(R.layout.activity_mykajuan);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        orderTabSet();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.coupon));
    }

    /**
     * 设置tab控件添加tab数据页面
     */
    private void orderTabSet() {
        List<String> listTitle = new ArrayList<>();
        List<Fragment> tabFragments = new ArrayList<>();

        daishiyongFragment = new JiangyaoshiyongKanjuanFragment();
        daishiyongFragment.assessmentType = "1";
        yishiyongFragment = new JiangyaoshiyongKanjuanFragment();
        yishiyongFragment.assessmentType = "2";
        yiguoqiFragmnt = new JiangyaoshiyongKanjuanFragment();
        yiguoqiFragmnt.assessmentType = "3";

        tabFragments.add(daishiyongFragment);
        tabFragments.add(yishiyongFragment);
        tabFragments.add(yiguoqiFragmnt);
        final String[] stringArray = getResources().getStringArray(R.array.card_tab);
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
            }
        });
        IndicatorLineUtil.changeTabIndicatorWidth(tlTab, 15);

    }

}
