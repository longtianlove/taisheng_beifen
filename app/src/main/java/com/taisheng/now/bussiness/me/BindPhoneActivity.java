package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;

/**
 * Created by dragon on 2019/6/28.
 */

public class BindPhoneActivity extends BaseActivity {
    View iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindphone);
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
