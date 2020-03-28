package com.taisheng.now.bussiness.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.login.AgreementA;
import com.taisheng.now.util.Apputil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2019/6/28.
 */

public class AboutUsActivity extends BaseHActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_fuwuxieyi)
    TextView tvFuwuxieyi;
    @BindView(R.id.tv_yisixieyi)
    TextView tvYisixieyi;

    @Override
    public void initView() {
        setContentView(R.layout.activity_aboutus);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {
        String version = Apputil.getVersionName(this);
        tvVersion.setText(getString(R.string.mine02) + version);
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.mine01));
    }

    @OnClick({R.id.tv_fuwuxieyi, R.id.tv_yisixieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fuwuxieyi:
                Intent intent = new Intent(AboutUsActivity.this, AgreementA.class);
                startActivity(intent);
                break;
            case R.id.tv_yisixieyi:
                Intent intent2 = new Intent(AboutUsActivity.this, YisixieyiActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
