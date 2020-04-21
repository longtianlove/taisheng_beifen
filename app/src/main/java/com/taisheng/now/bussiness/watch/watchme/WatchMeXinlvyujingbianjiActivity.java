package com.taisheng.now.bussiness.watch.watchme;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.result.XinlvXueyaYujingBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Uiutils;
import com.th.j.commonlibrary.utils.TextsUtils;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeXinlvyujingbianjiActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_xinlvpingzuidazhi)
    EditText tvXinlvpingzuidazhi;
    @BindView(R.id.tv_xinlvpingzuixiaozhi)
    EditText tvXinlvpingzuixiaozhi;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_xinlvyujingbianji);
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
        tvTitle.setText(getString(R.string.watch_msg17));
    }

    @OnClick({R.id.tv_save, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                if (TextsUtils.isEmpty(TextsUtils.getTexts(tvXinlvpingzuidazhi)) || TextsUtils.isEmpty(TextsUtils.getTexts(tvXinlvpingzuixiaozhi))) {
                    Uiutils.showToast(getString(R.string.please_input));
                    return;
                }
                if (Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuidazhi)) < Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuixiaozhi))) {
                    Uiutils.showToast("心率最大值不能小于心率最小值");
                    return;
                }
                XinlvXueyaYujingBean bean = new XinlvXueyaYujingBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;


                bean.bloodPressureHighMax = WatchInstance.getInstance().temp_bpxyHighMax;
                bean.bloodPressureHighMin = WatchInstance.getInstance().temp_bpxyHighMin;

                bean.bloodPressureLowMax = WatchInstance.getInstance().temp_bpxyLowMax;
                bean.bloodPressureLowMin = WatchInstance.getInstance().temp_bpxyLowMin;

                bean.bloodPressureDiffMax = WatchInstance.getInstance().temp_bpxyPressureDifferenceMax;
                bean.bloodPressureDiffMin = WatchInstance.getInstance().temp_bpxyPressureDifferenceMin;
                bean.heartRateMax = Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuidazhi));
                bean.heartRateMin = Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuixiaozhi));

                ApiUtils.getApiService_hasdialog().setWatchWarning(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                WatchInstance.getInstance().temp_heartNumMax = bean.heartRateMax;
                                WatchInstance.getInstance().temp_heartNumMin = bean.heartRateMin;
                                WatchMeXinlvyujingbianjiActivity.this.finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
                break;
            case R.id.tv_cancel:
                this.finish();
                break;
        }
    }
}
