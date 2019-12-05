package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.FeedbackPostBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class YijianfankuiActivity extends BaseActivity {
    View iv_back;
    EditText et_feedbackContent;

    TextView btn_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yijianfankui);
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

        et_feedbackContent = (EditText) findViewById(R.id.et_feedbackContent);
        et_feedbackContent.addTextChangedListener(new TextWatcher() {
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
                String feedbackContent = et_feedbackContent.getText().toString();
                if (TextUtils.isEmpty(feedbackContent)) {
                    return;
                }
                FeedbackPostBean bean = new FeedbackPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.remark = "";
                bean.doctorId = "";
                bean.feedbackContent = feedbackContent;
                bean.feedbackType = "2";
                bean.feedbackTitle = "";
                ApiUtils.getApiService().feedback(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showTost("反馈成功");
                                finish();
                                break;
                        }

                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
    }
}
