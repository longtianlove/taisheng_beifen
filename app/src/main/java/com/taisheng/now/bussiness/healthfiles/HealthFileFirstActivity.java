package com.taisheng.now.bussiness.healthfiles;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthFileFirstActivity extends BaseHActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_health_first);
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
        tvTitle.setText(getString(R.string.health_records));
    }

}
