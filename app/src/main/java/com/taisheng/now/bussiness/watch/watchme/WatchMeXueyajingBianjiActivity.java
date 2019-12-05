package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.result.XinlvXueyaYujingBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeXueyajingBianjiActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;


    EditText tv_gaoyazuida;
    EditText tv_gaoyazuixiao;
    EditText tv_diyazuida;
    EditText tv_diyazuixiao;
    EditText tv_maiyaca;

    View tv_save;
    View tv_cancel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_xueyayujing_bianji);
        initView();
    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_gaoyazuida = findViewById(R.id.tv_gaoyazuida);
        tv_gaoyazuixiao = findViewById(R.id.tv_gaoyazuixiao);
        tv_diyazuida = findViewById(R.id.tv_diyazuida);
        tv_diyazuixiao = findViewById(R.id.tv_diyazuixiao);
        tv_maiyaca = findViewById(R.id.tv_maiyaca);

        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(tv_gaoyazuida.getText().toString())
                        || "".equals(tv_gaoyazuixiao.getText().toString())
                        || "".equals(tv_diyazuida.getText().toString())
                        || "".equals(tv_diyazuixiao.getText().toString())
                        || "".equals(tv_maiyaca.getText().toString())
                ) {
                    ToastUtil.showAtCenter("请输入值");
                    return;
                }


                XinlvXueyaYujingBean bean = new XinlvXueyaYujingBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.clientId = WatchInstance.getInstance().deviceId;


                bean.bpxyHighMax = Integer.parseInt(tv_gaoyazuida.getText().toString());
                bean.bpxyHighMin = Integer.parseInt(tv_gaoyazuixiao.getText().toString());

                bean.bpxyLowMax = Integer.parseInt(tv_diyazuida.getText().toString());
                bean.bpxyLowMin = Integer.parseInt(tv_diyazuixiao.getText().toString());

                bean.bpxyPressureDifferenceMax = Integer.parseInt(tv_maiyaca.getText().toString());
                bean.bpxyPressureDifferenceMin = Integer.parseInt(tv_maiyaca.getText().toString());
                bean.heartNumMax = WatchInstance.getInstance().temp_heartNumMax;
                bean.heartNumMin = WatchInstance.getInstance().temp_heartNumMin;

                ApiUtils.getApiService().setWatchWarning(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                WatchInstance.getInstance().temp_bpxyHighMax = bean.bpxyHighMax;
                                WatchInstance.getInstance().temp_bpxyHighMin = bean.bpxyHighMin;

                                WatchInstance.getInstance().temp_bpxyLowMax = bean.bpxyLowMax;
                                WatchInstance.getInstance().temp_bpxyLowMin = bean.bpxyLowMin;

                                WatchInstance.getInstance().temp_bpxyPressureDifferenceMax = bean.bpxyPressureDifferenceMax;
                                WatchInstance.getInstance().temp_bpxyPressureDifferenceMin = bean.bpxyPressureDifferenceMin;
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
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
