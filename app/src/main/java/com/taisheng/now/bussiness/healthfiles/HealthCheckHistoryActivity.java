package com.taisheng.now.bussiness.healthfiles;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
//import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.util.DensityUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthCheckHistoryActivity extends BaseFragmentActivity {
    View iv_back;
    TextView tv_age;
    TextView tv_height;
    TextView tv_sex;
    TextView tv_weight;

    TabLayout tl_tab;

    ViewPager vp_content;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_health_check_history);

        initView();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_age.setText(UserInstance.getInstance().getAge());
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_height.setText(UserInstance.getInstance().getHeight());
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        if (Constants.FEMALE == UserInstance.getInstance().userInfo.sex) {
            tv_sex.setText("女");
        } else {
            tv_sex.setText("男");
        }
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_weight.setText(UserInstance.getInstance().healthInfo.weight);
        tl_tab = (TabLayout) findViewById(R.id.tl_tab);
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        initContent();
        initTab();


    }

    private void initTab() {
        tl_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.SelectedTabIndicatorColor));
        tl_tab.setTabTextColors(ContextCompat.getColor(this, R.color.UnSelectedTextColor), ContextCompat.getColor(this, R.color.SelectedTextColor));
        tl_tab.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
//        tl_tab.setTabTextColors(ContextCompat.getColor(this, R.color.gray), ContextCompat.getColor(this, R.color.white));
//        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
//        ViewCompat.setElevation(tl_tab, 10);
        tl_tab.setupWithViewPager(vp_content);
        changeTabIndicatorWidth(tl_tab, 15);
        reflex(tl_tab);
    }


    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp25 = DensityUtil.dip2px(tabLayout.getContext(), 25);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp25;
                        params.rightMargin = dp25;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 改变tablayout指示器的宽度
     *
     * @param tabLayout
     * @param margin
     */
    public void changeTabIndicatorWidth(final TabLayout tabLayout, final int margin) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                    int dp10 = margin == 0 ? 50 : margin;

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    ZhongyitizhiFragment zhongyitizhiFragment1;
    ZhongyitizhiFragment zhongyitizhiFragment2;
    ZhongyitizhiFragment zhongyitizhiFragment3;


    private void initContent() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("中医体质");
        tabIndicators.add("基础代谢");
        tabIndicators.add("女性健康");

//        for (int i = 0; i < 3; i++) {
//            tabIndicators.add("Tab " + i);
//        }
        tabFragments = new ArrayList<>();
        zhongyitizhiFragment1 = new ZhongyitizhiFragment();
        zhongyitizhiFragment1.assessmentType = "1";
        zhongyitizhiFragment2 = new ZhongyitizhiFragment();
        zhongyitizhiFragment2.assessmentType = "2";
        zhongyitizhiFragment3 = new ZhongyitizhiFragment();
        zhongyitizhiFragment3.assessmentType = "3";

        tabFragments.add(zhongyitizhiFragment1);
        tabFragments.add(zhongyitizhiFragment2);
        tabFragments.add(zhongyitizhiFragment3);

//        for (String s : tabIndicators) {
////            tabFragments.add(TabContentFragment.newInstance(s));
//        }
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(contentAdapter);
    }


    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }

    }
}
