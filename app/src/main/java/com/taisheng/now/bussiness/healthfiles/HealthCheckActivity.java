package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthCheckActivity extends BaseActivity {
    View iv_back;
    View tv_checkhistory;

    TextView btn_zhongyitizhi;
    TextView btn_jichudaixie;
    TextView btn_fukejiankang;
    TextView btn_xinfeigongneng;
    TextView btn_yaojingjianbei;
    TextView btn_piweiganshen;

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


//            0 其他 1 中医体质测评 2 基础代谢测评 3 基础代谢测评 4 女性健康测评 5 心肺功能测评 6 腰颈肩背测评 7 脾胃肝肾测评
            String assessmentType = "0";
            Intent intent = new Intent(HealthCheckActivity.this, HealthQuestionActivity.class);
            switch (v.getId()) {

                case R.id.btn_zhongyitizhi:
                    assessmentType="1";
                    break;

                case R.id.btn_jichudaixie:
                    assessmentType="2";
                    break;

                case R.id.btn_fukejiankang:
                    assessmentType="3";
                    break;

            }

            intent.putExtra("assessmentType",assessmentType);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_check);
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

        tv_checkhistory = findViewById(R.id.tv_checkhistory);
        tv_checkhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthCheckActivity.this, HealthCheckHistoryActivity.class);
                startActivity(intent);
            }
        });

        btn_zhongyitizhi = (TextView) findViewById(R.id.btn_zhongyitizhi);
        btn_zhongyitizhi.setOnClickListener(listener);
        btn_jichudaixie = (TextView) findViewById(R.id.btn_jichudaixie);
        btn_jichudaixie.setOnClickListener(listener);
        btn_fukejiankang = (TextView) findViewById(R.id.btn_fukejiankang);
        btn_fukejiankang.setOnClickListener(listener);



    }
}
