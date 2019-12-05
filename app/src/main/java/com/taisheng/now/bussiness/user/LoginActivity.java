package com.taisheng.now.bussiness.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.SplashActivity;
import com.taisheng.now.bussiness.bean.post.LoginPostBean;
import com.taisheng.now.bussiness.me.FillInMessageActivity;
import com.taisheng.now.bussiness.me.FillInMessageSecondActivity;
import com.taisheng.now.bussiness.me.FuwuxieyiActivity;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dragon on 2019/6/27.
 */

public class LoginActivity extends BaseFragmentActivity implements LoginView {


    //手机号登录
    View ll_shoujihao;
    EditText et_shoujihao;
    ImageView iv_shoujihao_guanbi;
    EditText et_yanzhengma;
    Button btn_yanzhengma;
    TextView btn_yanzhengma_login;
    TextView tv_zhanghao_change;

    //账号
    View ll_zhanghao;
    EditText et_zhanghao;
    ImageView iv_zhanghao_guanbi;
    EditText et_password;
    ImageView iv_password_yincang;
    TextView btn_zhanghao_login;
    TextView tv_yanzhengma_change;


    View ll_fuwuxieyi;


    public boolean isPhone = true;


    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //未进入主页
        SPUtil.putHome(false);
        initView();
        initListener();
        initData();
        loginPresenter = new LoginPresenter(this);
        EventBus.getDefault().register(this);
    }

    void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        //手机号登录
        ll_shoujihao = findViewById(R.id.ll_shoujihao);
        ll_shoujihao.setVisibility(View.VISIBLE);
        isPhone = true;

        et_shoujihao = (EditText) findViewById(R.id.et_shoujihao);
        iv_shoujihao_guanbi = (ImageView) findViewById(R.id.iv_shoujihao_guanbi);
        iv_shoujihao_guanbi.setVisibility(View.GONE);

        et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);
        btn_yanzhengma = (Button) findViewById(R.id.btn_yanzhengma);
        btn_yanzhengma.setEnabled(false);

        btn_yanzhengma_login = (TextView) findViewById(R.id.btn_yanzhengma_login);
        btn_yanzhengma_login.setEnabled(false);

        tv_zhanghao_change = (TextView) findViewById(R.id.tv_zhanghao_change);
        //账号
        ll_zhanghao = findViewById(R.id.ll_zhanghao);
        ll_zhanghao.setVisibility(View.GONE);

        et_zhanghao = (EditText) findViewById(R.id.et_zhanghao);
        iv_zhanghao_guanbi = (ImageView) findViewById(R.id.iv_zhanghao_guanbi);
        iv_zhanghao_guanbi.setVisibility(View.GONE);

        et_password = (EditText) findViewById(R.id.et_password);
        iv_password_yincang = (ImageView) findViewById(R.id.iv_password_yincang);
        yincang = true;
        iv_password_yincang.setImageDrawable(getResources().getDrawable(R.drawable.icon_yincang));
        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        btn_zhanghao_login = (TextView) findViewById(R.id.btn_zhanghao_login);
        btn_zhanghao_login.setEnabled(false);
        tv_yanzhengma_change = (TextView) findViewById(R.id.tv_yanzhengma_change);

        ll_fuwuxieyi = findViewById(R.id.ll_fuwuxieyi);
        ll_fuwuxieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FuwuxieyiActivity.class);
                startActivity(intent);
            }
        });

    }

    void initListener() {
        //手机号
        et_shoujihao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    iv_shoujihao_guanbi.setVisibility(View.VISIBLE);
                    btn_yanzhengma_login.setEnabled(true);
                } else {
                    iv_shoujihao_guanbi.setVisibility(View.GONE);
                    btn_yanzhengma_login.setEnabled(false);
                }
                if (isPhone(s.toString()) && messageWaitTime <= 1) {
                    btn_yanzhengma.setEnabled(true);
                } else {
                    btn_yanzhengma.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_shoujihao_guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_shoujihao.setText("");
            }
        });
        et_yanzhengma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    btn_yanzhengma_login.setEnabled(true);
                } else {
                    btn_yanzhengma_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPhone = et_shoujihao.getText().toString();
                loginPresenter.getVerifyCode(strPhone);

            }
        });
        btn_yanzhengma_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkYanzhengmaInputs()) {
                    return;
                }
                String strPhone = et_shoujihao.getText().toString();
                String verifyCode = et_yanzhengma.getText().toString();
                LoginPostBean loginPostBean = new LoginPostBean();
                loginPostBean.loginSign = "0";
                loginPostBean.phoneNumber = strPhone;
                loginPostBean.captcha = verifyCode;
                loginPostBean.deviceType = "1";

//                try {
                loginPresenter.loginPhone(loginPostBean);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        tv_zhanghao_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPhone = false;
                ll_shoujihao.setVisibility(View.GONE);
                ll_zhanghao.setVisibility(View.VISIBLE);
            }
        });
        //账号
        et_zhanghao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    iv_zhanghao_guanbi.setVisibility(View.VISIBLE);
                    btn_zhanghao_login.setEnabled(true);
                } else {
                    iv_zhanghao_guanbi.setVisibility(View.GONE);
                    btn_zhanghao_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_zhanghao_guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_zhanghao.setText("");
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s != null && s.length() > 0) {
//                    iv_password_yincang.setVisibility(View.VISIBLE);
//                }else{
//                    iv_password_yincang.setVisibility(View.GONE);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        btn_zhanghao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkZhanghaoInputs()) {
                    return;
                }
                String userName = et_zhanghao.getText().toString();
                String password = et_password.getText().toString();
                LoginPostBean loginPostBean = new LoginPostBean();
                loginPostBean.loginSign = "1";
                loginPostBean.phoneNumber = userName;
                loginPostBean.password = password;
                loginPostBean.deviceType = "1";
//                try {
                loginPresenter.loginPhone(loginPostBean);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        tv_yanzhengma_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPhone = true;
                ll_shoujihao.setVisibility(View.VISIBLE);
                ll_zhanghao.setVisibility(View.GONE);
            }
        });

    }

    boolean yincang = true;

    void initData() {

    }

    private boolean checkZhanghaoInputs() {
        if (TextUtils.isEmpty(et_zhanghao.getText().toString())) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(et_password.getText().toString())) {
            Toast.makeText(this, "密码为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 检查是否输入完全
     *
     * @return
     */
    private boolean checkYanzhengmaInputs() {
        if (TextUtils.isEmpty(et_shoujihao.getText().toString())) {
            Toast.makeText(this, "请输入手机号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(et_yanzhengma.getText().toString())) {
            Toast.makeText(this, "验证码为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public static boolean isPhone(String phone) {
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
                ToastUtil.showTost("请填入正确的手机号");
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
}
