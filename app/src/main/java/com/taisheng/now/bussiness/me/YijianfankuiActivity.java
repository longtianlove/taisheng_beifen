package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.FeedbackPostBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;
import com.th.j.commonlibrary.utils.TextsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class YijianfankuiActivity extends BaseHActivity implements TextWatcher {
    @BindView(R.id.et_feedbackContent)
    EditText etFeedbackContent;
    @BindView(R.id.btn_post)
    TextView btnPost;

    @Override
    public void initView() {
        setContentView(R.layout.activity_yijianfankui);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        etFeedbackContent.addTextChangedListener(this);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.feedback));
    }

    @OnClick(R.id.btn_post)
    public void onViewClicked() {
        String feedbackContent = TextsUtils.getTexts(etFeedbackContent);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null && s.length() > 0) {
            btnPost.setEnabled(true);
        } else {
            btnPost.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
