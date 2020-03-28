package com.taisheng.now.bussiness.market;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;
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

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class FenleiMarketActivity extends BaseHActivity {

    public static TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private DoctorTabAdapter adapter;
    public static int selectTab = 0;
   private FenleiMarketTabFragment zhongyitizhiFragment1;
   private FenleiMarketTabFragment jichudaixieFragment;
   private FenleiMarketTabFragment fukejiankangFragment;
   private FenleiMarketTabFragment xinfeigongnengFragment;
    @Override
    public void initView() {
        setContentView(R.layout.activity_fenleimarket);
        ButterKnife.bind(this);
        tlTab=this.findViewById(R.id.tl_tab);
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
        tvTitle.setText(getString(R.string.good_list));
    }
    /**
     * 设置tab控件添加tab数据页面
     */
    private void orderTabSet() {
        List<String> listTitle = new ArrayList<>();
        List<Fragment> tabFragments = new ArrayList<>();

        zhongyitizhiFragment1 = new FenleiMarketTabFragment();
        zhongyitizhiFragment1.typeName = "营养保健";

        jichudaixieFragment = new FenleiMarketTabFragment();
        jichudaixieFragment.typeName = "食品滋补";

        fukejiankangFragment = new FenleiMarketTabFragment();
        fukejiankangFragment.typeName = "中药材";

        xinfeigongnengFragment = new FenleiMarketTabFragment();
        xinfeigongnengFragment.typeName = "积分兑换";
        tabFragments.add(zhongyitizhiFragment1);
        tabFragments.add(jichudaixieFragment);
        tabFragments.add(fukejiankangFragment);
        tabFragments.add(xinfeigongnengFragment);
        final String[] stringArray = getResources().getStringArray(R.array.dmarket_tab);
        for (int i = 0; i < stringArray.length; i++) {
            listTitle.add(stringArray[i]);
        }
        adapter = new DoctorTabAdapter(getSupportFragmentManager(), tabFragments, listTitle);
        vpContent.setAdapter(adapter);
        vpContent.setCurrentItem(selectTab);
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
