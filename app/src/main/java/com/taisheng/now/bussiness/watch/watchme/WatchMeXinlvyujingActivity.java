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
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.XinlvXueyaYujingPostBean;
import com.taisheng.now.bussiness.watch.bean.result.XinlvXueyaYujingBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeXinlvyujingActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    TextView tv_xinlvpingzuidazhi;
    TextView tv_xinlvpingzuixiaozhi;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_xinlvyujing);
        initViews();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
tvTitle.setText("心率预警");
tvRight.setVisibility(View.VISIBLE);
tvRight.setText(getString(R.string.edit));
tvRight.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(WatchMeXinlvyujingActivity.this, WatchMeXinlvyujingbianjiActivity.class);
        startActivity(intent);
    }
});
    }

    void initViews() {




        tv_xinlvpingzuidazhi=findViewById(R.id.tv_xinlvpingzuidazhi);
        tv_xinlvpingzuixiaozhi=findViewById(R.id.tv_xinlvpingzuixiaozhi);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initDatas();
    }

    void initDatas(){
        XinlvXueyaYujingPostBean bean=new XinlvXueyaYujingPostBean();
        bean.userId= UserInstance.getInstance().getUid();
        bean.token=UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().getWatchWarning(bean).enqueue(new TaiShengCallback<BaseBean<XinlvXueyaYujingBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XinlvXueyaYujingBean>> response, BaseBean<XinlvXueyaYujingBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        WatchInstance.getInstance().temp_bpxyHighMax=message.result.bpxyHighMax;
                        WatchInstance.getInstance().temp_bpxyHighMin=message.result.bpxyHighMin;

                        WatchInstance.getInstance().temp_bpxyLowMax=message.result.bpxyLowMax;
                        WatchInstance.getInstance().temp_bpxyLowMin=message.result.bpxyLowMin;

                        WatchInstance.getInstance().temp_bpxyPressureDifferenceMax=message.result.bpxyPressureDifferenceMax;
                        WatchInstance.getInstance().temp_bpxyPressureDifferenceMin=message.result.bpxyPressureDifferenceMin;

                        WatchInstance.getInstance().temp_heartNumMax=message.result.heartNumMax;
                        WatchInstance.getInstance().temp_heartNumMin=message.result.heartNumMin;

                        tv_xinlvpingzuidazhi.setText(message.result.heartNumMax+"");
                        tv_xinlvpingzuixiaozhi.setText(message.result.heartNumMin+"");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XinlvXueyaYujingBean>> call, Throwable t) {

            }
        });
    }


}
