package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.th.j.commonlibrary.global.Global;
import com.th.j.commonlibrary.utils.PhoneUtil;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchSosUpdataActivity extends BaseIvActivity {

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
    private String intenType;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watch_sos_updata);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        intenType = getIntent().getStringExtra(Global.INTENT_TYPE);

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        if (Global.SOS_UPDATA.equals(intenType)) {
            tvTitle.setText("更新紧急联系人");
            tvCancel.setTextColor(ContextCompat.getColor(this, R.color.colorff0202));
        } else {
            tvTitle.setText("新增紧急联系人");
            tvCancel.setTextColor(ContextCompat.getColor(this, R.color.color999999));
        }
    }


    @OnClick({R.id.tv_mail_list, R.id.tv_save, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mail_list:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_save://只能添加3个

                break;
            case R.id.tv_cancel:
                if (Global.SOS_UPDATA.equals(intenType)) {

                } else {
                    this.finish();
                }
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
                String[] contacts = PhoneUtil.getPhoneContacts(this,uri);
                etPhone.setText(contacts[0]);
                etTongxunluName.setText(contacts[1]);
                break;
        }
    }
}