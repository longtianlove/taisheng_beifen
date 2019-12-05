package com.taisheng.now.bussiness.watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.ZXing.ZXingActivity;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.bussiness.me.FuwuxieyiActivity;
import com.taisheng.now.bussiness.me.YisixieyiActivity;
import com.taisheng.now.util.Apputil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dragon on 2019/6/28.
 */

public class BindWatchsActivity extends BaseActivity {
    View iv_back;


    View tv_adddevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindwatchs);
        initView();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_adddevice = findViewById(R.id.tv_adddevice);
        tv_adddevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZXingActivity.skipToAsResult(BindWatchsActivity.this, REQUEST_SWEEP_CODE);

            }
        });


    }


    public static final int REQUEST_SWEEP_CODE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_SWEEP_CODE == requestCode && RESULT_OK == resultCode) {
            parseSweepResult(data);
        }
    }


    private void parseSweepResult(Intent data) {
        if (null == data) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (null == bundle) {
            return;
        }
        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
            //解析成功
            String Imei = bundle.getString(CodeUtils.RESULT_STRING);
//            inputImei.setText(Imei);
//            if (isZXresultCorrect(Imei)) {
//                DeviceInfoInstance.getInstance().bindDevice(InitBindDeviceActivity.this,Imei);
//            } else {
//                showToast("IMEI码错误，请正确扫码！");
//            }


            WatchInstance.getInstance().preDeviceNumber = Imei;
            Intent intent = new Intent(BindWatchsActivity.this, BindMessageActivity.class);
            startActivity(intent);
            finish();

        }
    }

    /**
     * 检查IMEI是否符合规范
     *
     * @param result
     * @return
     */
    private boolean isZXresultCorrect(String result) {
        String regex = "([a-zA-Z0-9]{15})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(result);
        return m.matches();
    }
}
