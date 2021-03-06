package com.taisheng.now.bussiness.watch.watchme;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.UpdateWatchPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class UpdateRelationShipActivity extends BaseHActivity {
    EditText et_nickname;
    ImageView iv_nickname_guanbi;
    View btn_update;

    @Override
    public void initView() {
        setContentView(R.layout.activity_updaterelationship);
        ButterKnife.bind(this);
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
        tvTitle.setText("更改关系");
    }

    void initViewS() {

        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(et_nickname.getText().toString())) {
                    ToastUtil.showAtCenter("请输入值");
                    return;
                }
                UpdateWatchPostBean bean = new UpdateWatchPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.deviceNickName = WatchInstance.getInstance().deviceNickName;
                bean.headUrl = WatchInstance.getInstance().headUrl;
                bean.relationShip = et_nickname.getText().toString();

                ApiUtils.getApiService_hasdialog().updateDeviceInfo(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                WatchInstance.getInstance().relationShip = bean.relationShip;
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


            }
        });
        iv_nickname_guanbi = (ImageView) findViewById(R.id.iv_nickname_guanbi);
        iv_nickname_guanbi.setVisibility(View.INVISIBLE);
        iv_nickname_guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nickname.setText("");
            }
        });

        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    iv_nickname_guanbi.setVisibility(View.VISIBLE);
                } else {
                    iv_nickname_guanbi.setVisibility(View.GONE);
                }

                if (s != null && s.length() > 0 && !s.equals(UserInstance.getInstance().getNickname())) {
                    btn_update.setEnabled(true);
                } else {
                    btn_update.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_nickname.setText(WatchInstance.getInstance().relationShip);
        CharSequence text = et_nickname.getText();
        //Debug.asserts(text instanceof Spannable);
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }


    }
}
