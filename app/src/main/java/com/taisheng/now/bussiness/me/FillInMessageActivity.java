package com.taisheng.now.bussiness.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.UserInfoPostBean;
import com.taisheng.now.bussiness.bean.result.ModifyUserInfoResultBean;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.th.j.commonlibrary.utils.TextsUtils;

import androidx.annotation.IdRes;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/1.
 */

public class FillInMessageActivity extends BaseHActivity {

    @BindView(R.id.et_realname)
    EditText etRealname;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private int sex;


    @Override
    public void initView() {
        setContentView(R.layout.activity_fillinmessage);
        ButterKnife.bind(this);
        initViews();
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

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        if (checkInputsToast()) {
            modifyuser();
        }
    }

    private void initViews() {
        tvNext.setEnabled(false);
        etRealname.addTextChangedListener(new TextWatcher() {
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
        sex = Constants.MALE;
        etAge.addTextChangedListener(new TextWatcher() {
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
        etPhone.addTextChangedListener(new TextWatcher() {
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
            etPhone.setText(UserInstance.getInstance().userInfo.phone);
        }

    }

    private void modifyuser() {
        UserInfoPostBean bean = new UserInfoPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.sysUser = new UserInfo();
        bean.sysUser.id = UserInstance.getInstance().getUid();
        bean.sysUser.token = UserInstance.getInstance().getToken();

        bean.sysUser.age = TextsUtils.getTexts(etAge);
        bean.sysUser.phone = TextsUtils.getTexts(etPhone);
        bean.sysUser.realName = TextsUtils.getTexts(etRealname);
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

    private boolean checkInputs() {
        tvNext.setEnabled(false);
        if (TextUtils.isEmpty(TextsUtils.getTexts(etRealname))) {
            return false;
        }
        if (TextUtils.isEmpty(TextsUtils.getTexts(etAge))) {
            return false;
        }
        if (TextUtils.isEmpty(TextsUtils.getTexts(etPhone))) {
            return false;
        }
//        if (TextUtils.isEmpty(et_height.getText())) {
//            return false;
//        }
//        if (TextUtils.isEmpty(et_weight.getText())) {
//            return false;
//        }
        tvNext.setEnabled(true);
        return true;
    }

    private boolean checkInputsToast() {
        tvNext.setEnabled(false);
        if (TextUtils.isEmpty(TextsUtils.getTexts(etRealname))) {
            Uiutils.showToast(getString(R.string.input_name));
            return false;
        }
        if (TextUtils.isEmpty(TextsUtils.getTexts(etAge))) {
            Uiutils.showToast(getString(R.string.input_age));
            return false;
        }
        if (TextUtils.isEmpty(TextsUtils.getTexts(etPhone))) {
            Uiutils.showToast(getString(R.string.login08));
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
        tvNext.setEnabled(true);
        return true;
    }

}
