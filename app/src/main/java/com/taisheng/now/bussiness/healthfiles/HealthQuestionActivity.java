package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.AnswerPostBean;
import com.taisheng.now.bussiness.bean.post.QuestionPostBean;
import com.taisheng.now.bussiness.bean.result.AnswerResultBean;
import com.taisheng.now.bussiness.bean.result.AssessmentOptionsList;
import com.taisheng.now.bussiness.bean.result.QuestionResultBean;
import com.taisheng.now.bussiness.bean.result.Records;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;

import java.util.List;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthQuestionActivity extends BaseActivity {
    View iv_back;
    ProgressBar progressBar;
    TextView tv_now_postion;
    TextView tv_all_size;
    TextView tv_question;

    View ll_a;
    TextView tv_a_label;
    TextView tv_b_label;
    TextView tv_c_label;
    TextView tv_d_label;
    TextView tv_e_label;
    TextView tv_a;
    View ll_b;
    TextView tv_b;
    View ll_c;
    TextView tv_c;
    View ll_d;
    TextView tv_d;
    View ll_e;
    TextView tv_e;

    QuestionResultBean allBean;
    List<Records> records;
    Records question;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_question);
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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_now_postion = (TextView) findViewById(R.id.tv_now_postion);
        tv_all_size = (TextView) findViewById(R.id.tv_all_size);
        tv_question = (TextView) findViewById(R.id.tv_question);
        ll_a = findViewById(R.id.ll_a);
        ll_a.setOnClickListener(listener);
        tv_a = (TextView) findViewById(R.id.tv_a);
        tv_a_label = (TextView) findViewById(R.id.tv_a_label);
        tv_b_label = (TextView) findViewById(R.id.tv_b_label);
        tv_c_label = (TextView) findViewById(R.id.tv_c_label);
        tv_d_label = (TextView) findViewById(R.id.tv_d_label);
        tv_e_label = (TextView) findViewById(R.id.tv_e_label);
        ll_b = findViewById(R.id.ll_b);
        ll_b.setOnClickListener(listener);
        tv_b = (TextView) findViewById(R.id.tv_b);
        ll_c = findViewById(R.id.ll_c);
        ll_c.setOnClickListener(listener);
        tv_c = (TextView) findViewById(R.id.tv_c);
        ll_d = findViewById(R.id.ll_d);
        ll_d.setOnClickListener(listener);
        tv_d = (TextView) findViewById(R.id.tv_d);
        ll_e = findViewById(R.id.ll_e);
        ll_e.setOnClickListener(listener);
        tv_e = (TextView) findViewById(R.id.tv_e);
    }

    android.os.Handler handler = new android.os.Handler();

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (question == null) {
                return;
            }
            List<AssessmentOptionsList> assessmentOptionsList = question.assessmentOptionsList;
            if (assessmentOptionsList == null || assessmentOptionsList.size() <= 0) {
                return;
            }
            String result = "";
            switch (v.getId()) {
                case R.id.ll_a:
                    tv_a_label.setEnabled(true);
                    result = assessmentOptionsList.get(0).id;
                    break;
                case R.id.ll_b:
                    tv_b_label.setEnabled(true);
                    result = assessmentOptionsList.get(1).id;
                    break;
                case R.id.ll_c:
                    tv_c_label.setEnabled(true);
                    result = assessmentOptionsList.get(2).id;

                    break;
                case R.id.ll_d:
                    tv_d_label.setEnabled(true);
                    result = assessmentOptionsList.get(3).id;

                    break;
                case R.id.ll_e:
                    tv_e_label.setEnabled(true);
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
    };

    String assessmentType;
    String subjectdbType;
    String answersResult;//答案结果

    void initData() {
        Intent intent = getIntent();
        assessmentType = intent.getStringExtra("assessmentType");
        subjectdbType = "1";
        answersResult = "";
        position = 0;
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

    int position = 0;

    void updateView() {

        question = records.get(position);
        updatePosition(position);
        updateAnswer();
    }

    void updatePosition(int i) {
        i++;
        progressBar.setProgress((i * 100) / records.size());
        if (records.size() > 10) {
            if (i < 10) {
                tv_now_postion.setText("0" + i);
            } else {
                tv_now_postion.setText("" + i);
            }
        } else {
            tv_now_postion.setText("" + i);
        }

        tv_all_size.setText("/" + records.size());
        tv_question.setText(question.name);
    }

    void updateAnswer() {
        tv_a_label.setEnabled(false);
        tv_b_label.setEnabled(false);
        tv_c_label.setEnabled(false);
        tv_d_label.setEnabled(false);
        tv_e_label.setEnabled(false);
        switch (question.assessmentOptionsList.size()) {
            case 1:
                ll_a.setVisibility(View.VISIBLE);
                tv_a.setText(question.assessmentOptionsList.get(0).optionsValue);
                ll_b.setVisibility(View.GONE);
                ll_c.setVisibility(View.GONE);
                ll_d.setVisibility(View.GONE);
                ll_e.setVisibility(View.GONE);
                break;
            case 2:
                ll_a.setVisibility(View.VISIBLE);
                tv_a.setText(question.assessmentOptionsList.get(0).optionsValue);
                ll_b.setVisibility(View.VISIBLE);
                tv_b.setText(question.assessmentOptionsList.get(1).optionsValue);
                ll_c.setVisibility(View.GONE);
                ll_d.setVisibility(View.GONE);
                ll_e.setVisibility(View.GONE);
                break;
            case 3:
                ll_a.setVisibility(View.VISIBLE);
                tv_a.setText(question.assessmentOptionsList.get(0).optionsValue);
                ll_b.setVisibility(View.VISIBLE);
                tv_b.setText(question.assessmentOptionsList.get(1).optionsValue);
                ll_c.setVisibility(View.VISIBLE);
                tv_c.setText(question.assessmentOptionsList.get(2).optionsValue);

                ll_d.setVisibility(View.GONE);
                ll_e.setVisibility(View.GONE);
                break;
            case 4:
                ll_a.setVisibility(View.VISIBLE);
                tv_a.setText(question.assessmentOptionsList.get(0).optionsValue);
                ll_b.setVisibility(View.VISIBLE);
                tv_b.setText(question.assessmentOptionsList.get(1).optionsValue);
                ll_c.setVisibility(View.VISIBLE);
                tv_c.setText(question.assessmentOptionsList.get(2).optionsValue);
                ll_d.setVisibility(View.VISIBLE);
                tv_d.setText(question.assessmentOptionsList.get(3).optionsValue);
                ll_e.setVisibility(View.GONE);
                break;
            case 5:
                ll_a.setVisibility(View.VISIBLE);
                tv_a.setText(question.assessmentOptionsList.get(0).optionsValue);
                ll_b.setVisibility(View.VISIBLE);
                tv_b.setText(question.assessmentOptionsList.get(1).optionsValue);
                ll_c.setVisibility(View.VISIBLE);
                tv_c.setText(question.assessmentOptionsList.get(2).optionsValue);
                ll_d.setVisibility(View.VISIBLE);
                tv_d.setText(question.assessmentOptionsList.get(3).optionsValue);
                ll_e.setVisibility(View.VISIBLE);
                tv_e.setText(question.assessmentOptionsList.get(4).optionsValue);

                break;
        }
    }
}
