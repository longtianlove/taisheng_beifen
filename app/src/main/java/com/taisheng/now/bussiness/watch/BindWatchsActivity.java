package com.taisheng.now.bussiness.watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.ZXing.ZXingActivity;
import com.taisheng.now.base.BaseIvActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2019/6/28.
 */

public class BindWatchsActivity extends BaseIvActivity {
    @BindView(R.id.tv_adddevice)
    TextView tvAdddevice;
    public static final int REQUEST_SWEEP_CODE = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_bindwatchs);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.device_binding));
    }
    @OnClick(R.id.tv_adddevice)
    public void onViewClicked() {
        ZXingActivity.skipToAsResult(BindWatchsActivity.this, REQUEST_SWEEP_CODE);
    }


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
