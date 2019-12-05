package com.taisheng.now.bussiness.me;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.HealthInfo;
import com.taisheng.now.bussiness.bean.post.HealthInfoPostBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.dialog.BleedDialog;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/9.
 */

public class FillInMessageSecondActivity extends BaseActivity implements BleedDialog.OnPickNumberListener {


    EditText et_height, et_weight;
    View ll_blood;
    TextView tv_blood;

    RadioGroup rg_antihistamine;
    RadioGroup rg_hereditaryHistory;

    TextView tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillinmessagesecond);
        initView();
    }

    void initView() {

        et_height = (EditText) findViewById(R.id.et_height);
        et_height.addTextChangedListener(new TextWatcher() {
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
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_weight.addTextChangedListener(new TextWatcher() {
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


        ll_blood = findViewById(R.id.ll_blood);
        ll_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleedDialog dialog = new BleedDialog(FillInMessageSecondActivity.this);
                dialog.setOnPickNumberListener(FillInMessageSecondActivity.this);
                dialog.show();
            }
        });
        tv_blood = (TextView) findViewById(R.id.tv_blood);
        tv_blood.addTextChangedListener(new TextWatcher() {
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


        rg_antihistamine = (RadioGroup) findViewById(R.id.rg_antihistamine);
        rg_antihistamine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        rg_hereditaryHistory = (RadioGroup) findViewById(R.id.rg_hereditaryHistory);
        rg_hereditaryHistory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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


        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputsToast()) {
                    addOrUpdateHealth();
                }
            }
        });
    }


    int antihistamine = 0;
    int hereditaryHistory = 0;

    void addOrUpdateHealth() {

        HealthInfoPostBean bean = new HealthInfoPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        HealthInfo userHealth = new HealthInfo();
        userHealth.userId=UserInstance.getInstance().getUid();
        userHealth.height=et_height.getText().toString();
        userHealth.weight=et_weight.getText().toString();
        userHealth.antihistamine = antihistamine;
        userHealth.hereditaryHistory = hereditaryHistory;
        userHealth.bloodType = tv_blood.getText().toString();
        bean.userHealth=userHealth;
        ApiUtils.getApiService().addOrUpdateHealth(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        UserInstance.getInstance().healthInfo=bean.userHealth;
                        UserInstance.getInstance().userInfo.height=bean.userHealth.height;
                        SPUtil.putHEIGHT(bean.userHealth.height);
                        Intent intent=new Intent(FillInMessageSecondActivity.this, MainActivity.class);
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


    boolean checkInputsToast() {
        tv_next.setEnabled(false);
        if (TextUtils.isEmpty(et_height.getText())) {
            ToastUtil.showTost("请输入身高");
            return false;
        }
        if (TextUtils.isEmpty(et_weight.getText())) {
            ToastUtil.showTost("请输入体重");
            return false;
        }
        if (TextUtils.isEmpty(tv_blood.getText()) || "请选择".equals(tv_blood.getText())) {
            ToastUtil.showTost("请选择血型");
            return false;
        }

        tv_next.setEnabled(true);
        return true;
    }


    boolean checkInputs() {
        tv_next.setEnabled(false);

        if (TextUtils.isEmpty(et_height.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_weight.getText())) {
            return false;
        }

        if (TextUtils.isEmpty(tv_blood.getText()) || "请选择".equals(tv_blood.getText())) {
            return false;
        }
        tv_next.setEnabled(true);
        return true;
    }

    @Override
    public void onConfirmNumber(String number) {
        tv_blood.setText(number);
    }
}
