package com.taisheng.now.bussiness.doctor;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.DoctorScorePostBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.StarGradeCanclick;
import com.th.j.commonlibrary.utils.TextsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class DoctorCommentActivity extends BaseHActivity implements TextWatcher {
    @BindView(R.id.starGrade)
    StarGradeCanclick starGrade;
    @BindView(R.id.et_commend)
    EditText etCommend;
    @BindView(R.id.btn_post)
    TextView btnPost;
    private String doctorId;

    @Override
    public void initView() {
        setContentView(R.layout.activity_doctorcomment);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        etCommend.addTextChangedListener(this);
        Intent intent = getIntent();
        doctorId = intent.getStringExtra("id");
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.publication_evaluation));
    }


    @OnClick(R.id.btn_post)
    public void onViewClicked() {
        doctorScore();
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


    private void doctorScore() {
        DoctorScorePostBean bean = new DoctorScorePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.doctorId = doctorId;
        bean.consultationScore = starGrade.resultScore;
        bean.content = TextsUtils.getTexts(etCommend);
        ApiUtils.getApiService_hasdialog().doctorScore(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        Intent intent = new Intent(DoctorCommentActivity.this, DoctorCommentSuccessActivity.class);
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
