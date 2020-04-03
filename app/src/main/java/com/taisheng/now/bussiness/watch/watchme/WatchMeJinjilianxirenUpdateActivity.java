package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
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
import com.taisheng.now.bussiness.watch.bean.post.UpdateSosContactSettingPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;
import com.th.j.commonlibrary.utils.PhoneUtil;
import com.th.j.commonlibrary.utils.TextsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class WatchMeJinjilianxirenUpdateActivity extends BaseIvActivity {

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
    public String type;
    public String name;
    public String phone;

    @Override
    public void initView() {
        setContentView(R.layout.activity_jinjilianxiren_update);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");

        etTongxunluName.setText(name);
        etTongxunluName.setSelection(TextsUtils.getTexts(etTongxunluName).length());
        etPhone.setText(phone);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText("更新紧急联系人");
    }

    @OnClick({R.id.tv_mail_list, R.id.tv_save, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_mail_list:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_save:
                if ("".equals(etTongxunluName.getText().toString()) || "".equals(etPhone.getText().toString())) {
                    ToastUtil.showAtCenter("请输入值");
                    return;
                }

                UpdateSosContactSettingPostBean bean = new UpdateSosContactSettingPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.sosUserName = etTongxunluName.getText().toString();
                bean.sosPhone = etPhone.getText().toString();
                bean.type = type;
                ApiUtils.getApiService().updateSosContactSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
//                                WatchInstance.getInstance().deviceNickName = bean.deviceNickName;
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


//                UpdateWatchPostBean bean = new UpdateWatchPostBean();
//                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
//                bean.deviceId = WatchInstance.getInstance().deviceId;
//                bean.relationShip = WatchInstance.getInstance().relationShip;
//                bean.headUrl = WatchInstance.getInstance().headUrl;
//                bean.deviceNickName = et_nickname.getText().toString();
//
//                ApiUtils.getApiService().updateDeviceInfo(bean).enqueue(new TaiShengCallback<BaseBean>() {
//                    @Override
//                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                        switch (message.code) {
//                            case Constants.HTTP_SUCCESS:
//                                WatchInstance.getInstance().deviceNickName = bean.deviceNickName;
//                                finish();
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Call<BaseBean> call, Throwable t) {
//
//                    }
//                });
                break;
            case R.id.tv_cancel:
                this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String[] contacts = PhoneUtil.getPhoneContacts(this, uri);
                etPhone.setText(contacts[0]);
                etTongxunluName.setText(contacts[1]);
                break;
        }
    }
}
