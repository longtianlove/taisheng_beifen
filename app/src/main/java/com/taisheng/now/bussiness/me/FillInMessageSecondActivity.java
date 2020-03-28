package com.taisheng.now.bussiness.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.HealthInfo;
import com.taisheng.now.bussiness.bean.post.HealthInfoPostBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.taisheng.now.view.dialog.BleedDialog;
import com.th.j.commonlibrary.utils.TextsUtils;

import androidx.annotation.IdRes;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/9.
 */

public class FillInMessageSecondActivity extends BaseHActivity implements BleedDialog.OnPickNumberListener {

    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.tv_blood)
    TextView tvBlood;
    @BindView(R.id.ll_blood)
    LinearLayout llBlood;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.rg_antihistamine)
    RadioGroup rgAntihistamine;
    @BindView(R.id.rb_yes2)
    RadioButton rbYes2;
    @BindView(R.id.rb_n2)
    RadioButton rbN2;
    @BindView(R.id.rg_hereditaryHistory)
    RadioGroup rgHereditaryHistory;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private int antihistamine = 0;
    private int hereditaryHistory = 0;


    @Override
    public void initView() {
        setContentView(R.layout.activity_fillinmessagesecond);
        ButterKnife.bind(this);
        llTop.setVisibility(View.INVISIBLE);
        initViewS();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }
    @OnClick({R.id.ll_blood, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_blood:
                BleedDialog dialog = new BleedDialog(FillInMessageSecondActivity.this);
                dialog.setOnPickNumberListener(FillInMessageSecondActivity.this);
                dialog.show();
                break;
            case R.id.tv_next:
                if (checkInputsToast()) {
                    addOrUpdateHealth();
                }
                break;
        }
    }
    private void initViewS() {

        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvBlood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        rgAntihistamine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        antihistamine = 1;
                        break;
                    case R.id.rb_no:
                        antihistamine = 0;
                        break;

                }
            }
        });
        rgHereditaryHistory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes2:
                        hereditaryHistory = 1;
                        break;
                    case R.id.rb_n2:
                        hereditaryHistory = 0;
                        break;

                }
            }
        });
    }


    private void addOrUpdateHealth() {

        HealthInfoPostBean bean = new HealthInfoPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        HealthInfo userHealth = new HealthInfo();
        userHealth.userId = UserInstance.getInstance().getUid();
        userHealth.height = TextsUtils.getTexts(etHeight);
        userHealth.weight = TextsUtils.getTexts(etWeight);
        userHealth.antihistamine = antihistamine;
        userHealth.hereditaryHistory = hereditaryHistory;
        userHealth.bloodType = TextsUtils.getTexts(tvBlood);
        bean.userHealth = userHealth;
        ApiUtils.getApiService().addOrUpdateHealth(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        UserInstance.getInstance().healthInfo = bean.userHealth;
                        UserInstance.getInstance().userInfo.height = bean.userHealth.height;
                        SPUtil.putHEIGHT(bean.userHealth.height);
                        Intent intent = new Intent(FillInMessageSecondActivity.this, MainActivity.class);
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


    private boolean checkInputsToast() {
        tvNext.setEnabled(false);
        if (TextUtils.isEmpty(TextsUtils.getTexts(etHeight))) {
            Uiutils.showToast(getString(R.string.input_height));
            return false;
        }
        if (TextUtils.isEmpty(TextsUtils.getTexts(etWeight))) {
            Uiutils.showToast(getString(R.string.input_weight));
            return false;
        }
        if (TextUtils.isEmpty(TextsUtils.getTexts(tvBlood)) || getString(R.string.slect).equals(TextsUtils.getTexts(tvBlood))) {
            Uiutils.showToast(getString(R.string.select_blood_type));
            return false;
        }

        tvNext.setEnabled(true);
        return true;
    }


    private boolean checkInputs() {
        tvNext.setEnabled(false);

        if (TextUtils.isEmpty(TextsUtils.getTexts(etHeight))) {
            return false;
        }
        if (TextUtils.isEmpty(TextsUtils.getTexts(etWeight))) {
            return false;
        }

        if (TextUtils.isEmpty(TextsUtils.getTexts(tvBlood)) || getString(R.string.slect).equals(TextsUtils.getTexts(tvBlood))) {
            return false;
        }
        tvNext.setEnabled(true);
        return true;
    }

    @Override
    public void onConfirmNumber(String number) {
        tvBlood.setText(number);
    }

}
