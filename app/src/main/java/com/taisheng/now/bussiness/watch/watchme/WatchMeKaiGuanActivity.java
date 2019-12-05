package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.AllSettingPostBean;
import com.taisheng.now.bussiness.watch.bean.post.KaiGuanPostBean;
import com.taisheng.now.bussiness.watch.bean.result.AllSettingResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeKaiGuanActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;

    View ll_kaiguan_gps;
    ImageView iv_kaiguan_gps;

    View ll_kaiguan_sos;
    ImageView iv_kaiguan_sos;

    View ll_kaiguan_jinjilianxiren;

    View ll_kaiguan_didianduanxin;
    ImageView iv_kaiguan_didianduanxin;

    View ll_kaiguan_quxiashouhuan;
    ImageView iv_kaiguan_quxiashouhuan;

    View ll_kaiguan_jibu;
    ImageView iv_kaiguan_jibu;

    View ll_kaiguan_fanzhuan;
    ImageView iv_kaiguan_fanzhuan;

    View ll_kaiguan_miandarao;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_kaiguan);
        initView();
//        EventBus.getDefault().register(this);
        initData();
    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_kaiguan_gps = findViewById(R.id.ll_kaiguan_gps);
        ll_kaiguan_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KaiGuanPostBean bean = new KaiGuanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.value = iv_kaiguan_gps.isSelected() ? "0" : "1";
                ApiUtils.getApiService().gpsSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_gps.isSelected()) {
                                    iv_kaiguan_gps.setSelected(false);
                                } else {
                                    iv_kaiguan_gps.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        iv_kaiguan_gps = findViewById(R.id.iv_kaiguan_gps);
        iv_kaiguan_gps.setSelected(false);

        ll_kaiguan_sos = findViewById(R.id.ll_kaiguan_sos);
        ll_kaiguan_sos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                KaiGuanPostBean bean = new KaiGuanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.value = iv_kaiguan_sos.isSelected() ? "0" : "1";
                ApiUtils.getApiService().sosAlarmSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_sos.isSelected()) {
                                    iv_kaiguan_sos.setSelected(false);
                                } else {
                                    iv_kaiguan_sos.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });

        ll_kaiguan_jinjilianxiren = findViewById(R.id.ll_kaiguan_jinjilianxiren);
        ll_kaiguan_jinjilianxiren.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //todo 进入紧急联系人
            }
        });
        iv_kaiguan_sos = findViewById(R.id.iv_kaiguan_sos);
        iv_kaiguan_sos.setSelected(false);

        ll_kaiguan_didianduanxin = findViewById(R.id.ll_kaiguan_didianduanxin);
        ll_kaiguan_didianduanxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KaiGuanPostBean bean = new KaiGuanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.value = iv_kaiguan_didianduanxin.isSelected() ? "0" : "1";
                ApiUtils.getApiService().lowElectSmsSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_didianduanxin.isSelected()) {
                                    iv_kaiguan_didianduanxin.setSelected(false);
                                } else {
                                    iv_kaiguan_didianduanxin.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });

            }
        });
        iv_kaiguan_didianduanxin = findViewById(R.id.iv_kaiguan_didianduanxin);
        iv_kaiguan_didianduanxin.setSelected(false);

        ll_kaiguan_quxiashouhuan = findViewById(R.id.ll_kaiguan_quxiashouhuan);
        ll_kaiguan_quxiashouhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KaiGuanPostBean bean = new KaiGuanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.value = iv_kaiguan_quxiashouhuan.isSelected() ? "0" : "1";
                ApiUtils.getApiService().takeOffWristbandSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_quxiashouhuan.isSelected()) {
                                    iv_kaiguan_quxiashouhuan.setSelected(false);
                                } else {
                                    iv_kaiguan_quxiashouhuan.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        iv_kaiguan_quxiashouhuan = findViewById(R.id.iv_kaiguan_quxiashouhuan);
        iv_kaiguan_quxiashouhuan.setSelected(false);


        ll_kaiguan_jibu = findViewById(R.id.ll_kaiguan_jibu);
        ll_kaiguan_jibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                KaiGuanPostBean bean = new KaiGuanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.value = iv_kaiguan_jibu.isSelected() ? "0" : "1";
                ApiUtils.getApiService().stepCountingSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_jibu.isSelected()) {
                                    iv_kaiguan_jibu.setSelected(false);
                                } else {
                                    iv_kaiguan_jibu.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });

            }
        });
        iv_kaiguan_jibu = findViewById(R.id.iv_kaiguan_jibu);
        iv_kaiguan_jibu.setSelected(false);


        ll_kaiguan_fanzhuan = findViewById(R.id.ll_kaiguan_fanzhuan);
        ll_kaiguan_fanzhuan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                KaiGuanPostBean bean = new KaiGuanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.value = iv_kaiguan_fanzhuan.isSelected() ? "0" : "1";
                ApiUtils.getApiService().flipCheckSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_fanzhuan.isSelected()) {
                                    iv_kaiguan_fanzhuan.setSelected(false);
                                } else {
                                    iv_kaiguan_fanzhuan.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        iv_kaiguan_fanzhuan = findViewById(R.id.iv_kaiguan_fanzhuan);
        iv_kaiguan_fanzhuan.setSelected(false);


        ll_kaiguan_miandarao = findViewById(R.id.ll_kaiguan_miandarao);
        ll_kaiguan_miandarao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMeKaiGuanActivity.this, WatchMeMiandaraoActivity.class);
                startActivity(intent);
            }
        });
    }

    void initData() {
        AllSettingPostBean bean = new AllSettingPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.clientId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().allSetting(bean).enqueue(new TaiShengCallback<BaseBean<AllSettingResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<AllSettingResultBean>> response, BaseBean<AllSettingResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if ("1".equals(message.result.watchGpsSwitch)) {
                            iv_kaiguan_gps.setSelected(true);
                        } else {
                            iv_kaiguan_gps.setSelected(false);
                        }
                        if ("1".equals(message.result.watchSossms)) {
                            iv_kaiguan_sos.setSelected(true);
                        } else {
                            iv_kaiguan_sos.setSelected(false);
                        }
                        if ("1".equals(message.result.watchLowbat)) {
                            iv_kaiguan_didianduanxin.setSelected(true);
                        } else {
                            iv_kaiguan_didianduanxin.setSelected(false);
                        }
                        if ("1".equals(message.result.watchRemove)) {
                            iv_kaiguan_quxiashouhuan.setSelected(true);
                        } else {
                            iv_kaiguan_quxiashouhuan.setSelected(false);
                        }
                        if ("1".equals(message.result.watchPedo)) {
                            iv_kaiguan_jibu.setSelected(true);
                        } else {
                            iv_kaiguan_jibu.setSelected(false);
                        }

                        if("1".equals(message.result.watchSleeptimeSwitch)){
                            iv_kaiguan_fanzhuan.setSelected(true);
                        }else{
                            iv_kaiguan_fanzhuan.setSelected(false);
                        }

                        // 免打扰
                        WatchInstance.getInstance().watchSilencetimeSwitch = message.result.watchSliencetimeSwitch;
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<AllSettingResultBean>> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
