package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2019/6/28.
 */

public class UpdatePasswordLastActivity extends BaseHActivity {
    @BindView(R.id.tv_back_memessage)
    TextView tvBackMemessage;

    @Override
    public void initView() {
        setContentView(R.layout.activity_updatepwd_last);
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
        tvLeft.setVisibility(View.INVISIBLE);
        tvTitle.setText(getString(R.string.updata_psw_success));
    }

    @OnClick(R.id.tv_back_memessage)
    public void onViewClicked() {
        finish();
    }
}
