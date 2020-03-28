package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseHActivity;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthCheckActivity extends BaseHActivity {

    @BindView(R.id.btn_zhongyitizhi)
    TextView btnZhongyitizhi;
    @BindView(R.id.btn_jichudaixie)
    TextView btnJichudaixie;
    @BindView(R.id.btn_fukejiankang)
    TextView btnFukejiankang;

    @Override
    public void initView() {
        setContentView(R.layout.activity_health_check);
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
        tvTitle.setText(getString(R.string.health_assessment));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.evaluation_history));
        tvRight.setTextColor(ContextCompat.getColor(this,R.color.color00c8aa));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthCheckActivity.this, HealthCheckHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.btn_zhongyitizhi, R.id.btn_jichudaixie, R.id.btn_fukejiankang})
    public void onViewClicked(View view) {
//            0 其他 1 中医体质测评 2 基础代谢测评 3 基础代谢测评 4 女性健康测评 5 心肺功能测评 6 腰颈肩背测评 7 脾胃肝肾测评
        String assessmentType = "0";
        Intent intent = new Intent(HealthCheckActivity.this, HealthQuestionActivity.class);
        switch (view.getId()) {
            case R.id.btn_zhongyitizhi:
                assessmentType = "1";
                break;

            case R.id.btn_jichudaixie:
                assessmentType = "2";
                break;
            case R.id.btn_fukejiankang:
                assessmentType = "3";
                break;

        }

        intent.putExtra("assessmentType", assessmentType);
        startActivity(intent);
    }
}
