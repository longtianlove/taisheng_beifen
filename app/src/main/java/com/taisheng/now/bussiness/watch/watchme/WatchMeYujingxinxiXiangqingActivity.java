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
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.bean.post.YujingxinxiSetYiduPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeYujingxinxiXiangqingActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    TextView tv_title;
    TextView tv_content;
    TextView tv_time;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchyujingxinxixiangqing);
        initViews();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.watch_msg37));
    }

    void initViews() {

        Intent intent = getIntent();

//        intent.putExtra("warningType", bean.warningContent);
//        intent.putExtra("message", bean.warningContent);
//        intent.putExtra("createTime", bean.createTime);
        String id=intent.getStringExtra("id");
        String warningType = intent.getStringExtra("warningType");
        String message = intent.getStringExtra("message");
        String createTime = intent.getStringExtra("createTime");

        YujingxinxiSetYiduPostBean bean1 = new YujingxinxiSetYiduPostBean();
        bean1.userId = UserInstance.getInstance().getUid();
        bean1.token = UserInstance.getInstance().getToken();
        bean1.id =id;
        ApiUtils.getApiService().watchWarningupdateBykey(bean1).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:


                        break;
                }


            }

            @Override
            public void onFail(Call<BaseBean> call, Throwable t) {

            }
        });


        tv_title = findViewById(R.id.tv_title);
        String temp;
        switch (warningType) {
            case "1":
                temp = "心率信息通知";
                break;
            case "2":
                temp = "血压信息通知";
                break;
            case "3":
                temp = "脉压差信息通知";
                break;
            case "4":
                temp = "低电量通知";
                break;
            case "5":
                temp = "SOS预警通知";
                break;
            case "6":
                temp = "电子围栏预警通知";
                break;
            default:
                temp = "监测信息通知";
                break;
        }
        tv_title.setText(temp);
        tv_content = findViewById(R.id.tv_content);
        String temp1 = message.replace("温馨提示", "\n温馨提示");
        tv_content.setText(temp1);
        tv_time = findViewById(R.id.tv_time);
        tv_time.setText("发布于" + createTime);

    }


}
