package com.taisheng.now.bussiness.healthfiles;

import android.os.Bundle;
import android.view.View;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthFileFirstActivity extends BaseActivity {
    View iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_first);
        initView();
    }
    void initView(){
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
