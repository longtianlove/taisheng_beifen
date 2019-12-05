package com.taisheng.now.bussiness.healthfiles;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.user.UserInstance;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthFileSearchActivity extends BaseFragmentActivity {
    View iv_back;
    TextView tv_age;
    TextView tv_height;
    TextView tv_sex;
    TextView tv_weight;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_health_file_search);

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

        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_age.setText(UserInstance.getInstance().getAge());
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_height.setText(UserInstance.getInstance().getHeight());
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        if (Constants.FEMALE == UserInstance.getInstance().userInfo.sex) {
            tv_sex.setText("女");
        } else {
            tv_sex.setText("男");
        }
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_weight.setText(UserInstance.getInstance().healthInfo.weight);





    }

}
