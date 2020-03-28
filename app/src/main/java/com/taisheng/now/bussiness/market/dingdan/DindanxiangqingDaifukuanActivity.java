package com.taisheng.now.bussiness.market.dingdan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.DeleteOrderPostBean;
import com.taisheng.now.bussiness.bean.post.OrderxiangqingPostBean;
import com.taisheng.now.bussiness.bean.post.WexinZhifuPostBean;
import com.taisheng.now.bussiness.bean.result.market.DingdanxiangqingGoodBean;
import com.taisheng.now.bussiness.bean.result.market.DingdanxiangqingResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.ShangPinxiangqingActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.test.WechatResultBean;
import com.taisheng.now.view.WithScrolleViewListView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class DindanxiangqingDaifukuanActivity extends BaseHActivity {

    @BindView(R.id.tv_dizhiname)
    TextView tvDizhiname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_dizhi)
    LinearLayout llDizhi;
    @BindView(R.id.tv_orderno)
    TextView tvOrderno;
    @BindView(R.id.list_goods)
    WithScrolleViewListView listGoods;
    @BindView(R.id.tv_gouyou)
    TextView tvGouyou;
    @BindView(R.id.tv_zongjia)
    TextView tvZongjia;
    @BindView(R.id.tv_quxiaodingdan)
    TextView tvQuxiaodingdan;
    @BindView(R.id.tv_quzhifu)
    TextView tvQuzhifu;
    @BindView(R.id.tv_beizhu)
    TextView tvBeizhu;
    @BindView(R.id.tv_jiangli)
    TextView tvJiangli;
    @BindView(R.id.tv_chuangjianshijian)
    TextView tvChuangjianshijian;
    private String orderId;


    @Override
    public void initView() {
        setContentView(R.layout.activity_dingdanxiangqing_daifukuan);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        initDatas();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.order_details));
    }


    private void initDatas() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        OrderxiangqingPostBean bean = new OrderxiangqingPostBean();
        bean.orderId = orderId;
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();

        ApiUtils.getApiService().orderDetail(bean).enqueue(new TaiShengCallback<BaseBean<DingdanxiangqingResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DingdanxiangqingResultBean>> response, BaseBean<DingdanxiangqingResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        tvDizhiname.setText(message.result.consignee);
                        tvPhone.setText(message.result.phone);
                        tvAddress.setText(message.result.address);
                        tvOrderno.setText(message.result.orderId);

                        DingdanShangpinAdapter adapter3 = new DingdanShangpinAdapter(DindanxiangqingDaifukuanActivity.this);
                        adapter3.mData = message.result.list;
                        listGoods.setAdapter(adapter3);


                        tvGouyou.setText("共有" + message.result.goodsNumber + "件商品");
                        tvZongjia.setText("¥" + message.result.totalPrice);
                        tvQuxiaodingdan.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                DeleteOrderPostBean deleteOrderPostBean = new DeleteOrderPostBean();
                                deleteOrderPostBean.userId = UserInstance.getInstance().getUid();
                                deleteOrderPostBean.token = UserInstance.getInstance().getToken();
                                deleteOrderPostBean.orderId = bean.orderId;
                                ApiUtils.getApiService().deleteOrder(deleteOrderPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                                    @Override
                                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                                        switch (message.code) {
                                            case Constants.HTTP_SUCCESS:
//                                                getDoctors();
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

                        tvQuzhifu.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                WexinZhifuPostBean bean1 = new WexinZhifuPostBean();
                                bean1.orderId = bean.orderId;
                                DingdanInstance.getInstance().orderId = bean.orderId;

                                bean1.userId = UserInstance.getInstance().getUid();
                                bean1.token = UserInstance.getInstance().getToken();
                                ApiUtils.getApiService().weChatPay(bean1).enqueue(new TaiShengCallback<BaseBean<WechatResultBean>>() {
                                    @Override
                                    public void onSuccess(Response<BaseBean<WechatResultBean>> response, BaseBean<WechatResultBean> message) {
                                        switch (message.code) {
                                            case Constants.HTTP_SUCCESS:
                                                IWXAPI api = WXAPIFactory.createWXAPI(DindanxiangqingDaifukuanActivity.this, Constants.WXAPPID, false);//填写自己的APPIDapi.registerApp("wxAPPID");//填写自己的APPID，注册本身
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


                        if (TextUtils.isEmpty(message.result.message)) {
                            tvBeizhu.setVisibility(View.GONE);

                        } else {
                            tvBeizhu.setVisibility(View.VISIBLE);
                            tvBeizhu.setText("订单备注：" + message.result.message);

                        }
//                        if(message.result.scoreGoods==0){
                        tvJiangli.setVisibility(View.GONE);
//                        }else{
//                            tv_jiangli.setVisibility(View.VISIBLE);
//                            tv_jiangli.setText("奖励积分：" + message.result.totalPrice .multiply(new BigDecimal(100)));
//
//                        }
                        tvChuangjianshijian.setText("创建时间：" + message.result.createTime);
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DingdanxiangqingResultBean>> call, Throwable t) {

            }
        });

    }


    class DingdanShangpinAdapter extends BaseAdapter {

        public Context mcontext;

        List<DingdanxiangqingGoodBean> mData = new ArrayList<DingdanxiangqingGoodBean>();

        public DingdanShangpinAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 声明内部类
            Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_dingdannshangpinn, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_article = convertView.findViewById(R.id.sdv_article);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_jianjie = convertView.findViewById(R.id.tv_jianjie);
                util.tv_counterprice = convertView.findViewById(R.id.tv_counterprice);
//                util.tv_retailprice = convertView.findViewById(R.id.tv_retailprice);
                util.tv_number = convertView.findViewById(R.id.tv_number);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            DingdanxiangqingGoodBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(DindanxiangqingDaifukuanActivity.this, ShangPinxiangqingActivity.class);
                    intent.putExtra("goodsid", bean.goodsId);

                    startActivity(intent);
                }
            });

            String temp_url = bean.picUrl;
            if (temp_url == null || "".equals(temp_url)) {
                util.sdv_article.setBackgroundResource(R.drawable.article_default);

            } else {
                Uri uri = Uri.parse(temp_url);
                util.sdv_article.setImageURI(uri);
            }
            util.tv_name.setText(bean.name);
            util.tv_jianjie.setText(bean.brief);
            util.tv_counterprice.setText(bean.price + "");
//            util.tv_retailprice.setText(bean.retailPrice + "");
//            util.tv_retailprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            util.tv_number.setText("x " + bean.number);
            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_article;
            TextView tv_name;
            TextView tv_jianjie;
            TextView tv_counterprice;
            TextView tv_number;

        }
    }

}
