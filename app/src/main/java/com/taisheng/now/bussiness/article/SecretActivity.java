package com.taisheng.now.bussiness.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.Constants;
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

/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class SecretActivity extends BaseIvActivity {

    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private DoctorTabAdapter adapter;
    private SecretTabFragment zhongyitizhiFragment1;
    private SecretTabFragment jichudaixieFragment;
    private SecretTabFragment fukejiankangFragment;
    private SecretTabFragment xinfeigongnengFragment;
    private SecretTabFragment yaojingjianbeiFragment;

    @Override
    public void initView() {
        setContentView(R.layout.activity_secret);
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
        tvTitle.setText(getString(R.string.treasure));
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setBackgroundResource(R.drawable.icon_search2);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecretActivity.this, SecretSearchActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 设置tab控件添加tab数据页面
     */
    private void orderTabSet() {
        List<String> tabIndicators = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        zhongyitizhiFragment1 = new SecretTabFragment();
        zhongyitizhiFragment1.typeName = Constants.SUSHENHUFU;
        zhongyitizhiFragment1.typeSoure = "fe50b23ae5e68434def76f67cef35d01";
//        zhongyitizhiFragment1.typeSoure = "fe50b23ae5e68434def76f67cef35d04";

        jichudaixieFragment = new SecretTabFragment();
        jichudaixieFragment.typeName = Constants.JIANSHENYUNDONG;
        jichudaixieFragment.typeSoure = "fe50b23ae5e68434def76f67cef35d04";

        fukejiankangFragment = new SecretTabFragment();
        fukejiankangFragment.typeName = Constants.SHILIAOYANGSHENG;
        fukejiankangFragment.typeSoure = "fe50b23ae5e68434def76f67cef35d02";

        xinfeigongnengFragment = new SecretTabFragment();
        xinfeigongnengFragment.typeName = Constants.YONGYAOZHIDAO;
        xinfeigongnengFragment.typeSoure = "fe50b23ae5e68434def76f67cef35d05";
//        xinfeigongnengFragment.typeSoure = "fe50b23ae5e68434def76f67cef35d02";

        yaojingjianbeiFragment = new SecretTabFragment();
        yaojingjianbeiFragment.typeName = Constants.MUYINGYUNYU;
        yaojingjianbeiFragment.typeSoure = "fe50b23ae5e68434def76f67cef35d03";


        fragmentList.add(zhongyitizhiFragment1);
        fragmentList.add(jichudaixieFragment);
        fragmentList.add(fukejiankangFragment);
        fragmentList.add(xinfeigongnengFragment);
        fragmentList.add(yaojingjianbeiFragment);


        final String[] stringArray = getResources().getStringArray(R.array.secret_tab);
        for (int i = 0; i < stringArray.length; i++) {
            tabIndicators.add(stringArray[i]);
        }
        adapter = new DoctorTabAdapter(getSupportFragmentManager(), fragmentList, tabIndicators);
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
//        IndicatorLineUtil.changeTabIndicatorWidth(tlTab, 15);

    }
}
