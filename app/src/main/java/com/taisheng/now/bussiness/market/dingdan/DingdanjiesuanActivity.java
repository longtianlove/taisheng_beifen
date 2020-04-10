package com.taisheng.now.bussiness.market.dingdan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.post.CreateOrderPostBean;
import com.taisheng.now.bussiness.bean.post.WexinZhifuPostBean;
import com.taisheng.now.bussiness.bean.result.CreateOrderResultBean;
import com.taisheng.now.bussiness.bean.result.PostageResultBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistResultBean;
import com.taisheng.now.bussiness.bean.result.xiadanshangpinBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.ShangPinxiangqingActivity;
import com.taisheng.now.bussiness.market.ZhifuchenggongActivity;
import com.taisheng.now.bussiness.market.dizhi.DizhiActivity;
import com.taisheng.now.bussiness.market.youhuijuan.MyYouhuijuanActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.test.WechatResultBean;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.WithListViewScrollView;
import com.taisheng.now.view.WithScrolleViewListView;
import com.taisheng.now.wxapi.WXPayUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by an on 2017/6/14.
 * 购物车界面
 */
public class DingdanjiesuanActivity extends BaseIvActivity {

    @BindView(R.id.tv_dizhiname)
    TextView tvDizhiname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_dizhi)
    LinearLayout llDizhi;
    @BindView(R.id.tv_youhuijuan)
    TextView tvYouhuijuan;
    @BindView(R.id.ll_youhuijuan)
    LinearLayout llYouhuijuan;
    @BindView(R.id.ll_youhuijuan_all)
    LinearLayout llYouhuijuanAll;
    @BindView(R.id.lv_jiesuan)
    WithScrolleViewListView lvJiesuan;
    @BindView(R.id.tv_jianyouhuijuan)
    TextView tvJianyouhuijuan;
    @BindView(R.id.ll_youhuijuan2)
    LinearLayout llYouhuijuan2;
    @BindView(R.id.view_youhuijuanlabel)
    View viewYouhuijuanlabel;
    @BindView(R.id.tv_youfei)
    TextView tvYoufei;
    @BindView(R.id.ll_youfei)
    LinearLayout llYoufei;
    @BindView(R.id.view_youfei_label)
    View viewYoufeiLabel;
    @BindView(R.id.et_beizhu)
    EditText etBeizhu;
    @BindView(R.id.scl_bag)
    WithListViewScrollView sclBag;
    @BindView(R.id.tv_zongjia)
    TextView tvZongjia;
    @BindView(R.id.btn_qujiesuan)
    TextView btnQujiesuan;
    private ArticleAdapter madapter;
    private String discount = "0";
    private String youfei = "0";

    @Override
    public void initView() {
        setContentView(R.layout.layout_diandanjiesuan);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

        if (DingdanInstance.getInstance().scoreGoods == 0) {
            llYouhuijuanAll.setVisibility(View.GONE);
        } else {
            llYouhuijuanAll.setVisibility(View.VISIBLE);
        }

        madapter = new ArticleAdapter(DingdanjiesuanActivity.this);

        if (DingdanInstance.getInstance().scoreGoods == 1) {
            madapter.mData = DingdanInstance.getInstance().putongshangpindingdanList;
            viewYoufeiLabel.setVisibility(View.VISIBLE);
            llYouhuijuan2.setVisibility(View.VISIBLE);
            viewYouhuijuanlabel.setVisibility(View.VISIBLE);
        } else {
            madapter.mData = DingdanInstance.getInstance().jifenshangpindingdanList;
            viewYoufeiLabel.setVisibility(View.GONE);
            llYouhuijuan2.setVisibility(View.GONE);
            viewYouhuijuanlabel.setVisibility(View.GONE);
        }
        lvJiesuan.setAdapter(madapter);
        tvJianyouhuijuan.setText("-" + getString(R.string.mony_code) + "0");
        tvYoufei.setText(getString(R.string.mony_code) + "0");
        if (DingdanInstance.getInstance().scoreGoods == 0) {
            llYoufei.setVisibility(View.GONE);
        } else {
            llYoufei.setVisibility(View.VISIBLE);
        }
        tvDizhiname.setText(DingdanInstance.getInstance().name);
        tvPhone.setText(DingdanInstance.getInstance().phone);
        tvAddress.setText(DingdanInstance.getInstance().address);
        initDatas();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.order_settlement));
    }

    @OnClick({R.id.ll_dizhi, R.id.ll_youhuijuan_all, R.id.btn_qujiesuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_dizhi:
                Intent intent = new Intent(DingdanjiesuanActivity.this, DizhiActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ll_youhuijuan_all:
                Intent intent2 = new Intent(DingdanjiesuanActivity.this, MyYouhuijuanActivity.class);
                startActivityForResult(intent2, 2);
                break;
            case R.id.btn_qujiesuan:
                if (!WXPayUtil.isWxAppInstalled(this)) {
                    return;
                }
                if (TextsUtils.isEmpty(TextsUtils.getTexts(tvYouhuijuan))) {
                    DingdanInstance.getInstance().couponId = "";
                }
                CreateOrderPostBean bean = new CreateOrderPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.addressId = DingdanInstance.getInstance().addressId;
                bean.couponId = DingdanInstance.getInstance().couponId;
                bean.flag = DingdanInstance.getInstance().flag;
                if (DingdanInstance.getInstance().scoreGoods == 1) {
                    bean.goodsList = DingdanInstance.getInstance().putongshangpindingdanList;
                } else {
                    bean.goodsList = DingdanInstance.getInstance().jifenshangpindingdanList;
                }
                bean.postFeeId = DingdanInstance.getInstance().postFeeId;
                bean.message = TextsUtils.getTexts(etBeizhu);
                ApiUtils.getApiService_hasdialog().createOrder(bean).enqueue(new TaiShengCallback<BaseBean<CreateOrderResultBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<CreateOrderResultBean>> response, BaseBean<CreateOrderResultBean> message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if ("1".equals(message.result.scoreType)) {
                                    //请求支付接口
//                                String orderId=message.message;
                                    WexinZhifuPostBean bean1 = new WexinZhifuPostBean();
                                    bean1.orderId = message.result.orderId;
                                    DingdanInstance.getInstance().orderId = message.result.orderId;
                                    DingdanInstance.getInstance().gangzhifu_orderId = message.result.orderId;

                                    BigDecimal temp = new BigDecimal(DingdanInstance.getInstance().zongjia);
//                                    BigDecimal temp1=temp.subtract(new BigDecimal(youfei));
                                    DingdanInstance.getInstance().gangzhifu_zongjia = temp.subtract(new BigDecimal(discount));

                                    bean1.userId = UserInstance.getInstance().getUid();
                                    bean1.token = UserInstance.getInstance().getToken();
                                    ApiUtils.getApiService_hasdialog().weChatPay(bean1).enqueue(new TaiShengCallback<BaseBean<WechatResultBean>>() {
                                        @Override
                                        public void onSuccess(Response<BaseBean<WechatResultBean>> response, BaseBean<WechatResultBean> message) {
                                            switch (message.code) {
                                                case Constants.HTTP_SUCCESS:
                                                    IWXAPI api = WXAPIFactory.createWXAPI(DingdanjiesuanActivity.this, Constants.WXAPPID, false);//填写自己的APPIDapi.registerApp("wxAPPID");//填写自己的APPID，注册本身
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
                                } else {
//                                    DingdanInstance.getInstance().orderId = message.result.orderId;
                                    Intent intent = new Intent(DingdanjiesuanActivity.this, ZhifuchenggongActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                                break;

                            case 6000:
                                ToastUtil.showAtCenter(message.message);
                                break;
                            default:
                                ToastUtil.showAtCenter(message.message);
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean<CreateOrderResultBean>> call, Throwable t) {

                    }
                });
                break;
        }
    }


    //初始化数据
    protected void initDatas() {
//获取地址信息
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = 1;
        bean.pageSize = 10;

        ApiUtils.getApiService_hasdialog().addressList(bean).enqueue(new TaiShengCallback<BaseBean<DizhilistResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DizhilistResultBean>> response, BaseBean<DizhilistResultBean> message) {

                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            DizhilistBean bean = new DizhilistBean();
                            int i = 0;
                            for (DizhilistBean tempbean : message.result.records) {
                                i++;
                                if (tempbean.defaultAddress == 1) {
                                    bean = tempbean;
                                    break;
                                }

                                if (i == message.result.records.size()) {
                                    bean = tempbean;
                                }

                            }
                            DingdanInstance.getInstance().addressId = bean.id;
                            DingdanInstance.getInstance().name = bean.name;
                            DingdanInstance.getInstance().phone = bean.phone;
                            DingdanInstance.getInstance().address = bean.province + bean.city + bean.county + bean.addressDetail;
                            tvDizhiname.setText(bean.name);
                            tvPhone.setText(bean.phone);
                            tvAddress.setText(bean.province + bean.city + bean.county + bean.addressDetail);

                        } else {

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DizhilistResultBean>> call, Throwable t) {

                DialogUtil.closeProgress();
            }
        });


        BasePostBean basePostBean = new BasePostBean();
        basePostBean.userId = UserInstance.getInstance().getUid();
        basePostBean.token = UserInstance.getInstance().getToken();
        if (DingdanInstance.getInstance().scoreGoods == 1) {
            //获取邮费
            ApiUtils.getApiService_hasdialog().getPostage(basePostBean).enqueue(new TaiShengCallback<BaseBean<PostageResultBean>>() {
                @Override
                public void onSuccess(Response<BaseBean<PostageResultBean>> response, BaseBean<PostageResultBean> message) {
                    switch (message.code) {
                        case Constants.HTTP_SUCCESS:
                            youfei = message.result.money + "";
                            DingdanInstance.getInstance().youfei = message.result.money;
                            DingdanInstance.getInstance().postFeeId = message.result.id;
                            tvYoufei.setText(getString(R.string.mony_code) + youfei);
//                            tv_zongjia.setText(getString(R.string.mony_code) + (Double.parseDouble(DingdanInstance.getInstance().zongjia) - Double.parseDouble(discount) + Double.parseDouble(youfei)));
                            BigDecimal temp = new BigDecimal(DingdanInstance.getInstance().zongjia);
                            BigDecimal temp1 = temp.add(new BigDecimal(youfei));
                            BigDecimal temp2 = temp1.subtract(new BigDecimal(discount));

                            tvZongjia.setText(getString(R.string.mony_code) + temp2);

                            break;
                    }
                }

                @Override
                public void onFail(Call<BaseBean<PostageResultBean>> call, Throwable t) {
                }
            });
        } else {
            BigDecimal temp = new BigDecimal(DingdanInstance.getInstance().zongjia);
            tvZongjia.setText(temp.multiply(new BigDecimal(100)) + "");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                String address = data.getStringExtra("address");
                tvDizhiname.setText(name);
                tvPhone.setText(phone);
                tvAddress.setText(address);
                break;
            case 2:
                discount = DingdanInstance.getInstance().tv_discount;
                tvYouhuijuan.setText(getString(R.string.mony_code) + discount);
                tvJianyouhuijuan.setText("-" + getString(R.string.mony_code) + discount);
//                tv_zongjia.setText(getString(R.string.mony_code) + (Double.parseDouble(DingdanInstance.getInstance().zongjia) - Double.parseDouble(discount) + Double.parseDouble(youfei)));
                BigDecimal temp = new BigDecimal(DingdanInstance.getInstance().zongjia);
                BigDecimal temp1 = temp.add(new BigDecimal(youfei));
                BigDecimal temp2 = temp1.subtract(new BigDecimal(discount));
                tvZongjia.setText(getString(R.string.mony_code) + temp2);
                break;
        }
    }


    class ArticleAdapter extends BaseAdapter {

        public Context mcontext;

        List<xiadanshangpinBean> mData = new ArrayList<xiadanshangpinBean>();

        public ArticleAdapter(Context context) {
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
                util.tv_counterprice = convertView.findViewById(R.id.tv_counterprice);
//                util.tv_retailprice = convertView.findViewById(R.id.tv_retailprice);
                util.tv_number = convertView.findViewById(R.id.tv_number);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            xiadanshangpinBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入商品详情

                    Intent intent = new Intent(DingdanjiesuanActivity.this, ShangPinxiangqingActivity.class);
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
            if (DingdanInstance.getInstance().scoreGoods == 1) {
                util.tv_counterprice.setText(mcontext.getString(R.string.mony_code) + bean.counterPrice);
            } else {
                BigDecimal temp = new BigDecimal(bean.counterPrice);
                util.tv_counterprice.setText(temp.multiply(new BigDecimal(100)) + "");
            }
//            util.tv_retailprice.setText(bean.retailPrice + "");
//            util.tv_retailprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            util.tv_number.setText("x " + bean.number);
            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_article;
            TextView tv_name;
            TextView tv_counterprice;
            TextView tv_number;
            TextView tv_retailprice;

        }
    }
}
