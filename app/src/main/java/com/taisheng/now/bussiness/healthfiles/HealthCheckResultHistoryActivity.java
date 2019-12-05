package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;

/**
 * Created by dragon on 2019/7/10.
 */

public class HealthCheckResultHistoryActivity extends BaseActivity {
    View iv_back;

    TextView tv_completeBatch;

    TextView tv_remarks;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_check_result_history);
        initView();
        initData();
    }
    void initView(){
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_completeBatch= (TextView) findViewById(R.id.tv_completeBatch);
        tv_remarks= (TextView) findViewById(R.id.tv_remarks);



    }

    void initData(){
        Intent intent=getIntent();
        String completeBatch=intent.getStringExtra("completeBatch");
        String remarks=intent.getStringExtra("remarks");
//        int score=intent.getIntExtra("score",0);

        tv_completeBatch.setText(completeBatch);
        tv_remarks.setText(remarks);



    }
}
