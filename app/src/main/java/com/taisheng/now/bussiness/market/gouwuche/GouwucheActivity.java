package com.taisheng.now.bussiness.market.gouwuche;

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
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.util.DensityUtil;
import com.th.j.commonlibrary.utils.IndicatorLineUtil;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.TextsUtils;

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

public class GouwucheActivity extends BaseIvActivity {

    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private DoctorTabAdapter adapter;
    private GouwucheFragment putongFragment;
    private GouwucheFragment jifenFragment;
    private boolean isCheck = true;

    @Override
    public void initView() {
        setContentView(R.layout.activity_gouwuchefinal);
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
        tvTitle.setText(getString(R.string.Shopping_cart));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.manage));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getString(R.string.manage).equals(TextsUtils.getTexts(tvRight))) {
                    tvRight.setText(getString(R.string.complete));
                    if (vpContent.getCurrentItem() == 0) {
                        putongFragment.changManager(true);
                    } else {
                        jifenFragment.changManager(true);
                    }
                } else {
                    tvRight.setText(getString(R.string.manage));
                    if (vpContent.getCurrentItem() == 0) {
                        putongFragment.changManager(false);
                    } else {
                        jifenFragment.changManager(false);
                    }
                }
            }
        });
    }

    /**
     * 设置tab控件添加tab数据页面
     */
    private void orderTabSet() {
        List<String> listTitle = new ArrayList<>();
        List<Fragment> tabFragments = new ArrayList<>();

        putongFragment = new GouwucheFragment();
        putongFragment.assessmentType = "1";
        putongFragment.scoreGoods = 1;
        jifenFragment = new GouwucheFragment();
        jifenFragment.assessmentType = "2";
        jifenFragment.scoreGoods = 0;

        tabFragments.add(putongFragment);
        tabFragments.add(jifenFragment);

        final String[] stringArray = getResources().getStringArray(R.array.car_tab);
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
                int position = tab.getPosition();
                if (position == 0) {
                    DingdanInstance.getInstance().scoreGoods = 1;
                } else {
                    DingdanInstance.getInstance().scoreGoods = 0;
                }
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
