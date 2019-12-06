package com.taisheng.now.bussiness.watch.watchme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.taisheng.now.bussiness.watch.bean.result.NaozhongLIstBean;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongListResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.WithScrolleViewListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeYujingxinxiXiangqingActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;
    TextView tv_title;
    TextView tv_content;
    TextView tv_time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchyujingxinxixiangqing);
        initView();
//        EventBus.getDefault().register(this);

    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

//        intent.putExtra("warningType", bean.warningContent);
//        intent.putExtra("message", bean.warningContent);
//        intent.putExtra("createTime", bean.createTime);
        String warningType = intent.getStringExtra("warningType");
        String message = intent.getStringExtra("message");
        String createTime = intent.getStringExtra("createTime");

        tv_title = findViewById(R.id.tv_title);
        String temp;
        switch (warningType) {
            case "heart":
                temp = "心率信息通知";
                break;
            case "bpxy":
                temp = "血压信息通知";
                break;
            case "bphrt":
                temp = "心率信息通知";
                break;
            case "LK":
                temp = "低电量通知";
                break;
            default:
                temp = "监测信息通知";
                break;
        }
        tv_title.setText(temp);
        tv_content=findViewById(R.id.tv_content);
        tv_content.setText(message);
        tv_time=findViewById(R.id.tv_time);
        tv_time.setText("发布于"+createTime);

    }


}
