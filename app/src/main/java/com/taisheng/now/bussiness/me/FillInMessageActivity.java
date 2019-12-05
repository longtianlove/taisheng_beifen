package com.taisheng.now.bussiness.me;

import android.content.Intent;
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
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.UserInfoPostBean;
import com.taisheng.now.bussiness.bean.result.ModifyUserInfoResultBean;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/1.
 */

public class FillInMessageActivity extends BaseActivity {

    View tv_skip;
    EditText et_realname;
    RadioGroup rg;
    RadioButton rb_male;
    RadioButton rb_female;
    int sex;
    EditText et_age;
    TextView et_phone;
    //    EditText et_height;
//    EditText et_weight;
    TextView tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillinmessage);
        initView();

        initData();
    }


    void initView() {
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setEnabled(false);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputsToast()) {
                    modifyuser();
                }
            }
        });
        tv_skip = findViewById(R.id.tv_skip);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.putSKIP(true);
                Intent intent = new Intent();
                intent.setClass(FillInMessageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
        et_phone = (TextView) findViewById(R.id.et_phone);
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


    }

    void initData() {


    }

    void modifyuser() {
        UserInfoPostBean bean = new UserInfoPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.sysUser = new UserInfo();
        bean.sysUser.id = UserInstance.getInstance().getUid();
        bean.sysUser.token = UserInstance.getInstance().getToken();

        bean.sysUser.age = et_age.getText().toString();
        bean.sysUser.phone = et_phone.getText().toString();
        bean.sysUser.realName = et_realname.getText().toString();
        bean.sysUser.sex = sex;

        ApiUtils.getApiService().modifyuser(bean).enqueue(new TaiShengCallback<BaseBean<ModifyUserInfoResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ModifyUserInfoResultBean>> response, BaseBean<ModifyUserInfoResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        UserInstance.getInstance().userInfo.age = bean.sysUser.age;
                        SPUtil.putAge(UserInstance.getInstance().userInfo.age);
                        UserInstance.getInstance().userInfo.phone = bean.sysUser.phone;
                        SPUtil.putPhone(UserInstance.getInstance().userInfo.phone);
                        UserInstance.getInstance().userInfo.realName = bean.sysUser.realName;
                        SPUtil.putRealname(UserInstance.getInstance().userInfo.realName);
                        UserInstance.getInstance().userInfo.sex = bean.sysUser.sex;
                        SPUtil.putSex(UserInstance.getInstance().userInfo.sex);
                        UserInstance.getInstance().userInfo.userName = message.result.userName;
                        SPUtil.putZhanghao(message.result.userName);
                        UserInstance.getInstance().userInfo.nickName = message.result.nickName;
                        SPUtil.putNickname(message.result.nickName);

                        Intent intent;
                        if (TextUtils.isEmpty(SPUtil.getHEIGHT())) {
                            intent = new Intent(FillInMessageActivity.this, FillInMessageSecondActivity.class);
                        } else {
                            intent = new Intent(FillInMessageActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        finish();
                        break;

                }
            }

            @Override
            public void onFail(Call<BaseBean<ModifyUserInfoResultBean>> call, Throwable t) {

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
//        if (TextUtils.isEmpty(et_height.getText())) {
//            return false;
//        }
//        if (TextUtils.isEmpty(et_weight.getText())) {
//            return false;
//        }
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
//        if (TextUtils.isEmpty(et_height.getText())) {
//            ToastUtil.showTost("请输入身高");
//            return false;
//        }
//        if (TextUtils.isEmpty(et_weight.getText())) {
//            ToastUtil.showTost("请输入体重");
//            return false;
//        }
        tv_next.setEnabled(true);
        return true;
    }
}
