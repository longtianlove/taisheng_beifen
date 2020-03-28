package com.taisheng.now.bussiness.doctor;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.adapter.DoctorTabAdapter;
import com.th.j.commonlibrary.utils.IndicatorLineUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("WrongConstant")
public class DoctorsFenleiActivity extends BaseIvActivity {

    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private DoctorTabAdapter adapter;
    private DoctorsFenleiFragment zhongyitizhiFragment1;
    private DoctorsFenleiFragment jichudaixieFragment;
    private DoctorsFenleiFragment fukejiankangFragment;
    public static int selectTab = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_doctorsfenlei);
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
        tvTitle.setText(getString(R.string.doctor01));
    }

    /**
     * 设置tab控件添加tab数据页面
     */
    private void orderTabSet() {
        List<String> listTitle = new ArrayList<>();
        List<Fragment> tabFragments = new ArrayList<>();

        jichudaixieFragment = new DoctorsFenleiFragment();
        jichudaixieFragment.type = "4";
        fukejiankangFragment = new DoctorsFenleiFragment();
        fukejiankangFragment.type = "99";
        zhongyitizhiFragment1 = new DoctorsFenleiFragment();
        zhongyitizhiFragment1.type = "3";


        tabFragments.add(jichudaixieFragment);
        tabFragments.add(fukejiankangFragment);
        tabFragments.add(zhongyitizhiFragment1);
        final String[] stringArray = getResources().getStringArray(R.array.doctor_tab);
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

                switch (tab.getPosition()) {
                    case 0:
                        if (zhongyitizhiFragment1 != null) {
                            zhongyitizhiFragment1.initData();
                        }
                        break;
                    case 1:
                        if (jichudaixieFragment != null) {
                            jichudaixieFragment.initData();
                        }
                        break;
                    case 2:
                        if (fukejiankangFragment != null) {
                            fukejiankangFragment.initData();
                        }
                        break;
                }
            }
        });
        IndicatorLineUtil.changeTabIndicatorWidth(tlTab, 20);

    }

}
