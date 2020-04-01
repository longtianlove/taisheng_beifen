package com.taisheng.now.bussiness.watch.watchme;

import android.os.Bundle;
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
import com.taisheng.now.util.ToastUtil;
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

public class WatchMeXueyajingBianjiActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_gaoyazuida)
    EditText tvGaoyazuida;
    @BindView(R.id.tv_gaoyazuixiao)
    EditText tvGaoyazuixiao;
    @BindView(R.id.tv_diyazuida)
    EditText tvDiyazuida;
    @BindView(R.id.tv_diyazuixiao)
    EditText tvDiyazuixiao;
    @BindView(R.id.tv_maiyaca)
    EditText tvMaiyaca;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;


    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_xueyayujing_bianji);
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
        tvTitle.setText(getString(R.string.watch_msg18));
    }

    @OnClick({R.id.tv_save, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                if (TextsUtils.isEmpty(TextsUtils.getTexts(tvGaoyazuida)) || TextsUtils.isEmpty(TextsUtils.getTexts(tvGaoyazuixiao)) ||
                        TextsUtils.isEmpty(TextsUtils.getTexts(tvDiyazuida)) || TextsUtils.isEmpty(TextsUtils.getTexts(tvDiyazuixiao))
                        || TextsUtils.isEmpty(TextsUtils.getTexts(tvMaiyaca))) {
                    Uiutils.showToast(getString(R.string.please_input));
                    return;
                }

                XinlvXueyaYujingBean bean = new XinlvXueyaYujingBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;


                bean.bpxyHighMax = Integer.parseInt(TextsUtils.getTexts(tvGaoyazuida));
                bean.bpxyHighMin = Integer.parseInt(TextsUtils.getTexts(tvGaoyazuixiao));

                bean.bpxyLowMax = Integer.parseInt(TextsUtils.getTexts(tvDiyazuida));
                bean.bpxyLowMin = Integer.parseInt(TextsUtils.getTexts(tvDiyazuixiao));

                bean.bpxyPressureDifferenceMax = Integer.parseInt(TextsUtils.getTexts(tvMaiyaca));
                bean.bpxyPressureDifferenceMin = Integer.parseInt(TextsUtils.getTexts(tvMaiyaca));
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
                                WatchMeXueyajingBianjiActivity.this.finish();
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
