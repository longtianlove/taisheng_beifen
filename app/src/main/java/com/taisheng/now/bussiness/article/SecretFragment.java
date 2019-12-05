package com.taisheng.now.bussiness.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
//import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.util.DensityUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class SecretFragment extends BaseFragment {

View iv_search;
    public TabLayout tl_tab;
    ViewPager vp_content;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    public  int selectTab=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_secret, container, false);

        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();

        return rootView;
    }

    void initView(View rootView){
        iv_search=rootView.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,SecretSearchActivity.class);
                startActivity(intent);
            }
        });
        tl_tab= (TabLayout) rootView.findViewById(R.id.tl_tab);
        vp_content= (ViewPager) rootView.findViewById(R.id.vp_content);
        initContent();
        initTab();
    }

    void initData() {


    }


    private void initTab(){
        tl_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.SelectedTabIndicatorColor));
        tl_tab.setSelectedTabIndicatorHeight(DensityUtil.dip2px(mActivity,2));
        tl_tab.setTabTextColors(ContextCompat.getColor(mActivity, R.color.UnSelectedTextColor), ContextCompat.getColor(mActivity, R.color.SelectedTextColor));
        tl_tab.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
//        tl_tab.setTabTextColors(ContextCompat.getColor(this, R.color.gray), ContextCompat.getColor(this, R.color.white));
//        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
//        ViewCompat.setElevation(tl_tab, 10);
        tl_tab.setupWithViewPager(vp_content);
        changeTabIndicatorWidth(tl_tab,15);

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



    SecretTabFragment zhongyitizhiFragment1;
    SecretTabFragment jichudaixieFragment;
    SecretTabFragment fukejiankangFragment;
    SecretTabFragment xinfeigongnengFragment;
    SecretTabFragment yaojingjianbeiFragment;
//    SecretTabFragment piweiganshen;
    private void initContent(){
        tabIndicators = new ArrayList<>();
        tabIndicators.add(Constants.SUSHENHUFU);
        tabIndicators.add(Constants.JIANSHENYUNDONG);
        tabIndicators.add(Constants.SHILIAOYANGSHENG);
        tabIndicators.add(Constants.YONGYAOZHIDAO);
        tabIndicators.add(Constants.MUYINGYUNYU);
//        tabIndicators.add("脾胃肝肾");
//        for (int i = 0; i < 3; i++) {
//            tabIndicators.add("Tab " + i);
//        }
        tabFragments = new ArrayList<>();
        zhongyitizhiFragment1=new SecretTabFragment();
        zhongyitizhiFragment1.typeName=Constants.SUSHENHUFU;

        jichudaixieFragment=new SecretTabFragment();
        jichudaixieFragment.typeName=Constants.JIANSHENYUNDONG;
        fukejiankangFragment=new SecretTabFragment();
        fukejiankangFragment.typeName=Constants.SHILIAOYANGSHENG;

        xinfeigongnengFragment=new SecretTabFragment();
        xinfeigongnengFragment.typeName=Constants.YONGYAOZHIDAO;

        yaojingjianbeiFragment=new SecretTabFragment();
        yaojingjianbeiFragment.typeName=Constants.MUYINGYUNYU;
//        piweiganshen=new SecretTabFragment();
//        piweiganshen.typeName=Constants.PIWEIGANSHEN;

        tabFragments.add(zhongyitizhiFragment1);
        tabFragments.add(jichudaixieFragment);
        tabFragments.add(fukejiankangFragment);
        tabFragments.add(xinfeigongnengFragment);
        tabFragments.add(yaojingjianbeiFragment);
//        tabFragments.add(piweiganshen);
//        for (String s : tabIndicators) {
////            tabFragments.add(TabContentFragment.newInstance(s));
//        }
        contentAdapter = new ContentPagerAdapter(((MainActivity)mActivity).getSupportFragmentManager());
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
