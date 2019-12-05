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
import com.taisheng.now.bussiness.watch.bean.post.KaiGuanPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeMiandaraoActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;

    View ll_kaiguan_gps;
    ImageView iv_kaiguan_gps;


    View ll_kaiguan_miandarao;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_miandarao);
        initView();
//        EventBus.getDefault().register(this);
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
                ApiUtils.getApiService().notDisturbSwitchSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (iv_kaiguan_gps.isSelected()) {
                                    iv_kaiguan_gps.setSelected(false);
                                } else {
                                    iv_kaiguan_gps.setSelected(true);
                                }
                                WatchInstance.getInstance().watchSilencetimeSwitch=bean.value;
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
        if("1".equals(WatchInstance.getInstance().watchSilencetimeSwitch)){
            iv_kaiguan_gps.setSelected(true);
        }else{
            iv_kaiguan_gps.setSelected(false);
        }



        ll_kaiguan_miandarao = findViewById(R.id.ll_kaiguan_miandarao);
        ll_kaiguan_miandarao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WatchMeMiandaraoActivity.this, WatchMianDaraoSettingliActivity.class);
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
