package com.taisheng.now.bussiness.watch.watchyujing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.watch.watchfirst.JibuFragment;
import com.taisheng.now.bussiness.watch.watchfirst.XinlvFragment;
import com.taisheng.now.bussiness.watch.watchfirst.XueyaFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WatchYujingFragment extends BaseFragment {






    View iv_back;






    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_watchyujing, container, false);
        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();


        return rootView;
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_watchfirst);
//        initView();
//
//
////        EventBus.getDefault().register(this);
//        initData();
//    }

    void initView(View rootView) {
        iv_back=rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }

    void initData() {


    }



    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
