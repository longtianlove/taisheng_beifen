package com.taisheng.now.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.SplashActivity;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.post.WexinZhifuPostBean;
import com.taisheng.now.bussiness.market.dingdan.DingdanjiesuanActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiService;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by dragon on 2019/6/28.
 */

public class TestActivity extends BaseActivity {
    View iv_back;
    EditText et_qian;
    View tv_gozhifu;

    View tv_goshouye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        et_qian = findViewById(R.id.et_qian);
        tv_gozhifu = findViewById(R.id.tv_gozhifu);
        tv_gozhifu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qian = et_qian.getText().toString();


                BasePostBean bean = new BasePostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();


//请求支付接口
//                                String orderId=message.message;
                WexinZhifuPostBean bean1 = new WexinZhifuPostBean();
//                bean1.orderId = "a85aeda490de49d2993b1f230df44f7f";
                bean1.userId = UserInstance.getInstance().getUid();
                bean1.token = UserInstance.getInstance().getToken();
//                ApiUtils.getApiService().weChatPay(bean1).enqueue(new TaiShengCallback<WechatResultBean>() {
//                    @Override
//                    public void onSuccess(Response<WechatResultBean> response, WechatResultBean message) {
//
//                                IWXAPI api = WXAPIFactory.createWXAPI(TestActivity.this, Constants.WXAPPID, false);//填写自己的APPIDapi.registerApp("wxAPPID");//填写自己的APPID，注册本身
//                                PayReq req = new PayReq();//PayReq就是订单信息对象
//                                req.appId = Constants.WXAPPID;//你的微信appid
//                                req.partnerId = message.partnerid;//商户号
//                                req.prepayId = message.prepayid;//预支付交易会话ID
//                                req.nonceStr = message.noncestr;//随机字符串
//                                req.timeStamp = message.timestamp + "";//时间戳
//                                req.packageValue = "Sign=WXPay";//扩展字段,这里固定填写Sign=WXPay
//                                req.sign = message.sign;//签名
//                                api.sendReq(req);//将订单信息对象发送给微信服务器，即发送支付请求
//
//
//
//
//                    }
//
//                    @Override
//                    public void onFail(Call<WechatResultBean> call, Throwable t) {
//
//                    }
//                });


            }
        });


        tv_goshouye=findViewById(R.id.tv_goshouye);
        tv_goshouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

    }


}
