package com.taisheng.now.bussiness.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.LoginPostBean;
import com.taisheng.now.bussiness.me.FillInMessageActivity;
import com.taisheng.now.bussiness.me.FillInMessageSecondActivity;
import com.taisheng.now.util.DensityUtil;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.SpanUtil;
import com.th.j.commonlibrary.utils.TextsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2019/6/27.
 */

public class LoginActivity extends BaseIvActivity implements LoginView {

    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.et_zhanghao)
    EditText etZhanghao;
    @BindView(R.id.iv_zhanghao_guanbi)
    ImageView ivZhanghaoGuanbi;
    @BindView(R.id.tv_line_zhanghao)
    TextView tvLineZhanghao;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_password_yincang)
    ImageView ivPasswordYincang;
    @BindView(R.id.tv_line_password)
    TextView tvLinePassword;
    @BindView(R.id.btn_zhanghao_login)
    TextView btnZhanghaoLogin;
    @BindView(R.id.tv_yanzhengma_change)
    TextView tvYanzhengmaChange;
    @BindView(R.id.ll_zhanghao)
    LinearLayout llZhanghao;
    @BindView(R.id.et_shoujihao)
    EditText etShoujihao;
    @BindView(R.id.iv_shoujihao_guanbi)
    ImageView ivShoujihaoGuanbi;
    @BindView(R.id.tv_line_shoujihao)
    TextView tvLineShoujihao;
    @BindView(R.id.et_yanzhengma)
    EditText etYanzhengma;
    @BindView(R.id.btn_yanzhengma)
    TextView btnYanzhengma;
    @BindView(R.id.tv_line_yanzhengma)
    TextView tvLineYanzhengma;
    @BindView(R.id.btn_yanzhengma_login)
    TextView btnYanzhengmaLogin;
    @BindView(R.id.tv_zhanghao_change)
    TextView tvZhanghaoChange;
    @BindView(R.id.ll_shoujihao)
    LinearLayout llShoujihao;
    @BindView(R.id.cb)
    CheckBox cb;
    @BindView(R.id.ll_fuwuxieyi)
    LinearLayout llFuwuxieyi;

    private LoginPresenter loginPresenter;
    private int messageWaitTime;
    boolean yincang = true;
    public boolean isPhone = true;


    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
        SPUtil.putHome(false);
        loginPresenter = new LoginPresenter(this);
        llShoujihao.setVisibility(View.VISIBLE);
        ivShoujihaoGuanbi.setVisibility(View.GONE);
        llZhanghao.setVisibility(View.GONE);
        ivZhanghaoGuanbi.setVisibility(View.GONE);
        btnYanzhengma.setEnabled(false);
        btnYanzhengmaLogin.setEnabled(false);
        btnZhanghaoLogin.setEnabled(false);
        llTop.setVisibility(View.GONE);
        initListener();

    }

    @Override
    public void initData() {
        double screenHeight = (DensityUtil.getScreenHeight(this) * 0.4) + 20;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) screenHeight);
        llBg.setLayoutParams(layoutParams);
        yincang = true;
        isPhone = true;
        ivPasswordYincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_yincang));
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void next(EventManage.getUserInfoEvent event) {
        EventBus.getDefault().unregister(this);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        Intent intent = new Intent();
        if (SPUtil.getSKIP()) {
            intent.setClass(LoginActivity.this, MainActivity.class);
        } else if (UserInstance.getInstance().userInfo.realName == null || TextUtils.isEmpty(UserInstance.getInstance().userInfo.realName)) {
            intent.setClass(LoginActivity.this, FillInMessageActivity.class);
        } else if (TextUtils.isEmpty(SPUtil.getHEIGHT())) {
            intent.setClass(LoginActivity.this, FillInMessageSecondActivity.class);
        } else {
            intent.setClass(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);

        finish();
    }

    @OnClick({R.id.iv_zhanghao_guanbi, R.id.ll_fuwuxieyi, R.id.iv_password_yincang, R.id.btn_zhanghao_login, R.id.tv_yanzhengma_change, R.id.iv_shoujihao_guanbi, R.id.btn_yanzhengma, R.id.btn_yanzhengma_login, R.id.tv_zhanghao_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_zhanghao_guanbi:
                etZhanghao.setText("");
                break;
            case R.id.ll_fuwuxieyi:
                Intent intent = new Intent(LoginActivity.this, AgreementA.class);
                startActivity(intent);
                break;
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
                if (TextsUtils.getTexts(etPassword).length() > 0) {
                    etPassword.setSelection(TextsUtils.getTexts(etPassword).length());
                }
                break;
            case R.id.btn_zhanghao_login:
                if (!checkZhanghaoInputs()) {
                    return;
                }
                if (!cb.isChecked()) {
                    Uiutils.showToast(getString(R.string.login10));
                }
                LoginPostBean loginPostBeanz = new LoginPostBean();
                loginPostBeanz.loginSign = "1";
                loginPostBeanz.phoneNumber = TextsUtils.getTexts(etZhanghao);
                loginPostBeanz.password = TextsUtils.getTexts(etPassword);
                loginPostBeanz.deviceType = "1";
                loginPresenter.loginPhone(loginPostBeanz);
                break;
            case R.id.tv_yanzhengma_change:
                isPhone = true;
                llShoujihao.setVisibility(View.VISIBLE);
                etShoujihao.setText(TextsUtils.getTexts(etZhanghao));
                llZhanghao.setVisibility(View.GONE);

                break;
            case R.id.iv_shoujihao_guanbi:
                etShoujihao.setText("");
                break;
            case R.id.btn_yanzhengma:
                String strPhone = TextsUtils.getTexts(etShoujihao);
                if (TextsUtils.isEmpty(strPhone)){
                    Uiutils.showToast(getString(R.string.login08));
                    return;
                }
                loginPresenter.getVerifyCode(strPhone);
                break;
            case R.id.btn_yanzhengma_login:
                if (!checkYanzhengmaInputs()) {
                    return;
                }
                if (!cb.isChecked()) {
                    Uiutils.showToast(getString(R.string.login10));
                }
                LoginPostBean loginPostBean = new LoginPostBean();
                loginPostBean.loginSign = "0";
                loginPostBean.phoneNumber = TextsUtils.getTexts(etShoujihao);;
                loginPostBean.captcha =  TextsUtils.getTexts(etYanzhengma);
                loginPostBean.deviceType = "1";
                loginPresenter.loginPhone(loginPostBean);
                break;
            case R.id.tv_zhanghao_change:
                isPhone = false;
                llShoujihao.setVisibility(View.GONE);
                etZhanghao.setText(TextsUtils.getTexts(etShoujihao));
                llZhanghao.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initListener() {
        //手机号
        etShoujihao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    ivShoujihaoGuanbi.setVisibility(View.VISIBLE);
                    btnYanzhengmaLogin.setEnabled(true);
                    tvLineShoujihao.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.color28b28b));
                } else {
                    ivShoujihaoGuanbi.setVisibility(View.GONE);
                    btnYanzhengmaLogin.setEnabled(false);
                    tvLineShoujihao.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.coloreeeeee));
                }
                if (isPhone(s.toString()) && messageWaitTime <= 1) {
                    btnYanzhengma.setEnabled(true);
                } else {
                    btnYanzhengma.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etYanzhengma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    btnYanzhengmaLogin.setEnabled(true);
                } else {
                    btnYanzhengmaLogin.setEnabled(false);
                }
                if (s != null && s.length() > 0) {
                    tvLineYanzhengma.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.color28b28b));
                } else {
                    tvLineYanzhengma.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.coloreeeeee));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //账号
        etZhanghao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    ivZhanghaoGuanbi.setVisibility(View.VISIBLE);
                    tvLineZhanghao.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.color28b28b));
                    btnZhanghaoLogin.setEnabled(true);
                } else {
                    ivZhanghaoGuanbi.setVisibility(View.GONE);
                    tvLineZhanghao.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.coloreeeeee));
                    btnZhanghaoLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    tvLinePassword.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.color28b28b));
                } else {
                    tvLinePassword.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.coloreeeeee));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    /**
     * 检查账号是否输入完全
     */
    private boolean checkZhanghaoInputs() {
        if (TextsUtils.isEmpty(TextsUtils.getTexts(etZhanghao))) {
            Uiutils.showToast(getString(R.string.login14));
            return false;
        }
        if (TextsUtils.isEmpty(TextsUtils.getTexts(etPassword))) {
            Uiutils.showToast(getString(R.string.login15));
            return false;
        }
        return true;
    }

    /**
     * 检查是否输入完全
     */
    private boolean checkYanzhengmaInputs() {
        if (TextsUtils.isEmpty(TextsUtils.getTexts(etShoujihao))) {
            Uiutils.showToast(getString(R.string.login08));
            return false;
        }
        if (TextsUtils.isEmpty(TextsUtils.getTexts(etYanzhengma))) {
            Uiutils.showToast(getString(R.string.login11));
            return false;
        }
        return true;
    }

    private   boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
//            MToast.showToast("手机号应为11位数");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);

            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
//            LogUtil.e(isMatch);
            if (!isMatch) {
                Uiutils.showToast(getString(R.string.login12));
            }
            return isMatch;
        }
    }

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

    /**
     * 验证码倒计时
     */
   private void WaitForNextFetchCode(int nSecond) {
        messageWaitTime = nSecond;
        btnYanzhengma.setEnabled(false);
        btnYanzhengma.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messageWaitTime == 1) {
                    btnYanzhengma.setEnabled(true);
                    btnYanzhengma.setText(getString(R.string.login13));
                    btnYanzhengma.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorWhite));
                } else {
                    messageWaitTime--;
                    SpanUtil.create()
//                            .addSection(String.valueOf(messageWaitTime) + "S"+"重新发送")  //添加带前景色的文字片段
                            .addForeColorSection(messageWaitTime + "s", ContextCompat.getColor(LoginActivity.this, R.color.color28b28b)) //设置相对字体
                            .addForeColorSection(getString(R.string.login13), ContextCompat.getColor(LoginActivity.this, R.color.color666666)) //设置相对字体
                            .showIn(btnYanzhengma); //显示到控件TextView中
                    btnYanzhengma.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 软键盘判断
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



}
