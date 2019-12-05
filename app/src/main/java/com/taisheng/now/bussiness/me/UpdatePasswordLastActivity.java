package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.view.View;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;

/**
 * Created by dragon on 2019/6/28.
 */

public class UpdatePasswordLastActivity extends BaseActivity {
//    View iv_back;


    View tv_back_memessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd_last);
        initView();
    }

    void initView() {
//        iv_back=findViewById(R.id.iv_back);
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        tv_back_memessage = findViewById(R.id.tv_back_memessage);
        tv_back_memessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
