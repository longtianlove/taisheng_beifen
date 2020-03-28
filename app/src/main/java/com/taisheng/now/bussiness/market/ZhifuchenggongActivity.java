package com.taisheng.now.bussiness.market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.WexinZhifuPostBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.market.dingdan.DindanxiangqingDaifahuoActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.test.WechatResultBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ZhifuchenggongActivity extends BaseHActivity {
    @BindView(R.id.btn_chakandingdan)
    TextView btnChakandingdan;
    @BindView(R.id.ll_chenggong)
    LinearLayout llChenggong;
    @BindView(R.id.btn_chongxinzhifu)
    TextView btnChongxinzhifu;
    @BindView(R.id.ll_shibai)
    LinearLayout llShibai;
    @BindView(R.id.btn_fanhuishouye)
    TextView btnFanhuishouye;

    @Override
    public void initView() {
        setContentView(R.layout.pay_resultjifenchenggong);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        llChenggong.setVisibility(View.VISIBLE);
        llShibai.setVisibility(View.GONE);
        btnChakandingdan.setVisibility(View.GONE);
//        api = WXAPIFactory.createWXAPI(this, "你的appid");
//        api.handleIntent(getIntent(), this);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvLeft.setVisibility(View.INVISIBLE);
        tvTitle.setText(getString(R.string.pay_succss));
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }
//
//    @Override
//    public void onReq(BaseReq req) {
//    }
//
//    @Override
//    public void onResp(BaseResp resp) {
//        Log.d("wechat", "onPayFinish, errCode = " + resp.errCode);
//
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
////            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            builder.setTitle("支付结果");
////            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
////            builder.show();
//            switch (resp.errCode) {
//                case 0:
//                    tv_title.setText("支付成功");
//                    ll_chenggong.setVisibility(View.VISIBLE);
//                    ll_shibai.setVisibility(View.GONE);
//                    break;
//                default:
//                    tv_title.setText("支付失败");
//                    ll_chenggong.setVisibility(View.GONE);
//                    ll_shibai.setVisibility(View.VISIBLE);
//                    break;
//
//            }
//        }
//    }



    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
    }

    @OnClick({R.id.btn_chakandingdan, R.id.btn_chongxinzhifu, R.id.btn_fanhuishouye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chakandingdan:
                Intent intent = new Intent(ZhifuchenggongActivity.this, DindanxiangqingDaifahuoActivity.class);
                intent.putExtra("orderId", DingdanInstance.getInstance().gangzhifu_orderId);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_chongxinzhifu:
                WexinZhifuPostBean bean1 = new WexinZhifuPostBean();
                bean1.orderId = DingdanInstance.getInstance().orderId;
                bean1.userId = UserInstance.getInstance().getUid();
                bean1.token = UserInstance.getInstance().getToken();
                ApiUtils.getApiService().weChatPay(bean1).enqueue(new TaiShengCallback<BaseBean<WechatResultBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<WechatResultBean>> response, BaseBean<WechatResultBean> message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                IWXAPI api = WXAPIFactory.createWXAPI(ZhifuchenggongActivity.this, Constants.WXAPPID, false);//填写自己的APPIDapi.registerApp("wxAPPID");//填写自己的APPID，注册本身
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
                break;
            case R.id.btn_fanhuishouye:
                Intent intent2 = new Intent(ZhifuchenggongActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                finish();
                break;
        }
    }
}

