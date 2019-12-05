package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.market.dingdan.MyDingdanFragment;
import com.taisheng.now.bussiness.market.youhuijuan.KanjuanFragment;
import com.taisheng.now.util.DensityUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2019/6/28.
 */

public class MyDingdanActivity extends BaseFragmentActivity {
    View iv_back;

    TabLayout tl_tab;

    ViewPager vp_content;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydingdan);
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

    MyDingdanFragment daishiyongFragment;
    MyDingdanFragment yishiyongFragment;
    MyDingdanFragment yiguoqiFragmnt;
    MyDingdanFragment yiwanchengFragmnt;
    private void initContent() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("待付款");
        tabIndicators.add("待发货");
        tabIndicators.add("待收货");
        tabIndicators.add("已完成");
//        for (int i = 0; i < 3; i++) {
//            tabIndicators.add("Tab " + i);
//        }
        tabFragments = new ArrayList<>();
        daishiyongFragment = new MyDingdanFragment();
        daishiyongFragment.assessmentType = "1";
        yishiyongFragment = new MyDingdanFragment();
        yishiyongFragment.assessmentType = "2";
        yiguoqiFragmnt = new MyDingdanFragment();
        yiguoqiFragmnt.assessmentType = "3";
        yiwanchengFragmnt = new MyDingdanFragment();
        yiwanchengFragmnt.assessmentType = "4";

        tabFragments.add(daishiyongFragment);
        tabFragments.add(yishiyongFragment);
        tabFragments.add(yiguoqiFragmnt);
        tabFragments.add(yiwanchengFragmnt);


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
