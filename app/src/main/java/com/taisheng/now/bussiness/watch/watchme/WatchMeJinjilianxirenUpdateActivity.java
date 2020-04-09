package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
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
                switch (type) {
                    case "SOS1":
                        WatchMejinjilianxirenActivity.watchNameSos1 = etTongxunluName.getText().toString();
                        WatchMejinjilianxirenActivity.watchSos1 = etPhone.getText().toString();
                        break;
                    case "SOS2":
                        WatchMejinjilianxirenActivity.watchNameSos2 = etTongxunluName.getText().toString();
                        WatchMejinjilianxirenActivity.watchSos2 = etPhone.getText().toString();
                        break;
                    case "SOS3":
                        WatchMejinjilianxirenActivity.watchNameSos3 = etTongxunluName.getText().toString();
                        WatchMejinjilianxirenActivity.watchSos3 = etPhone.getText().toString();
                        break;
                }

                bean.mobilePhoneOne = WatchMejinjilianxirenActivity.watchSos1;
                bean.realNameOne = WatchMejinjilianxirenActivity.watchNameSos1;
                bean.mobilePhoneTwo = WatchMejinjilianxirenActivity.watchSos2;
                bean.realNameTwo = WatchMejinjilianxirenActivity.watchNameSos2;
                bean.mobilePhoneThree = WatchMejinjilianxirenActivity.watchSos3;
                bean.realNameThree = WatchMejinjilianxirenActivity.watchNameSos3;
                ApiUtils.getApiService_hasdialog().updateSosContactSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
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


                break;
            case R.id.tv_cancel:
                UpdateSosContactSettingPostBean bean1 = new UpdateSosContactSettingPostBean();
                bean1.userId = UserInstance.getInstance().getUid();
                bean1.token = UserInstance.getInstance().getToken();
                bean1.deviceId = WatchInstance.getInstance().deviceId;
                switch (type) {
                    case "SOS1":
                        WatchMejinjilianxirenActivity.watchNameSos1 =WatchMejinjilianxirenActivity.watchNameSos2;
                        WatchMejinjilianxirenActivity.watchSos1 =  WatchMejinjilianxirenActivity.watchSos2;
                        WatchMejinjilianxirenActivity.watchNameSos2 =WatchMejinjilianxirenActivity.watchNameSos3;
                        WatchMejinjilianxirenActivity.watchSos2 =  WatchMejinjilianxirenActivity.watchSos3;
                        WatchMejinjilianxirenActivity.watchNameSos3 = "";
                        WatchMejinjilianxirenActivity.watchSos3 = "";
                        break;
                    case "SOS2":
                        WatchMejinjilianxirenActivity.watchNameSos2 =WatchMejinjilianxirenActivity.watchNameSos3;
                        WatchMejinjilianxirenActivity.watchSos2 =  WatchMejinjilianxirenActivity.watchSos3;
                        WatchMejinjilianxirenActivity.watchNameSos3 = "";
                        WatchMejinjilianxirenActivity.watchSos3 = "";
                        break;
                    case "SOS3":
                        WatchMejinjilianxirenActivity.watchNameSos3 = "";
                        WatchMejinjilianxirenActivity.watchSos3 = "";
                        break;
                }

                bean1.mobilePhoneOne = WatchMejinjilianxirenActivity.watchSos1;
                bean1.realNameOne = WatchMejinjilianxirenActivity.watchNameSos1;
                bean1.mobilePhoneTwo = WatchMejinjilianxirenActivity.watchSos2;
                bean1.realNameTwo = WatchMejinjilianxirenActivity.watchNameSos2;
                bean1.mobilePhoneThree = WatchMejinjilianxirenActivity.watchSos3;
                bean1.realNameThree = WatchMejinjilianxirenActivity.watchNameSos3;
                ApiUtils.getApiService_hasdialog().updateSosContactSetting(bean1).enqueue(new TaiShengCallback<BaseBean>() {
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
                etPhone.setText(contacts[1]);
                etTongxunluName.setText(contacts[0]);
                break;
        }
    }
}
