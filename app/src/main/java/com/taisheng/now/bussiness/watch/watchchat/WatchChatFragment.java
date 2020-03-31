package com.taisheng.now.bussiness.watch.watchchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.watch.watchfirst.JibuFragment;
import com.taisheng.now.bussiness.watch.watchfirst.XinlvFragment;
import com.taisheng.now.bussiness.watch.watchfirst.XueyaFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class WatchChatFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_watch_chat, container, false);
        return rootView;
    }



}
