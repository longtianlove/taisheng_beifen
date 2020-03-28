package com.taisheng.now.bussiness.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2019/6/28.
 */

public class DoctorCommentSuccessActivity extends BaseHActivity {
    @BindView(R.id.ll_go_detail)
    LinearLayout llGoDetail;
    @BindView(R.id.btn_post)
    TextView btnPost;

    @Override
    public void initView() {
        setContentView(R.layout.activity_doctor_comment_success);
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
        tvTitle.setText(getString(R.string.valuation_success));
        tvLeft.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.ll_go_detail, R.id.btn_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_go_detail:
                finish();
                break;
            case R.id.btn_post:
                Intent intent = new Intent(DoctorCommentSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
