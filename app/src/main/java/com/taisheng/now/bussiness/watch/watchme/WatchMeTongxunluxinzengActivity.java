package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.SetTongxunluPostBean;
import com.taisheng.now.bussiness.watch.bean.post.TongxunluDeletePostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.th.j.commonlibrary.global.Global;
import com.th.j.commonlibrary.utils.TextsUtils;
import com.th.j.commonlibrary.wight.CircleImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeTongxunluxinzengActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.sdv_header)
    CircleImageView sdvHeader;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_mail_list)
    TextView tvMailList;
    @BindView(R.id.et_tongxunlu_name)
    EditText etTongxunluName;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    public int nowphxNum = 1;
    private String intenType;
    private String phbxNum;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_tongxunluxinzeng);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        intenType = getIntent().getStringExtra(Global.INTENT_TYPE);
        nowphxNum = getIntent().getIntExtra("nowphxNum", 1);
        String phbxName = getIntent().getStringExtra("phbxName");
        phbxNum = getIntent().getStringExtra("phbxNum");
        if (!TextUtils.isEmpty(phbxName)) {
            etTongxunluName.setText(phbxName);
            etTongxunluName.setSelection(TextsUtils.getTexts(etTongxunluName).length());
        }
        String phbxTelephone = getIntent().getStringExtra("phbxTelephone");
        if (!TextUtils.isEmpty(phbxTelephone)) {
            etPhone.setText(phbxTelephone);
        }
    }

    @Override
    public void addData() {
        Glide.with(this)
                .load("")
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ad_sculpture)
                        .error(R.drawable.ad_sculpture)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(sdvHeader);
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        if (Global.MAIL_ADD.equals(intenType)) {
            tvTitle.setText(getString(R.string.watch_msg12));
            tvCancel.setTextColor(ContextCompat.getColor(this, R.color.colorbbbbbb));
            tvCancel.setText(getString(R.string.cancal));
        } else {
            tvTitle.setText(getString(R.string.watch_msg13));
            tvCancel.setTextColor(ContextCompat.getColor(this, R.color.colorff0202));
            tvCancel.setText(getString(R.string.delete));
        }
    }


    @OnClick({R.id.sdv_header, R.id.tv_header, R.id.tv_mail_list, R.id.tv_save, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sdv_header:
            case R.id.tv_header:
                break;
            case R.id.tv_mail_list:
                break;
            case R.id.tv_save:
                if (TextsUtils.isEmpty(TextsUtils.getTexts(etPhone))) {
                    Uiutils.showToast(getString(R.string.watch_msg10));
                    return;
                }
                if (TextsUtils.isEmpty(TextsUtils.getTexts(etTongxunluName))) {
                    Uiutils.showToast(getString(R.string.watch_msg11));
                    return;
                }
                SetTongxunluPostBean bean = new SetTongxunluPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.clientId = WatchInstance.getInstance().deviceId;
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.phbxNum = nowphxNum + "";
                bean.phbxName = TextsUtils.getTexts(etTongxunluName);
                bean.phbxTelephone = TextsUtils.getTexts(etPhone);
                ApiUtils.getApiService().setWatchPhbx(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                Uiutils.showToast(getString(R.string.add_success));
                                WatchMeTongxunluxinzengActivity.this.finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
                break;
            case R.id.tv_cancel:
                if (Global.MAIL_ADD.equals(intenType)) {
                    this.finish();
                } else {
                    TongxunluDeletePostBean tongxunluDeletePostBean = new TongxunluDeletePostBean();
                    tongxunluDeletePostBean.userId = UserInstance.getInstance().getUid();
                    tongxunluDeletePostBean.token = UserInstance.getInstance().getToken();
                    tongxunluDeletePostBean.deviceId = WatchInstance.getInstance().deviceId;
                    tongxunluDeletePostBean.phbxNum = phbxNum;
                    ApiUtils.getApiService().setWatchDphbx(tongxunluDeletePostBean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    Uiutils.showToast(getString(R.string.delete_success));
                                    initData();
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
                }

                break;
        }
    }
}
