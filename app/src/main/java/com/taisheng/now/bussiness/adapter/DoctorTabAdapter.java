package com.taisheng.now.bussiness.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DoctorTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private List<String> title;

    public DoctorTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public DoctorTabAdapter(FragmentManager fm, List<Fragment> list, List<String> title) {
        super(fm);
        this.list = list;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /*让TabLayout能获取到title*/
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
