package com.taisheng.now.bussiness.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.bussiness.user.LoginActivity;
import com.taisheng.now.util.Apputil;

/**
 * Created by dragon on 2019/6/28.
 */

public class AboutUsActivity extends BaseActivity {
    View iv_back;
    TextView tv_version;
    View ll_fuwuxieyi;
    View ll_yisixieyi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        initView();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_version = (TextView) findViewById(R.id.tv_version);
        String version = Apputil.getVersionName(this);
        tv_version.setText("版本号："+version);


        ll_fuwuxieyi = findViewById(R.id.ll_fuwuxieyi);
        ll_fuwuxieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, FuwuxieyiActivity.class);
                startActivity(intent);
            }
        });

        ll_yisixieyi=findViewById(R.id.ll_yisixieyi);
        ll_yisixieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, YisixieyiActivity.class);
                startActivity(intent);
            }
        });
    }
}
