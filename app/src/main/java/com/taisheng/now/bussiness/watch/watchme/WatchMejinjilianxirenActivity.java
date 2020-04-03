package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.SosListpostBean;
import com.taisheng.now.bussiness.watch.bean.result.NewSosJijinlianxirenlIstResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMejinjilianxirenActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_first_name)
    TextView tvFirstName;
    @BindView(R.id.tv_first_phone)
    TextView tvFirstPhone;
    @BindView(R.id.ll_first)
    LinearLayout llFirst;
    @BindView(R.id.tv_second_name)
    TextView tvSecondName;
    @BindView(R.id.tv_second_phone)
    TextView tvSecondPhone;
    @BindView(R.id.ll_second)
    LinearLayout llSecond;
    @BindView(R.id.tv_third_name)
    TextView tvThirdName;
    @BindView(R.id.tv_third_phone)
    TextView tvThirdPhone;
    @BindView(R.id.ll_third)
    LinearLayout llThird;
    @BindView(R.id.tv_addsos)
    TextView tvAddsos;
    @BindView(R.id.tv_line_first)
    TextView tvLineFirst;
    @BindView(R.id.tv_line_second)
    TextView tvLineSecond;
    @BindView(R.id.tv_line_third)
    TextView tvLineThird;

    public String watchNameSos1;
    public String watchSos1;

    public String watchNameSos2;
    public String watchSos2;

    public String watchNameSos3;
    public String watchSos3;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_jinjilianxiren);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.watch_msg36));
    }

    @OnClick({R.id.ll_first, R.id.ll_second, R.id.ll_third, R.id.tv_addsos})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_first:
                if (intent == null) {
                    intent = new Intent(WatchMejinjilianxirenActivity.this, WatchMeJinjilianxirenUpdateActivity.class);
                    intent.putExtra("type", "SOS1");
                    intent.putExtra("name", watchNameSos1);
                    intent.putExtra("phone", watchSos1);
                }
                break;
            case R.id.ll_second:
                if (intent == null) {
                    intent = new Intent(WatchMejinjilianxirenActivity.this, WatchMeJinjilianxirenUpdateActivity.class);
                    intent.putExtra("type", "SOS2");
                    intent.putExtra("name", watchNameSos2);
                    intent.putExtra("phone", watchSos2);
                }
                break;
            case R.id.ll_third:
                if (intent == null) {
                    intent = new Intent(WatchMejinjilianxirenActivity.this, WatchMeJinjilianxirenUpdateActivity.class);
                    intent.putExtra("type", "SOS3");
                    intent.putExtra("name", watchNameSos3);
                    intent.putExtra("phone", watchSos3);
                }
                break;
            case R.id.tv_addsos:
                if (intent == null) {
                    intent = new Intent(WatchMejinjilianxirenActivity.this, WatchMeJinjilianxirenXinzengActivity.class);
                    startActivity(intent);
                }
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDatas();
    }


    private void initDatas() {
        SosListpostBean bean = new SosListpostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().listSosContactSetting(bean).enqueue(new TaiShengCallback<BaseBean<NewSosJijinlianxirenlIstResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<NewSosJijinlianxirenlIstResultBean>> response, BaseBean<NewSosJijinlianxirenlIstResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (TextUtils.isEmpty(message.result.watchNameSos1)) {
                            llFirst.setVisibility(View.GONE);
                        } else {
                            llFirst.setVisibility(View.VISIBLE);
                            watchNameSos1 = message.result.watchNameSos1;
                            watchSos1 = message.result.watchSos1;
                            tvFirstName.setText(message.result.watchNameSos1);
                            tvFirstPhone.setText(message.result.watchSos1);
                        }

                        if (TextUtils.isEmpty(message.result.watchNameSos2)) {
                            llSecond.setVisibility(View.GONE);
                            tvLineSecond.setVisibility(View.GONE);
                        } else {
                            llSecond.setVisibility(View.VISIBLE);
                            watchNameSos2 = message.result.watchNameSos2;
                            watchSos2 = message.result.watchSos2;
                            tvSecondName.setText(message.result.watchNameSos2);
                            tvSecondPhone.setText(message.result.watchSos2);
                        }
                        if (TextUtils.isEmpty(message.result.watchNameSos3)) {
                            tvLineThird.setVisibility(View.GONE);
                            llThird.setVisibility(View.GONE);
                        } else {
                            llThird.setVisibility(View.VISIBLE);
                            watchNameSos3 = message.result.watchNameSos3;
                            watchSos3 = message.result.watchSos3;
                            tvThirdName.setText(message.result.watchNameSos3);
                            tvThirdPhone.setText(message.result.watchSos3);
                        }

                        if (TextUtils.isEmpty(message.result.watchNameSos1)
                                || TextUtils.isEmpty(message.result.watchNameSos2)
                                || TextUtils.isEmpty(message.result.watchNameSos3)) {
                            tvAddsos.setVisibility(View.VISIBLE);
                        } else {
                            tvAddsos.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<NewSosJijinlianxirenlIstResultBean>> call, Throwable t) {

            }
        });
    }


}
