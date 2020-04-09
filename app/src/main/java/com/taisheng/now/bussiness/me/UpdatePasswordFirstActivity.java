package com.taisheng.now.bussiness.me;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.UpdatePswPostBean;
import com.taisheng.now.bussiness.login.LoginView;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.Uiutils;
import com.th.j.commonlibrary.utils.TextsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class UpdatePasswordFirstActivity extends BaseHActivity implements LoginView {
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_password_yincang)
    ImageView ivPasswordYincang;
    @BindView(R.id.tv_done)
    TextView tvDone;

    @Override
    public void initView() {
        setContentView(R.layout.activity_updatepwd_first);
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
        tvTitle.setText(getString(R.string.updatapsw));
    }

    void initViews() {

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 5) {
                    tvDone.setEnabled(true);
                } else {
                    tvDone.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        yincang = true;
        ivPasswordYincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_yincang));
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    boolean yincang = true;

    @Override
    public void showDialog() {
        DialogUtil.showProgress(this, "");
    }

    @Override
    public void dismissDialog() {
        DialogUtil.closeProgress();
    }


    @Override
    public void getVerifyNextTime(int nSecond) {
        WaitForNextFetchCode(nSecond);

    }

    int messageWaitTime;

    void WaitForNextFetchCode(int nSecond) {
        messageWaitTime = nSecond;
    }


    @OnClick({R.id.iv_password_yincang, R.id.tv_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_password_yincang:
                if (yincang) {
                    yincang = false;
                    ivPasswordYincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_xianshi));
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    yincang = true;
                    ivPasswordYincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_yincang));
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
                if (etPassword.getText().length() > 0) {
                    etPassword.setSelection(etPassword.getText().length());
                }
                break;
            case R.id.tv_done:
                UpdatePswPostBean bean = new UpdatePswPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.password = TextsUtils.getTexts(etPassword);
                ApiUtils.getApiService_hasdialog().modifypasswrod(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                Intent intent = new Intent(UpdatePasswordFirstActivity.this, UpdatePasswordLastActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                Uiutils.showToast(getString(R.string.updata_error));
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
                break;
        }
    }
}
