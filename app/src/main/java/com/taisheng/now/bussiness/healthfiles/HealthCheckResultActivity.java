package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.bussiness.MainActivity;

/**
 * Created by dragon on 2019/7/10.
 */

public class HealthCheckResultActivity extends BaseActivity {
    View iv_back;

    TextView tv_completeBatch;

    TextView tv_remarks;

    View ll_zixun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_check_result);
        initView();
        initData();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_completeBatch = (TextView) findViewById(R.id.tv_completeBatch);
        tv_remarks = (TextView) findViewById(R.id.tv_remarks);

        ll_zixun = findViewById(R.id.ll_zixun);

        ll_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthCheckResultActivity.this, MainActivity.class);
                intent.putExtra("fromwhere","HealthCheckResultActivity");
                startActivity(intent);
            }
        });

    }

    void initData() {
        Intent intent = getIntent();
        String completeBatch = intent.getStringExtra("completeBatch");
        String remarks = intent.getStringExtra("remarks");
//        int score=intent.getIntExtra("score",0);

        tv_completeBatch.setText(completeBatch);
        tv_remarks.setText(remarks);


    }
}
