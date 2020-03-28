package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.KaiguanSettingPostBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeMiandaraoActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    View ll_kaiguan_gps;
    ImageView iv_kaiguan_gps;


    View ll_kaiguan_miandarao;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_miandarao);
        ButterKnife.bind(this);
        initViews();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
tvTitle.setText("免打扰设置");
    }

    void initViews() {
        ll_kaiguan_gps = findViewById(R.id.ll_kaiguan_gps);
        ll_kaiguan_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                KaiguanSettingPostBean bean=new KaiguanSettingPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId=WatchInstance.getInstance().deviceId;
                bean.switchType="SILENCETIME";
                bean.switchValue= iv_kaiguan_gps.isSelected() ? "0" : "1";
                ApiUtils.getApiService().watchSwitchConfig(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_gps.isSelected()) {
                                    iv_kaiguan_gps.setSelected(false);
                                } else {
                                    iv_kaiguan_gps.setSelected(true);
                                }
                                WatchInstance.getInstance().watchSilencetimeSwitch=bean.switchValue;
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
//                ApiUtils.getApiService().notDisturbSwitchSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                if (iv_kaiguan_gps.isSelected()) {
//                                    iv_kaiguan_gps.setSelected(false);
//                                } else {
//                                    iv_kaiguan_gps.setSelected(true);
//                                }
//                                WatchInstance.getInstance().watchSilencetimeSwitch=bean.value;
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
            }
        });
        iv_kaiguan_gps = findViewById(R.id.iv_kaiguan_gps);
        if("1".equals(WatchInstance.getInstance().watchSilencetimeSwitch)){
            iv_kaiguan_gps.setSelected(true);
        }else{
            iv_kaiguan_gps.setSelected(false);
        }



        ll_kaiguan_miandarao = findViewById(R.id.ll_kaiguan_miandarao);
        ll_kaiguan_miandarao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WatchMeMiandaraoActivity.this, WatchMiandaoraoListActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
