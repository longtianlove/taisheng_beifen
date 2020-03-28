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

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.SetTongxunluPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeTongxunluxinzengActivity extends BaseHActivity implements ActivityCompat.OnRequestPermissionsResultCallback {


    EditText et_tongxunlu_name;
    EditText et_phone;
    View tv_save;
    View tv_cancel;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_tongxunluxinzeng);
        initViews();
        initDatas();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText("通讯录新增联系人");
    }

    void initViews() {
        et_tongxunlu_name = findViewById(R.id.et_tongxunlu_name);
        et_phone = findViewById(R.id.et_phone);

        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("".equals(et_tongxunlu_name.getText().toString()) || "".equals(et_phone.getText().toString())) {
                    ToastUtil.showAtCenter("请输入值");
                    return;
                }
                SetTongxunluPostBean bean = new SetTongxunluPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.clientId = WatchInstance.getInstance().deviceId;
                bean.deviceId=WatchInstance.getInstance().deviceId;
                bean.phbxNum = nowphxNum+"";
                bean.phbxName = et_tongxunlu_name.getText().toString();
                bean.phbxTelephone = et_phone.getText().toString();
                ApiUtils.getApiService().setWatchPhbx(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("添加成功");
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });

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

    void initDatas() {
        Intent intent = getIntent();
        nowphxNum = intent.getIntExtra("nowphxNum",1);
        String phbxName = intent.getStringExtra("phbxName");
        if (!TextUtils.isEmpty(phbxName)) {
            et_tongxunlu_name.setText(phbxName);
            CharSequence text = et_tongxunlu_name.getText();
            //Debug.asserts(text instanceof Spannable);
            if (text instanceof Spannable) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        }
        String phbxTelephone = intent.getStringExtra("phbxTelephone");
        if (!TextUtils.isEmpty(phbxTelephone)) {
            et_phone.setText(phbxTelephone);
        }
    }


    public int nowphxNum = 1;


}
