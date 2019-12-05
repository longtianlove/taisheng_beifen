package com.taisheng.now.bussiness.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.bussiness.MainActivity;

/**
 * Created by dragon on 2019/6/28.
 */

public class DoctorCommentSuccessActivity extends BaseActivity {


    View ll_go_detail;
    TextView btn_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_comment_success);
        initView();
    }
    void initView(){
        ll_go_detail=findViewById(R.id.ll_go_detail);
        ll_go_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_post= (TextView) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoctorCommentSuccessActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
