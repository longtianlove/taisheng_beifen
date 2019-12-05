package com.taisheng.now.bussiness.watch.watchfirst;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.doctor.DoctorsFenleiActivity;
import com.taisheng.now.bussiness.doctor.DoctorsFenleiFragment;
import com.taisheng.now.util.DensityUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WatchFirstFragment extends BaseFragment {






    View iv_back;
    public static TabLayout tl_tab;
    ViewPager vp_content;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    public static int selectTab = 0;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_watchfirst, container, false);
        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();


        return rootView;
    }




    void initView(View rootView) {
        iv_back=rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tl_tab = (TabLayout) rootView.findViewById(R.id.tl_tab);
        vp_content = (ViewPager) rootView.findViewById(R.id.vp_content);
        initContent();
        initTab();
    }

    void initData() {


    }


    private void initTab() {
        tl_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(DoctorsFenleiActivity.this, R.color.SelectedTabIndicatorColor));
//        tl_tab.setSelectedTabIndicatorHeight(DensityUtil.dip2px(DoctorsFenleiActivity.this, 2));
//        tl_tab.setTabTextColors(ContextCompat.getColor(DoctorsFenleiActivity.this, R.color.UnSelectedTextColor), ContextCompat.getColor(DoctorsFenleiActivity.this, R.color.SelectedTextColor));
//        tl_tab.setBackgroundColor(ContextCompat.getColor(DoctorsFenleiActivity.this, R.color.white));
//        tl_tab.setTabTextColors(ContextCompat.getColor(this, R.color.gray), ContextCompat.getColor(this, R.color.white));
//        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
//        ViewCompat.setElevation(tl_tab, 10);
        tl_tab.setupWithViewPager(vp_content);
        changeTabIndicatorWidth(tl_tab, 80);

        tl_tab.getTabAt(selectTab).select();

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


    XueyaFragment xueyaFragment;
    XinlvFragment xinlvFragment;
    JibuFragment jibuFragment;


    //    SecretTabFragment piweiganshen;
    private void initContent() {
        tabIndicators = new ArrayList<>();

        tabIndicators.add("血压");
        tabIndicators.add("心率");
        tabIndicators.add("计步");


        tabFragments = new ArrayList<>();

        xueyaFragment = new XueyaFragment();
        xinlvFragment = new XinlvFragment();
        jibuFragment = new JibuFragment();


        tabFragments.add(xueyaFragment);
        tabFragments.add(xinlvFragment);
        tabFragments.add(jibuFragment);



        contentAdapter = new ContentPagerAdapter(getActivity().getSupportFragmentManager());
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


    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
