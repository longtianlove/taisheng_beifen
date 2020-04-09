package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
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

    public static String watchNameSos1 = "";
    public static String watchSos1 = "";

    public static String watchNameSos2 = "";
    public static String watchSos2 = "";

    public static String watchNameSos3 = "";
    public static String watchSos3 = "";

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_jinjilianxiren);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        type = "1";
        watchNameSos1 = "";
        watchSos1 = "";
        watchNameSos2 = "";
        watchSos2 = "";
        watchNameSos3 = "";
        watchSos3 = "";
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
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    String type = "1";

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initDatas();

    }

    private void initDatas() {
        SosListpostBean bean = new SosListpostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService_hasdialog().listSosContactSetting(bean).enqueue(new TaiShengCallback<BaseBean<NewSosJijinlianxirenlIstResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<NewSosJijinlianxirenlIstResultBean>> response, BaseBean<NewSosJijinlianxirenlIstResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null) {
                            if (TextUtils.isEmpty(message.result.realNameOne)) {
                                type = "1";
                                llFirst.setVisibility(View.GONE);
                                tvAddsos.setVisibility(View.VISIBLE);
                                return;
                            } else {
                                type = "2";
                                llFirst.setVisibility(View.VISIBLE);
                                watchNameSos1 = message.result.realNameOne;
                                watchSos1 = message.result.mobilePhoneOne;
                                tvFirstName.setText(message.result.realNameOne);
                                tvFirstPhone.setText(message.result.mobilePhoneOne);
                            }

                            if (TextUtils.isEmpty(message.result.realNameTwo)) {
                                type = "2";
                                llSecond.setVisibility(View.GONE);
                                tvLineSecond.setVisibility(View.GONE);
                                tvAddsos.setVisibility(View.VISIBLE);
                                return;
                            } else {
                                type = "3";
                                llSecond.setVisibility(View.VISIBLE);
                                watchNameSos2 = message.result.realNameTwo;
                                watchSos2 = message.result.mobilePhoneTwo;
                                tvSecondName.setText(message.result.realNameTwo);
                                tvSecondPhone.setText(message.result.mobilePhoneTwo);
                            }
                            if (TextUtils.isEmpty(message.result.realNameThree)) {
                                type = "3";
                                tvLineThird.setVisibility(View.GONE);
                                llThird.setVisibility(View.GONE);

                            } else {
                                type = "3";
                                llThird.setVisibility(View.VISIBLE);
                                watchNameSos3 = message.result.realNameThree;
                                watchSos3 = message.result.mobilePhoneThree;
                                tvThirdName.setText(message.result.realNameThree);
                                tvThirdPhone.setText(message.result.mobilePhoneThree);
                            }

                            if (TextUtils.isEmpty(message.result.realNameOne)
                                    || TextUtils.isEmpty(message.result.realNameTwo)
                                    || TextUtils.isEmpty(message.result.realNameThree)) {
                                tvAddsos.setVisibility(View.VISIBLE);
                            } else {
                                tvAddsos.setVisibility(View.GONE);
                            }
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
