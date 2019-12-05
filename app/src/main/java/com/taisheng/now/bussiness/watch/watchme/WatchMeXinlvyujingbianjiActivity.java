package com.taisheng.now.bussiness.watch.watchme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class WatchMeXinlvyujingbianjiActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;


    EditText tv_xinlvpingzuidazhi;
    EditText tv_xinlvpingzuixiaozhi;
    View tv_save;
    View tv_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_xinlvyujingbianji);
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
        tv_xinlvpingzuidazhi = findViewById(R.id.tv_xinlvpingzuidazhi);
        tv_xinlvpingzuixiaozhi = findViewById(R.id.tv_xinlvpingzuixiaozhi);

        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("".equals(tv_xinlvpingzuidazhi.getText().toString()) || "".equals(tv_xinlvpingzuixiaozhi.getText().toString())) {
                    ToastUtil.showAtCenter("请输入值");
                    return;
                }
                XinlvXueyaYujingBean bean = new XinlvXueyaYujingBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.clientId = WatchInstance.getInstance().deviceId;


                bean.bpxyHighMax = WatchInstance.getInstance().temp_bpxyHighMax;
                bean.bpxyHighMin = WatchInstance.getInstance().temp_bpxyHighMin;

                bean.bpxyLowMax = WatchInstance.getInstance().temp_bpxyLowMax;
                bean.bpxyLowMin = WatchInstance.getInstance().temp_bpxyLowMin;

                bean.bpxyPressureDifferenceMax = WatchInstance.getInstance().temp_bpxyPressureDifferenceMax;
                bean.bpxyPressureDifferenceMin = WatchInstance.getInstance().temp_bpxyPressureDifferenceMin;
                bean.heartNumMax = Integer.parseInt(tv_xinlvpingzuidazhi.getText().toString());
                bean.heartNumMin = Integer.parseInt(tv_xinlvpingzuixiaozhi.getText().toString());

                ApiUtils.getApiService().setWatchWarning(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                WatchInstance.getInstance().temp_heartNumMax = bean.heartNumMax;
                                WatchInstance.getInstance().temp_heartNumMin = bean.heartNumMin;
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
//                WatchInstance.getInstance().temp_heartNumMax=message.result.heartNumMax;
//                WatchInstance.getInstance().temp_heartNumMin=message.result.heartNumMin;

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
