package com.taisheng.now.bussiness.watch.watchme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchBean;
import com.taisheng.now.bussiness.watch.bean.post.SetNaozhongPostBean;
import com.taisheng.now.bussiness.watch.bean.post.SosListpostBean;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongLIstBean;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongListResultBean;
import com.taisheng.now.bussiness.watch.bean.result.NewSosJijinlianxirenlIstResultBean;
import com.taisheng.now.bussiness.watch.bean.result.SosLIstBean;
import com.taisheng.now.bussiness.watch.bean.result.SosListResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.WithScrolleViewListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMejinjilianxirenActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;

//    View iv_bianji;


    View ll_first;
    TextView tv_first_name;
    TextView tv_first_phone;

    View view2;
    View ll_second;
    TextView tv_second_name;
    TextView tv_second_phone;

    View view3;
    View ll_third;
    TextView tv_third_name;
    TextView tv_third_phone;

    View iv_addjinjilianxiren;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_jinjilianxiren);
        initView();
    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        iv_bianji = findViewById(R.id.iv_bianji);
//        iv_bianji.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(WatchMejinjilianxirenActivity.this, WatchMeXueyajingBianjiActivity.class);
////                startActivity(intent);
//            }
//        });


        ll_first = findViewById(R.id.ll_first);
        tv_first_name = findViewById(R.id.tv_first_name);
        tv_first_phone = findViewById(R.id.tv_first_phone);


        view2 = findViewById(R.id.view2);
        ll_second = findViewById(R.id.ll_second);
        tv_second_name = findViewById(R.id.tv_second_name);
        tv_second_phone = findViewById(R.id.tv_second_phone);

        view3=findViewById(R.id.view3);
        ll_third = findViewById(R.id.ll_third);
        tv_third_name = findViewById(R.id.tv_third_name);
        tv_third_phone = findViewById(R.id.tv_third_phone);


        iv_addjinjilianxiren = findViewById(R.id.iv_addjinjilianxiren);
        iv_addjinjilianxiren.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMejinjilianxirenActivity.this, WatchMeJinjilianxirenXinzengActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    void initData() {
        SosListpostBean bean = new SosListpostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().listSosContactSetting(bean).enqueue(new TaiShengCallback<BaseBean<NewSosJijinlianxirenlIstResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<NewSosJijinlianxirenlIstResultBean>> response, BaseBean<NewSosJijinlianxirenlIstResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (TextUtils.isEmpty(message.result.watchNameSos1)) {
                            ll_first.setVisibility(View.GONE);
                        } else {
                            ll_first.setVisibility(View.VISIBLE);
                            tv_first_name.setText(message.result.watchNameSos1);
                            tv_first_phone.setText(message.result.watchSos1);
                        }

                        if (TextUtils.isEmpty(message.result.watchNameSos2)) {
                            ll_second.setVisibility(View.GONE);
                            view2.setVisibility(View.GONE);
                        } else {
                            ll_second.setVisibility(View.VISIBLE);
                            tv_second_name.setText(message.result.watchNameSos2);
                            tv_second_phone.setText(message.result.watchSos2);
                        }
                        if (TextUtils.isEmpty(message.result.watchNameSos3)) {
                            view3.setVisibility(View.GONE);
                            ll_third.setVisibility(View.GONE);
                        } else {
                            ll_third.setVisibility(View.VISIBLE);
                            tv_third_name.setText(message.result.watchNameSos3);
                            tv_third_phone.setText(message.result.watchSos3);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<NewSosJijinlianxirenlIstResultBean>> call, Throwable t) {

            }
        });
    }


}
