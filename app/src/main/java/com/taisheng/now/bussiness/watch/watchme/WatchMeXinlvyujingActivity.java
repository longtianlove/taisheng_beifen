package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.XinlvXueyaYujingPostBean;
import com.taisheng.now.bussiness.watch.bean.result.XinlvXueyaYujingBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeXinlvyujingActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_xinlvpingzuidazhi)
    TextView tvXinlvpingzuidazhi;
    @BindView(R.id.tv_xinlvpingzuixiaozhi)
    TextView tvXinlvpingzuixiaozhi;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_xinlvyujing);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.watch_msg17));
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setBackgroundResource(R.drawable.icon_edit);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMeXinlvyujingActivity.this, WatchMeXinlvyujingbianjiActivity.class);
                startActivity(intent);
            }
        });
    }

   private void getData() {
        XinlvXueyaYujingPostBean bean = new XinlvXueyaYujingPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().getWatchWarning(bean).enqueue(new TaiShengCallback<BaseBean<XinlvXueyaYujingBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XinlvXueyaYujingBean>> response, BaseBean<XinlvXueyaYujingBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if(message.result==null){
                            return;
                        }
                        WatchInstance.getInstance().temp_bpxyHighMax = message.result.bloodPressureHighMax;
                        WatchInstance.getInstance().temp_bpxyHighMin = message.result.bloodPressureHighMin;

                        WatchInstance.getInstance().temp_bpxyLowMax = message.result.bloodPressureLowMax;
                        WatchInstance.getInstance().temp_bpxyLowMin = message.result.bloodPressureLowMin;

                        WatchInstance.getInstance().temp_bpxyPressureDifferenceMax = message.result.bloodPressureDiffMax;
                        WatchInstance.getInstance().temp_bpxyPressureDifferenceMin = message.result.bloodPressureDiffMin;

                        WatchInstance.getInstance().temp_heartNumMax = message.result.heartRateMax;
                        WatchInstance.getInstance().temp_heartNumMin = message.result.heartRateMin;

                        tvXinlvpingzuidazhi.setText(message.result.heartRateMax + "");
                        tvXinlvpingzuixiaozhi.setText(message.result.heartRateMin + "");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XinlvXueyaYujingBean>> call, Throwable t) {

            }
        });
    }

}
