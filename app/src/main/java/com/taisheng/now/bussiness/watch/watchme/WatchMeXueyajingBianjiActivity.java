package com.taisheng.now.bussiness.watch.watchme;

import android.text.TextUtils;
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
    @BindView(R.id.tv_maiyacazuixiaozhi)
    EditText tvMaiyachazuixiaozhi;
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
                        || TextsUtils.isEmpty(TextsUtils.getTexts(tvMaiyaca)) || TextUtils.isEmpty(TextsUtils.getTexts(tvMaiyachazuixiaozhi))) {
                    Uiutils.showToast(getString(R.string.please_input));
                    return;
                }
                if (Integer.parseInt(TextsUtils.getTexts(tvGaoyazuida)) < Integer.parseInt(TextsUtils.getTexts(tvGaoyazuixiao))) {
                    Uiutils.showToast("高压最大值不能小于高压最小值");
                    return;
                }

                if (Integer.parseInt(TextsUtils.getTexts(tvDiyazuida)) < Integer.parseInt(TextsUtils.getTexts(tvDiyazuixiao))) {
                    Uiutils.showToast("低压最大值不能小于低压最小值");
                    return;
                }
                if (Integer.parseInt(TextsUtils.getTexts(tvMaiyaca)) < Integer.parseInt(TextsUtils.getTexts(tvMaiyachazuixiaozhi))) {
                    Uiutils.showToast("脉压差最大值不能小于脉压差最小值");
                    return;
                }

                XinlvXueyaYujingBean bean = new XinlvXueyaYujingBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;


                bean.bloodPressureHighMax = Integer.parseInt(TextsUtils.getTexts(tvGaoyazuida));
                bean.bloodPressureHighMin = Integer.parseInt(TextsUtils.getTexts(tvGaoyazuixiao));

                bean.bloodPressureLowMax = Integer.parseInt(TextsUtils.getTexts(tvDiyazuida));
                bean.bloodPressureLowMin = Integer.parseInt(TextsUtils.getTexts(tvDiyazuixiao));

                bean.bloodPressureDiffMax = Integer.parseInt(TextsUtils.getTexts(tvMaiyaca));
                bean.bloodPressureDiffMin = Integer.parseInt(TextsUtils.getTexts(tvMaiyachazuixiaozhi));
                bean.heartRateMax = WatchInstance.getInstance().temp_heartNumMax;
                bean.heartRateMin = WatchInstance.getInstance().temp_heartNumMin;

                ApiUtils.getApiService_hasdialog().setWatchWarning(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                WatchInstance.getInstance().temp_bpxyHighMax = bean.bloodPressureHighMax;
                                WatchInstance.getInstance().temp_bpxyHighMin = bean.bloodPressureHighMin;

                                WatchInstance.getInstance().temp_bpxyLowMax = bean.bloodPressureLowMax;
                                WatchInstance.getInstance().temp_bpxyLowMin = bean.bloodPressureLowMin;

                                WatchInstance.getInstance().temp_bpxyPressureDifferenceMax = bean.bloodPressureDiffMax;
                                WatchInstance.getInstance().temp_bpxyPressureDifferenceMin = bean.bloodPressureDiffMin;
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
