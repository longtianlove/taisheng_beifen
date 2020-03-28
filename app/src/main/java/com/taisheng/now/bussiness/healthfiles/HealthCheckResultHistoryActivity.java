package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dragon on 2019/7/10.
 */

public class HealthCheckResultHistoryActivity extends BaseHActivity {

    @BindView(R.id.tv_completeBatch)
    TextView tvCompleteBatch;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;

    @Override
    public void initView() {
        setContentView(R.layout.activity_health_check_result_history);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {
        Intent intent = getIntent();
        String completeBatch = intent.getStringExtra("completeBatch");
        String remarks = intent.getStringExtra("remarks");
//        int score=intent.getIntExtra("score",0);

        tvCompleteBatch.setText(completeBatch);
        tvRemarks.setText(remarks);
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.evaluation_results));
    }

}
