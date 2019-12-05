package com.taisheng.now.bussiness.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.DoctorScorePostBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiService;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.StarGrade;
import com.taisheng.now.view.StarGradeCanclick;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class DoctorCommentActivity extends BaseActivity {
    View iv_back;
    StarGradeCanclick starGrade;

    EditText et_commend;
    TextView btn_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorcomment);
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
        starGrade = (StarGradeCanclick) findViewById(R.id.starGrade);
        et_commend = (EditText) findViewById(R.id.et_commend);
        et_commend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    btn_post.setEnabled(true);
                } else {
                    btn_post.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_post = (TextView) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorScore();
            }
        });
    }

    String doctorId;

    void initData() {
        Intent intent = getIntent();
        doctorId = intent.getStringExtra("id");
    }


    void doctorScore() {
        DoctorScorePostBean bean = new DoctorScorePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.doctorId = doctorId;
        bean.consultationScore=starGrade.resultScore;
        bean.content=et_commend.getText().toString();
        ApiUtils.getApiService().doctorScore(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        Intent intent=new Intent(DoctorCommentActivity.this,DoctorCommentSuccessActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                }

            @Override
            public void onFail(Call<BaseBean> call, Throwable t) {

            }
        });



    }
}
