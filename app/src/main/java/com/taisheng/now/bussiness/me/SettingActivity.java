package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseHActivity;

import butterknife.ButterKnife;

/**
 * Created by dragon on 2019/6/28.
 */

public class SettingActivity extends BaseHActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }
}
