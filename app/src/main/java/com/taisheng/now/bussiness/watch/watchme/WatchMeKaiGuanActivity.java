package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.KaiguanSettingPostBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.AllSettingPostBean;
import com.taisheng.now.bussiness.watch.bean.result.AllSettingResultBean;
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

public class WatchMeKaiGuanActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.iv_kaiguan_gps)
    ImageView ivKaiguanGps;
    @BindView(R.id.ll_kaiguan_gps)
    LinearLayout llKaiguanGps;
    @BindView(R.id.iv_kaiguan_sos)
    ImageView ivKaiguanSos;
    @BindView(R.id.ll_kaiguan_sos)
    LinearLayout llKaiguanSos;
    @BindView(R.id.ll_kaiguan_jinjilianxiren)
    LinearLayout llKaiguanJinjilianxiren;
    @BindView(R.id.iv_kaiguan_didianduanxin)
    ImageView ivKaiguanDidianduanxin;
    @BindView(R.id.ll_kaiguan_didianduanxin)
    LinearLayout llKaiguanDidianduanxin;
    @BindView(R.id.iv_kaiguan_quxiashouhuan)
    ImageView ivKaiguanQuxiashouhuan;
    @BindView(R.id.ll_kaiguan_quxiashouhuan)
    LinearLayout llKaiguanQuxiashouhuan;
    @BindView(R.id.iv_kaiguan_jibu)
    ImageView ivKaiguanJibu;
    @BindView(R.id.ll_kaiguan_jibu)
    LinearLayout llKaiguanJibu;
    @BindView(R.id.iv_kaiguan_fanzhuan)
    ImageView ivKaiguanFanzhuan;
    @BindView(R.id.ll_kaiguan_fanzhuan)
    LinearLayout llKaiguanFanzhuan;
    @BindView(R.id.ll_kaiguan_miandarao)
    LinearLayout llKaiguanMiandarao;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_kaiguan);
        ButterKnife.bind(this);
        initDatas();
    }

    @Override
    public void initData() {
        ivKaiguanGps.setSelected(false);
        ivKaiguanSos.setSelected(false);
        ivKaiguanDidianduanxin.setSelected(false);
        ivKaiguanQuxiashouhuan.setSelected(false);
        ivKaiguanJibu.setSelected(false);
        ivKaiguanFanzhuan.setSelected(false);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.watch_msg06));
    }

    void initDatas() {
        AllSettingPostBean bean = new AllSettingPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService_hasdialog().allSetting(bean).enqueue(new TaiShengCallback<BaseBean<AllSettingResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<AllSettingResultBean>> response, BaseBean<AllSettingResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result == null) {
                            return;
                        }
                        if ("1".equals(message.result.phoneBookSwitch)) {
                            ivKaiguanGps.setSelected(true);
                        } else {
                            ivKaiguanGps.setSelected(false);
                        }
                        if ("1".equals(message.result.watchSossms)) {
                            ivKaiguanSos.setSelected(true);
                        } else {
                            ivKaiguanSos.setSelected(false);
                        }
                        if ("1".equals(message.result.watchLowbat)) {
                            ivKaiguanDidianduanxin.setSelected(true);
                        } else {
                            ivKaiguanDidianduanxin.setSelected(false);
                        }
                        if ("1".equals(message.result.watchRemove)) {
                            ivKaiguanQuxiashouhuan.setSelected(true);
                        } else {
                            ivKaiguanQuxiashouhuan.setSelected(false);
                        }
                        if ("1".equals(message.result.stepSwitch)) {
                            ivKaiguanJibu.setSelected(true);
                        } else {
                            ivKaiguanJibu.setSelected(false);
                        }

                        if ("1".equals(message.result.watchSleeptimeSwitch)) {
                            ivKaiguanFanzhuan.setSelected(true);
                        } else {
                            ivKaiguanFanzhuan.setSelected(false);
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


    @OnClick({R.id.ll_kaiguan_gps, R.id.ll_kaiguan_sos, R.id.ll_kaiguan_jinjilianxiren, R.id.ll_kaiguan_didianduanxin, R.id.ll_kaiguan_quxiashouhuan, R.id.ll_kaiguan_jibu, R.id.ll_kaiguan_fanzhuan, R.id.ll_kaiguan_miandarao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_kaiguan_gps:
                KaiguanSettingPostBean bean = new KaiguanSettingPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.switchType = "APPLOCK";
//                bean.switchValue = ivKaiguanGps.isSelected() ? "0" : "1";
                bean.value=ivKaiguanGps.isSelected() ? "0" : "1";
                ApiUtils.getApiService_hasdialog().phonebookswitch(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (ivKaiguanGps.isSelected()) {
                                    ivKaiguanGps.setSelected(false);
                                } else {
                                    ivKaiguanGps.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
//
//                KaiGuanPostBean bean = new KaiGuanPostBean();
//                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
//                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.value = iv_kaiguan_gps.isSelected() ? "0" : "1";
//                ApiUtils.getApiService().gpsSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                if (iv_kaiguan_gps.isSelected()) {
//                                    iv_kaiguan_gps.setSelected(false);
//                                } else {
//                                    iv_kaiguan_gps.setSelected(true);
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.ll_kaiguan_sos:


                KaiguanSettingPostBean bean2 = new KaiguanSettingPostBean();
                bean2.userId = UserInstance.getInstance().getUid();
                bean2.token = UserInstance.getInstance().getToken();
                bean2.deviceId = WatchInstance.getInstance().deviceId;
                bean2.switchType = "SOSSMS";
                bean2.switchValue = ivKaiguanSos.isSelected() ? "0" : "1";
                ApiUtils.getApiService_hasdialog().watchSwitchConfig(bean2).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (ivKaiguanSos.isSelected()) {
                                    ivKaiguanSos.setSelected(false);
                                } else {
                                    ivKaiguanSos.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


//                KaiGuanPostBean bean = new KaiGuanPostBean();
//                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
//                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.value = iv_kaiguan_sos.isSelected() ? "0" : "1";
//                ApiUtils.getApiService().sosAlarmSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                if (iv_kaiguan_sos.isSelected()) {
//                                    iv_kaiguan_sos.setSelected(false);
//                                } else {
//                                    iv_kaiguan_sos.setSelected(true);
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.ll_kaiguan_jinjilianxiren:
                Intent intent = new Intent(WatchMeKaiGuanActivity.this, WatchMejinjilianxirenActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_kaiguan_didianduanxin:
                KaiguanSettingPostBean bean3 = new KaiguanSettingPostBean();
                bean3.userId = UserInstance.getInstance().getUid();
                bean3.token = UserInstance.getInstance().getToken();
                bean3.deviceId = WatchInstance.getInstance().deviceId;
                bean3.switchType = "LOWBAT";
                bean3.switchValue = ivKaiguanDidianduanxin.isSelected() ? "0" : "1";
                ApiUtils.getApiService_hasdialog().watchSwitchConfig(bean3).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (ivKaiguanDidianduanxin.isSelected()) {
                                    ivKaiguanDidianduanxin.setSelected(false);
                                } else {
                                    ivKaiguanDidianduanxin.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


//                KaiGuanPostBean bean = new KaiGuanPostBean();
//                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
//                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.value = iv_kaiguan_didianduanxin.isSelected() ? "0" : "1";
//                ApiUtils.getApiService().lowElectSmsSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                if (iv_kaiguan_didianduanxin.isSelected()) {
//                                    iv_kaiguan_didianduanxin.setSelected(false);
//                                } else {
//                                    iv_kaiguan_didianduanxin.setSelected(true);
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.ll_kaiguan_quxiashouhuan:
                KaiguanSettingPostBean bean4 = new KaiguanSettingPostBean();
                bean4.userId = UserInstance.getInstance().getUid();
                bean4.token = UserInstance.getInstance().getToken();
                bean4.deviceId = WatchInstance.getInstance().deviceId;
                bean4.switchType = "REMOVE";
                bean4.switchValue = ivKaiguanQuxiashouhuan.isSelected() ? "0" : "1";
                ApiUtils.getApiService_hasdialog().watchSwitchConfig(bean4).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (ivKaiguanQuxiashouhuan.isSelected()) {
                                    ivKaiguanQuxiashouhuan.setSelected(false);
                                } else {
                                    ivKaiguanQuxiashouhuan.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
//                KaiGuanPostBean bean = new KaiGuanPostBean();
//                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
//                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.value = iv_kaiguan_quxiashouhuan.isSelected() ? "0" : "1";
//                ApiUtils.getApiService().takeOffWristbandSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                if (iv_kaiguan_quxiashouhuan.isSelected()) {
//                                    iv_kaiguan_quxiashouhuan.setSelected(false);
//                                } else {
//                                    iv_kaiguan_quxiashouhuan.setSelected(true);
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.ll_kaiguan_jibu:
                KaiguanSettingPostBean bean5 = new KaiguanSettingPostBean();
                bean5.userId = UserInstance.getInstance().getUid();
                bean5.token = UserInstance.getInstance().getToken();
                bean5.deviceId = WatchInstance.getInstance().deviceId;
//                bean5.switchType = " PEDO";
//                bean5.switchValue = ivKaiguanJibu.isSelected() ? "0" : "1";
                bean5.value = ivKaiguanJibu.isSelected() ? "0" : "1";
                ApiUtils.getApiService_hasdialog().edometerSwitch(bean5).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (ivKaiguanJibu.isSelected()) {
                                    ivKaiguanJibu.setSelected(false);
                                } else {
                                    ivKaiguanJibu.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });

//                KaiGuanPostBean bean = new KaiGuanPostBean();
//                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
//                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.value = iv_kaiguan_jibu.isSelected() ? "0" : "1";
//                ApiUtils.getApiService().stepCountingSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                if (iv_kaiguan_jibu.isSelected()) {
//                                    iv_kaiguan_jibu.setSelected(false);
//                                } else {
//                                    iv_kaiguan_jibu.setSelected(true);
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.ll_kaiguan_fanzhuan:
                KaiguanSettingPostBean bean6 = new KaiguanSettingPostBean();
                bean6.userId = UserInstance.getInstance().getUid();
                bean6.token = UserInstance.getInstance().getToken();
                bean6.deviceId = WatchInstance.getInstance().deviceId;
                bean6.switchType = " SLEEPTIME";
                bean6.switchValue = ivKaiguanFanzhuan.isSelected() ? "0" : "1";
                ApiUtils.getApiService_hasdialog().watchSwitchConfig(bean6).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (ivKaiguanFanzhuan.isSelected()) {
                                    ivKaiguanFanzhuan.setSelected(false);
                                } else {
                                    ivKaiguanFanzhuan.setSelected(true);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
//                KaiGuanPostBean bean = new KaiGuanPostBean();
//                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
//                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.value = iv_kaiguan_fanzhuan.isSelected() ? "0" : "1";
//                ApiUtils.getApiService().flipCheckSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                if (iv_kaiguan_fanzhuan.isSelected()) {
//                                    iv_kaiguan_fanzhuan.setSelected(false);
//                                } else {
//                                    iv_kaiguan_fanzhuan.setSelected(true);
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.ll_kaiguan_miandarao:
                Intent intent2 = new Intent(WatchMeKaiGuanActivity.this, WatchMeMiandaraoActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
