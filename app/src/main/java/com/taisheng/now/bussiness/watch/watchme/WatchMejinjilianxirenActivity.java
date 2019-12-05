package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMejinjilianxirenActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;

    View iv_bianji;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_jinjilianxiren);
        initView();
    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_bianji = findViewById(R.id.iv_bianji);
        iv_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMejinjilianxirenActivity.this, WatchMeXueyajingBianjiActivity.class);
                startActivity(intent);
            }
        });

    }


}
