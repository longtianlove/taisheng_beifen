package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.AnswerPostBean;
import com.taisheng.now.bussiness.bean.post.QuestionPostBean;
import com.taisheng.now.bussiness.bean.result.AnswerResultBean;
import com.taisheng.now.bussiness.bean.result.AssessmentOptionsList;
import com.taisheng.now.bussiness.bean.result.QuestionResultBean;
import com.taisheng.now.bussiness.bean.result.Records;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthQuestionActivity extends BaseHActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_now_postion)
    TextView tvNowPostion;
    @BindView(R.id.tv_all_size)
    TextView tvAllSize;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_a_label)
    TextView tvALabel;
    @BindView(R.id.tv_a)
    TextView tvA;
    @BindView(R.id.ll_a)
    LinearLayout llA;
    @BindView(R.id.tv_b_label)
    TextView tvBLabel;
    @BindView(R.id.tv_b)
    TextView tvB;
    @BindView(R.id.ll_b)
    LinearLayout llB;
    @BindView(R.id.tv_c_label)
    TextView tvCLabel;
    @BindView(R.id.tv_c)
    TextView tvC;
    @BindView(R.id.ll_c)
    LinearLayout llC;
    @BindView(R.id.tv_d_label)
    TextView tvDLabel;
    @BindView(R.id.tv_d)
    TextView tvD;
    @BindView(R.id.ll_d)
    LinearLayout llD;
    @BindView(R.id.tv_e_label)
    TextView tvELabel;
    @BindView(R.id.tv_e)
    TextView tvE;
    @BindView(R.id.ll_e)
    LinearLayout llE;
    private QuestionResultBean allBean;
    private List<Records> records;
    private Records question;
    private Handler handler = new Handler();
    private String assessmentType;
    private String subjectdbType;
    private String answersResult;//答案结果
    private int position = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_health_question);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        assessmentType = intent.getStringExtra("assessmentType");
        subjectdbType = "1";
        answersResult = "";
        position = 0;
    }

    @Override
    public void addData() {
        QuestionPostBean bean = new QuestionPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.assessmentType = assessmentType;
        bean.subjectdbType = subjectdbType;
//        bean.sign = null;
        ApiUtils.getApiService().getExtractionSubjectDb(bean).enqueue(new TaiShengCallback<BaseBean<QuestionResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<QuestionResultBean>> response, BaseBean<QuestionResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        allBean = message.result;
                        records = allBean.records;


                        if (records == null || records.size() == 0) {
                            //todo 数据为空
                        }
                        if (position < records.size()) {
                            updateView();
                        } else {

                        }

                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<QuestionResultBean>> call, Throwable t) {

            }
        });
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.health_assessment));
    }

    @OnClick({R.id.ll_a, R.id.ll_b, R.id.ll_c, R.id.ll_d, R.id.ll_e})
    public void onViewClicked(View view) {
        if (question == null) {
            return;
        }
        List<AssessmentOptionsList> assessmentOptionsList = question.assessmentOptionsList;
        if (assessmentOptionsList == null || assessmentOptionsList.size() <= 0) {
            return;
        }
        String result = "";
        switch (view.getId()) {
            case R.id.ll_a:
                tvALabel.setEnabled(true);
                result = assessmentOptionsList.get(0).id;
                break;
            case R.id.ll_b:
                tvBLabel.setEnabled(true);
                result = assessmentOptionsList.get(1).id;
                break;
            case R.id.ll_c:
                tvCLabel.setEnabled(true);
                result = assessmentOptionsList.get(2).id;

                break;
            case R.id.ll_d:
                tvDLabel.setEnabled(true);
                result = assessmentOptionsList.get(3).id;

                break;
            case R.id.ll_e:
                tvELabel.setEnabled(true);
                result = assessmentOptionsList.get(4).id;
                break;
        }
        answersResult = answersResult + result + ",";
        position++;
        DialogUtil.showProgress(HealthQuestionActivity.this, "");
        handler.postDelayed(new Runnable() {
            public void run() {
                if (position < records.size()) {
                    updateView();
                    DialogUtil.closeProgress();
                }
            }
        }, 300);   //1秒

        if (position < records.size()) {

        } else {
            answersResult = answersResult.substring(0, answersResult.length() - 1);
            AnswerPostBean bean = new AnswerPostBean();
            bean.userId = UserInstance.getInstance().getUid();
            bean.token = UserInstance.getInstance().getToken();
            bean.ids = answersResult;
            ApiUtils.getApiService().saveAnswer(bean).enqueue(new TaiShengCallback<BaseBean<AnswerResultBean>>() {
                @Override
                public void onSuccess(Response<BaseBean<AnswerResultBean>> response, BaseBean<AnswerResultBean> message) {
                    DialogUtil.closeProgress();
                    switch (message.code) {
                        case Constants.HTTP_SUCCESS:
                            Intent intent = new Intent(HealthQuestionActivity.this, HealthCheckResultActivity.class);
                            intent.putExtra("completeBatch", message.result.completeBatch);
                            intent.putExtra("remarks", message.result.remarks);
                            intent.putExtra("score", message.result.score);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }

                @Override
                public void onFail(Call<BaseBean<AnswerResultBean>> call, Throwable t) {
                    DialogUtil.closeProgress();
                }
            });
        }
    }

    private void updateView() {
        question = records.get(position);
        updatePosition(position);
        updateAnswer();
    }

    private void updatePosition(int i) {
        i++;
        progressBar.setProgress((i * 100) / records.size());
        if (records.size() > 10) {
            if (i < 10) {
                tvNowPostion.setText("0" + i);
            } else {
                tvNowPostion.setText("" + i);
            }
        } else {
            tvNowPostion.setText("" + i);
        }
        tvAllSize.setText("/" + records.size());
        tvQuestion.setText(question.name);
    }

    void updateAnswer() {
        tvALabel.setEnabled(false);
        tvBLabel.setEnabled(false);
        tvCLabel.setEnabled(false);
        tvDLabel.setEnabled(false);
        tvELabel.setEnabled(false);
        switch (question.assessmentOptionsList.size()) {
            case 1:
                llA.setVisibility(View.VISIBLE);
                tvA.setText(question.assessmentOptionsList.get(0).optionsValue);
                llB.setVisibility(View.GONE);
                llC.setVisibility(View.GONE);
                llD.setVisibility(View.GONE);
                llE.setVisibility(View.GONE);
                break;
            case 2:
                llA.setVisibility(View.VISIBLE);
                tvA.setText(question.assessmentOptionsList.get(0).optionsValue);
                llB.setVisibility(View.VISIBLE);
                tvB.setText(question.assessmentOptionsList.get(1).optionsValue);
                llC.setVisibility(View.GONE);
                llD.setVisibility(View.GONE);
                llE.setVisibility(View.GONE);
                break;
            case 3:
                llA.setVisibility(View.VISIBLE);
                tvA.setText(question.assessmentOptionsList.get(0).optionsValue);
                llB.setVisibility(View.VISIBLE);
                tvB.setText(question.assessmentOptionsList.get(1).optionsValue);
                llC.setVisibility(View.VISIBLE);
                tvC.setText(question.assessmentOptionsList.get(2).optionsValue);

                llD.setVisibility(View.GONE);
                llE.setVisibility(View.GONE);
                break;
            case 4:
                llA.setVisibility(View.VISIBLE);
                tvA.setText(question.assessmentOptionsList.get(0).optionsValue);
                llB.setVisibility(View.VISIBLE);
                tvB.setText(question.assessmentOptionsList.get(1).optionsValue);
                llC.setVisibility(View.VISIBLE);
                tvC.setText(question.assessmentOptionsList.get(2).optionsValue);
                llD.setVisibility(View.VISIBLE);
                tvD.setText(question.assessmentOptionsList.get(3).optionsValue);
                llE.setVisibility(View.GONE);
                break;
            case 5:
                llA.setVisibility(View.VISIBLE);
                tvA.setText(question.assessmentOptionsList.get(0).optionsValue);
                llB.setVisibility(View.VISIBLE);
                tvB.setText(question.assessmentOptionsList.get(1).optionsValue);
                llC.setVisibility(View.VISIBLE);
                tvC.setText(question.assessmentOptionsList.get(2).optionsValue);
                llD.setVisibility(View.VISIBLE);
                tvD.setText(question.assessmentOptionsList.get(3).optionsValue);
                llE.setVisibility(View.VISIBLE);
                tvE.setText(question.assessmentOptionsList.get(4).optionsValue);
                break;
        }
    }


}
