package com.taisheng.now.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.WexinZhifuPostBean;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.ZhifuchenggongActivity;
import com.taisheng.now.bussiness.market.dingdan.DindanxiangqingDaifahuoActivity;
import com.taisheng.now.bussiness.market.dingdan.DingdanjiesuanActivity;
import com.taisheng.now.bussiness.me.MyDingdanActivity;
import com.taisheng.now.bussiness.user.LoginActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.test.WechatResultBean;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Response;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    public View btn_back;
    public TextView tv_title;
    public View ll_chenggong;
    public View btn_chakandingdan;
    public View ll_shibai;
    public View btn_chongxinzhifu;

    public View btn_fanhuishouye;

    public TextView tv_jifenhuoqu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
//        btn_back = findViewById(R.id.btn_back);
//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        tv_title = findViewById(R.id.tv_title);
        ll_chenggong = findViewById(R.id.ll_chenggong);
        btn_chakandingdan = findViewById(R.id.btn_chakandingdan);
        btn_chakandingdan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(WXPayEntryActivity.this, MyDingdanActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(WXPayEntryActivity.this, DindanxiangqingDaifahuoActivity.class);
                intent.putExtra("orderId", DingdanInstance.getInstance().gangzhifu_orderId);
                startActivity(intent);
                finish();
            }
        });
        ll_shibai = findViewById(R.id.ll_shibai);
        btn_chongxinzhifu = findViewById(R.id.btn_chongxinzhifu);
        btn_chongxinzhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WexinZhifuPostBean bean1 = new WexinZhifuPostBean();
                bean1.orderId = DingdanInstance.getInstance().orderId;
                bean1.userId = UserInstance.getInstance().getUid();
                bean1.token = UserInstance.getInstance().getToken();
                ApiUtils.getApiService().weChatPay(bean1).enqueue(new TaiShengCallback<BaseBean<WechatResultBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<WechatResultBean>> response, BaseBean<WechatResultBean> message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                IWXAPI api = WXAPIFactory.createWXAPI(WXPayEntryActivity.this, Constants.WXAPPID, false);//填写自己的APPIDapi.registerApp("wxAPPID");//填写自己的APPID，注册本身
                                PayReq req = new PayReq();//PayReq就是订单信息对象
                                req.appId = Constants.WXAPPID;//你的微信appid
                                req.partnerId = message.result.partnerid;//商户号
                                req.prepayId = message.result.prepayid;//预支付交易会话ID
                                req.nonceStr = message.result.noncestr;//随机字符串
                                req.timeStamp = message.result.timestamp + "";//时间戳
                                req.packageValue = "Sign=WXPay";//扩展字段,这里固定填写Sign=WXPay
                                req.sign = message.result.sign;//签名
                                api.sendReq(req);//将订单信息对象发送给微信服务器，即发送支付请求
                                break;
                        }


                    }

                    @Override
                    public void onFail(Call<BaseBean<WechatResultBean>> call, Throwable t) {

                    }
                });
            }
        });
        btn_fanhuishouye = findViewById(R.id.btn_fanhuishouye);
        btn_fanhuishouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(WXPayEntryActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
//                overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);

            }
        });

        api = WXAPIFactory.createWXAPI(this, Constants.WXAPPID);
        api.handleIntent(getIntent(), this);

        tv_jifenhuoqu = findViewById(R.id.tv_jifenhuoqu);
        tv_jifenhuoqu.setText("恭喜您此次支付获得" + DingdanInstance.getInstance().gangzhifu_zongjia.setScale(0, BigDecimal.ROUND_HALF_UP) + "积分");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("wechat", "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("支付结果");
//            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//            builder.show();
            switch (resp.errCode) {
                case 0:
                    tv_title.setText("支付成功");
                    ll_chenggong.setVisibility(View.VISIBLE);
                    ll_shibai.setVisibility(View.GONE);
                    break;
                default:
                    tv_title.setText("支付失败");
                    ll_chenggong.setVisibility(View.GONE);
                    ll_shibai.setVisibility(View.VISIBLE);
                    break;

            }
        }
    }


    String LOG_TAG = "TAG";

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity

        Log.i(LOG_TAG, "onBackPressed");
    }

}

