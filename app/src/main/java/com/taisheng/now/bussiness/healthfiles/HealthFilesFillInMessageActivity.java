package com.taisheng.now.bussiness.healthfiles;

import android.os.Bundle;
import androidx.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.util.ToastUtil;

/**
 * Created by dragon on 2019/7/1.
 */

public class HealthFilesFillInMessageActivity extends BaseActivity {
    EditText et_realname;
    RadioGroup rg;
    RadioButton rb_male;
    RadioButton rb_female;
    int sex;
    EditText et_age;
    EditText et_phone;
    EditText et_height;
    EditText et_weight;
    TextView tv_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_healthfilefillinmessage);
        initView();
    }
    void initView() {
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setEnabled(false);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputsToast()) {

                }
            }
        });

        et_realname = (EditText) findViewById(R.id.et_realname);
        et_realname.addTextChangedListener(new TextWatcher() {
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
        rg = (RadioGroup) findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        sex = Constants.MALE;
                        break;
                    case R.id.rb_female:
                        sex = Constants.FEMALE;
                        break;

                }
            }
        });
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        sex = Constants.MALE;

        et_age = (EditText) findViewById(R.id.et_age);
        et_age.addTextChangedListener(new TextWatcher() {
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
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.addTextChangedListener(new TextWatcher() {
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
        if (!TextUtils.isEmpty(UserInstance.getInstance().userInfo.phone)) {
            et_phone.setText(UserInstance.getInstance().userInfo.phone);
        }

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



    }

    boolean checkInputs() {
        tv_next.setEnabled(false);
        if (TextUtils.isEmpty(et_realname.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_age.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_phone.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_height.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_weight.getText())) {
            return false;
        }
        tv_next.setEnabled(true);
        return true;
    }
    boolean checkInputsToast() {
        tv_next.setEnabled(false);
        if (TextUtils.isEmpty(et_realname.getText())) {
            ToastUtil.showTost("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(et_age.getText())) {
            ToastUtil.showTost("请输入年龄");
            return false;
        }
        if (TextUtils.isEmpty(et_phone.getText())) {
            ToastUtil.showTost("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(et_height.getText())) {
            ToastUtil.showTost("请输入身高");
            return false;
        }
        if (TextUtils.isEmpty(et_weight.getText())) {
            ToastUtil.showTost("请输入体重");
            return false;
        }
        tv_next.setEnabled(true);
        return true;
    }
}
