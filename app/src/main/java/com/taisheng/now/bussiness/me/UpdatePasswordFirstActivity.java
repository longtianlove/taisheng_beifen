package com.taisheng.now.bussiness.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.UpdatePswPostBean;
import com.taisheng.now.bussiness.user.LoginPresenter;
import com.taisheng.now.bussiness.user.LoginView;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class UpdatePasswordFirstActivity extends BaseActivity implements LoginView {
    View iv_back;
    EditText et_shoujihao;
    Button btn_yanzhengma;
    EditText et_yanzhengma;
    TextView btn_yanzhengma_login;

    private LoginPresenter loginPresenter;


    EditText et_password;
    ImageView iv_password_yincang;
    TextView tv_done;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd_first);
        initView();
        loginPresenter = new LoginPresenter(this);
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_yanzhengma = (Button) findViewById(R.id.btn_yanzhengma);
        btn_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPhone = et_shoujihao.getText().toString();
                loginPresenter.getVerifyCode(strPhone);

            }
        });

        et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);
        btn_yanzhengma_login = (TextView) findViewById(R.id.btn_yanzhengma_login);


        et_shoujihao = (EditText) findViewById(R.id.et_shoujihao);
        et_shoujihao.setText(UserInstance.getInstance().getPhone());
        //手机号
        et_shoujihao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    btn_yanzhengma_login.setEnabled(true);
                } else {
                    btn_yanzhengma_login.setEnabled(false);
                }
                if (s.length() == 11) {
                    btn_yanzhengma.setEnabled(true);
                } else {
                    btn_yanzhengma.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString();
                UpdatePswPostBean bean = new UpdatePswPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.password = password;
                ApiUtils.getApiService().modifypasswrod(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                Intent intent = new Intent(UpdatePasswordFirstActivity.this, UpdatePasswordLastActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                ToastUtil.showTost("更新失败");
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    tv_done.setEnabled(true);
                } else {
                    tv_done.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_password_yincang = (ImageView) findViewById(R.id.iv_password_yincang);
        yincang = true;
        iv_password_yincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_yincang));
        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        iv_password_yincang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yincang) {
                    yincang = false;
                    iv_password_yincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_xianshi));
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    yincang = true;
                    iv_password_yincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_yincang));
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
                if (et_password.getText().length() > 0) {
                    et_password.setSelection(et_password.getText().length());
                }
            }
        });


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
        btn_yanzhengma.setText(String.valueOf(messageWaitTime) + "S");
        btn_yanzhengma.setTextColor(Color.parseColor("#529FFB"));
        btn_yanzhengma.setEnabled(false);

        btn_yanzhengma.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messageWaitTime == 1) {
                    btn_yanzhengma.setEnabled(true);
                    btn_yanzhengma.setText("重新发送");
                    btn_yanzhengma.setTextColor(Color.parseColor("#ffffff"));

                } else {
                    messageWaitTime--;
                    btn_yanzhengma.setText(String.valueOf(messageWaitTime) + "S");
                    btn_yanzhengma.setTextColor(Color.parseColor("#529FFB"));
                    btn_yanzhengma.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}
