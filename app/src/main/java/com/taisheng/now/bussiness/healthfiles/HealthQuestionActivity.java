package com.taisheng.now.bussiness.healthfiles;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.adapter.HealthQuestionAdapter;
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
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class HealthQuestionActivity extends BaseHActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_now_postion)
    TextView tvNowPostion;
    @BindView(R.id.tv_all_size)
    TextView tvAllSize;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.lv_topic)
    ListView lvTopic;
    private String assessmentType;
    private String subjectdbType;
    private String answersResult;//答案结果
    private int position = 0;
    private HealthQuestionAdapter adapter;
    private List<AssessmentOptionsList> data;
    List<Records> records;

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
        records = new ArrayList<>();
        data = new ArrayList<>();
        adapter = new HealthQuestionAdapter(this);
        lvTopic.setAdapter(adapter);
        lvTopic.setOnItemClickListener(this);
    }

    @Override
    public void addData() {
        QuestionPostBean bean = new QuestionPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.assessmentType = assessmentType;
        bean.subjectdbType = subjectdbType;
//        bean.sign = null;
        ApiUtils.getApiService_hasdialog().getExtractionSubjectDb(bean).enqueue(new TaiShengCallback<BaseBean<QuestionResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<QuestionResultBean>> response, BaseBean<QuestionResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            records = message.result.records;
                            updatePosition(0);
                            if (message.result.records.get(0).assessmentOptionsList != null && message.result.records.size() > 0) {
                                data.addAll(message.result.records.get(0).assessmentOptionsList);
                                adapter.setData2(data);
                            }
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
        tvQuestion.setText(records.get(i - 1).name);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i < data.size()) {
            data.get(i).isCheck = true;
            adapter.setData2(data);
            adapter.setIndext(i);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    data.clear();
                    if (records != null && records.size() > 0) {
                        String result = "";
                        if (position < records.size()) {
                            updatePosition(position);
                            if (records.get(position).assessmentOptionsList != null && records.size() > 0) {
                                result = records.get(position).assessmentOptionsList.get(i).id;
                                data.addAll(records.get(position).assessmentOptionsList);
                                adapter.setData2(data);
                                adapter.setIndext(-1);
                            }
                            answersResult = answersResult + result + ",";
                            position++;
                            LogUtilH.e(answersResult.split(",").length+"--length---");
                        } else {
                            answersResult = answersResult.substring(0, answersResult.length() - 1);
                            AnswerPostBean bean = new AnswerPostBean();
                            bean.userId = UserInstance.getInstance().getUid();
                            bean.token = UserInstance.getInstance().getToken();
                            bean.ids = answersResult;
                            ApiUtils.getApiService_hasdialog().saveAnswer(bean).enqueue(new TaiShengCallback<BaseBean<AnswerResultBean>>() {
                                @Override
                                public void onSuccess(Response<BaseBean<AnswerResultBean>> response, BaseBean<AnswerResultBean> message) {
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

                                }
                            });
                        }
                    }
                }
            });

        }
    }
}
