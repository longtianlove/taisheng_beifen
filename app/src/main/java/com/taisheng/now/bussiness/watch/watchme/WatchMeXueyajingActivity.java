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
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.XinlvXueyaYujingPostBean;
import com.taisheng.now.bussiness.watch.bean.result.XinlvXueyaYujingBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeXueyajingActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;

    View iv_bianji;

    TextView tv_gaoyazuida;
    TextView tv_gaoyazuixiao;
    TextView tv_diyazuida;
    TextView tv_diyazuixiao;

    TextView tv_maiyaca;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_xueyayujing);
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

        iv_bianji = findViewById(R.id.iv_bianji);
        iv_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMeXueyajingActivity.this, WatchMeXueyajingBianjiActivity.class);
                startActivity(intent);
            }
        });

        tv_gaoyazuida = findViewById(R.id.tv_gaoyazuida);
        tv_gaoyazuixiao = findViewById(R.id.tv_gaoyazuixiao);
        tv_diyazuida=findViewById(R.id.tv_diyazuida);
        tv_diyazuixiao=findViewById(R.id.tv_diyazuixiao);
        tv_maiyaca=findViewById(R.id.tv_maiyaca);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    void initData() {
        XinlvXueyaYujingPostBean bean = new XinlvXueyaYujingPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.clientId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().getWatchWarning(bean).enqueue(new TaiShengCallback<BaseBean<XinlvXueyaYujingBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XinlvXueyaYujingBean>> response, BaseBean<XinlvXueyaYujingBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        WatchInstance.getInstance().temp_bpxyHighMax = message.result.bpxyHighMax;
                        WatchInstance.getInstance().temp_bpxyHighMin = message.result.bpxyHighMin;

                        WatchInstance.getInstance().temp_bpxyLowMax = message.result.bpxyLowMax;
                        WatchInstance.getInstance().temp_bpxyLowMin = message.result.bpxyLowMin;

                        WatchInstance.getInstance().temp_bpxyPressureDifferenceMax = message.result.bpxyPressureDifferenceMax;
                        WatchInstance.getInstance().temp_bpxyPressureDifferenceMin = message.result.bpxyPressureDifferenceMin;

                        WatchInstance.getInstance().temp_heartNumMax = message.result.heartNumMax;
                        WatchInstance.getInstance().temp_heartNumMin = message.result.heartNumMin;

                        tv_gaoyazuida.setText(message.result.bpxyHighMax + "");
                        tv_gaoyazuixiao.setText(message.result.bpxyHighMin+"");
                        tv_diyazuida.setText(message.result.bpxyLowMax+"");
                        tv_diyazuixiao.setText(message.result.bpxyHighMin+"");
                        tv_maiyaca.setText(message.result.bpxyPressureDifferenceMax+"");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XinlvXueyaYujingBean>> call, Throwable t) {

            }
        });
    }


}
