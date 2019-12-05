package com.taisheng.now.bussiness.market.dingdan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.bean.post.DeleteOrderPostBean;
import com.taisheng.now.bussiness.bean.post.OrderListPostBean;
import com.taisheng.now.bussiness.bean.post.WexinZhifuPostBean;
import com.taisheng.now.bussiness.bean.result.market.OrderBean;
import com.taisheng.now.bussiness.bean.result.market.OrderGoodsBean;
import com.taisheng.now.bussiness.bean.result.market.OrderListResultBean;
import com.taisheng.now.bussiness.bean.result.xiadanshangpinBean;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.ShangPinxiangqingActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.test.WechatResultBean;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.WithScrolleViewListView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyDingdanFragment extends BaseFragment {

    public String assessmentType;


    //    MaterialDesignPtrFrameLayout ptr_refresh;
    TaishengListView lv_dingdan;

    DingdanAdapter madapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dingdan, container, false);


        initView(rootView);
//        initData();
        return rootView;
    }

    void initView(View rootView) {
        lv_dingdan = (TaishengListView) rootView.findViewById(R.id.lv_dingdan);


        madapter = new DingdanAdapter(getContext());
        lv_dingdan.setAdapter(madapter);
        lv_dingdan.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    void initData() {
        PAGE_NO=1;
        getDoctors();
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getDoctors() {
        OrderListPostBean bean = new OrderListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        switch (assessmentType) {
            case "1":
                bean.status = 1;
                break;
            case "2":
                bean.status = 3;
                break;
            case "3":
                bean.status = 4;
                break;
            case "4":
                bean.status = 5;
                break;

        }
        bean.pageNo=PAGE_NO;
        bean.pageSize=PAGE_SIZE;
        DialogUtil.showProgress(getActivity(), "");

        ApiUtils.getApiService().orderList(bean).enqueue(new TaiShengCallback<BaseBean<OrderListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<OrderListResultBean>> response, BaseBean<OrderListResultBean> message) {
//                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() >= 0) {
                            lv_dingdan.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_dingdan.setHasLoadMore(false);
                                lv_dingdan.setLoadAllViewText("暂时只有这么多商品");
                                lv_dingdan.setLoadAllFooterVisible(true);
                            } else {
                                lv_dingdan.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_dingdan.setHasLoadMore(false);
                            lv_dingdan.setLoadAllViewText("暂时只有这么多商品");
                            lv_dingdan.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<OrderListResultBean>> call, Throwable t) {
//                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });


    }

    class DingdanAdapter extends BaseAdapter {

        public Context mcontext;

        List<OrderBean> mData = new ArrayList<OrderBean>();

        public DingdanAdapter(Context context) {
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
            switch (assessmentType) {
                case "1":
// 声明内部类
                    Util util = null;
                    // 中间变量
                    final int flag = position;
                    if (convertView == null) {
                        util = new Util();
                        LayoutInflater inflater = LayoutInflater.from(mcontext);
                        convertView = inflater.inflate(R.layout.item_dingdandaifukuan, null);
                        util.ll_all=convertView.findViewById(R.id.ll_all);
                        util.tv_orderid = convertView.findViewById(R.id.tv_orderid);
                        util.list_goods = convertView.findViewById(R.id.list_goods);
                        util.tv_gouyou = convertView.findViewById(R.id.tv_gouyou);
                        util.tv_zongjia = convertView.findViewById(R.id.tv_zongjia);
                        util.tv_quxiaodingdan = convertView.findViewById(R.id.tv_quxiaodingdan);
                        util.tv_quzhifu = convertView.findViewById(R.id.tv_quzhifu);

                        convertView.setTag(util);
                    } else {
                        util = (Util) convertView.getTag();
                    }

                    OrderBean bean = mData.get(position);
                    util.ll_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingDaifukuanActivity.class);
                            intent.putExtra("orderId", bean.orderId);

                            startActivity(intent);
                        }
                    });
                    util.tv_orderid.setText(bean.orderId);

                    DingdanShangpinAdapter adapter = new DingdanShangpinAdapter(getActivity());
                    adapter.mData = bean.list;
                    util.list_goods.setAdapter(adapter);
                    util.list_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingDaifukuanActivity.class);
                            intent.putExtra("orderId", bean.orderId);

                            startActivity(intent);
                        }
                    });
                    util.tv_gouyou.setText("共有" + bean.goodsNumber + "件商品");
                    util.tv_zongjia.setText("¥" + bean.totalPrice);

                    util.tv_quxiaodingdan.setOnClickListener(new View.OnClickListener() {

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
                                            PAGE_NO = 1;
                                            getDoctors();
                                            break;
                                    }
                                }

                                @Override
                                public void onFail(Call<BaseBean> call, Throwable t) {

                                }
                            });
                        }
                    });

                    util.tv_quzhifu.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            WexinZhifuPostBean bean1 = new WexinZhifuPostBean();
                            bean1.orderId = bean.orderId;
                            DingdanInstance.getInstance().orderId=bean.orderId;
                            DingdanInstance.getInstance().gangzhifu_orderId=bean.orderId;
                            DingdanInstance.getInstance().gangzhifu_zongjia=bean.totalPrice.subtract(new BigDecimal(DingdanInstance.getInstance().youfei));
                            bean1.userId = UserInstance.getInstance().getUid();
                            bean1.token = UserInstance.getInstance().getToken();
                            ApiUtils.getApiService().weChatPay(bean1).enqueue(new TaiShengCallback<BaseBean<WechatResultBean>>() {
                                @Override
                                public void onSuccess(Response<BaseBean<WechatResultBean>> response, BaseBean<WechatResultBean> message) {
                                    switch (message.code) {
                                        case Constants.HTTP_SUCCESS:
                                            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Constants.WXAPPID, false);//填写自己的APPIDapi.registerApp("wxAPPID");//填写自己的APPID，注册本身
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


                    break;

                case "2":
                    // 声明内部类
                    Util util1 = null;
                    // 中间变量
                    final int flag1 = position;
                    if (convertView == null) {
                        util1 = new Util();
                        LayoutInflater inflater = LayoutInflater.from(mcontext);
                        convertView = inflater.inflate(R.layout.item_dingdandaifahuo, null);
                        util1.ll_all=convertView.findViewById(R.id.ll_all);
                        util1.tv_orderid = convertView.findViewById(R.id.tv_orderid);
                        util1.list_goods = convertView.findViewById(R.id.list_goods);
                        util1.tv_gouyou = convertView.findViewById(R.id.tv_gouyou);
                        util1.tv_zongjia = convertView.findViewById(R.id.tv_zongjia);
                        convertView.setTag(util1);
                    } else {
                        util1 = (Util) convertView.getTag();
                    }
                    OrderBean bean1 = mData.get(position);
                    util1.ll_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingDaifahuoActivity.class);
                            intent.putExtra("orderId", bean1.orderId);

                            startActivity(intent);
                        }
                    });

                    util1.tv_orderid.setText(bean1.orderId);

                    DingdanShangpinAdapter adapter1 = new DingdanShangpinAdapter(getActivity());
                    adapter1.mData = bean1.list;
                    util1.list_goods.setAdapter(adapter1);
                    util1.list_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingDaifahuoActivity.class);
                            intent.putExtra("orderId", bean1.orderId);

                            startActivity(intent);
                        }
                    });

                    util1.tv_gouyou.setText("共有" + bean1.goodsNumber + "件商品");
                    util1.tv_zongjia.setText("¥" + bean1.totalPrice);

                    break;
                case "3":
                    // 声明内部类
                    Util util2 = null;
                    // 中间变量
                    final int flag2 = position;
                    if (convertView == null) {
                        util2 = new Util();
                        LayoutInflater inflater = LayoutInflater.from(mcontext);
                        convertView = inflater.inflate(R.layout.item_dingdandaisouhuo, null);
                        util2.ll_all=convertView.findViewById(R.id.ll_all);
                        util2.tv_orderid = convertView.findViewById(R.id.tv_orderid);
                        util2.list_goods = convertView.findViewById(R.id.list_goods);
                        util2.tv_gouyou = convertView.findViewById(R.id.tv_gouyou);
                        util2.tv_zongjia = convertView.findViewById(R.id.tv_zongjia);
                        util2.tv_querensouhuo = convertView.findViewById(R.id.tv_querensouhuo);
                        convertView.setTag(util2);
                    } else {
                        util2 = (Util) convertView.getTag();
                    }


                    OrderBean bean2 = mData.get(position);

                    util2.ll_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingDaishouhuoActivity.class);
                            intent.putExtra("orderId", bean2.orderId);

                            startActivity(intent);
                        }
                    });
                    util2.tv_orderid.setText(bean2.orderId);

                    DingdanShangpinAdapter adapter2 = new DingdanShangpinAdapter(getActivity());
                    adapter2.mData = bean2.list;
                    util2.list_goods.setAdapter(adapter2);

                    util2.list_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingDaishouhuoActivity.class);
                            intent.putExtra("orderId", bean2.orderId);

                            startActivity(intent);
                        }
                    });


                    util2.tv_gouyou.setText("共有" + bean2.goodsNumber + "件商品");
                    util2.tv_zongjia.setText("¥" + bean2.totalPrice);
                    util2.tv_querensouhuo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            DeleteOrderPostBean deleteOrderPostBean = new DeleteOrderPostBean();
                            deleteOrderPostBean.userId = UserInstance.getInstance().getUid();
                            deleteOrderPostBean.token = UserInstance.getInstance().getToken();
                            deleteOrderPostBean.orderId = bean2.orderId;
                            ApiUtils.getApiService().confirmReceiveGoods(deleteOrderPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                                @Override
                                public void onSuccess(Response<BaseBean> response, BaseBean message) {
                                    switch (message.code) {
                                        case Constants.HTTP_SUCCESS:
                                            ToastUtil.showAtCenter("已确认");
                                            getDoctors();
                                            break;
                                    }
                                }

                                @Override
                                public void onFail(Call<BaseBean> call, Throwable t) {

                                }
                            });
                        }
                    });


                    break;
                case "4":
                    // 声明内部类
                    Util util3 = null;
                    // 中间变量
                    final int flag3 = position;
                    if (convertView == null) {
                        util3 = new Util();
                        LayoutInflater inflater = LayoutInflater.from(mcontext);
                        convertView = inflater.inflate(R.layout.item_dingdanyiwancheng, null);
                        util3.ll_all=convertView.findViewById(R.id.ll_all);
                        util3.tv_orderid = convertView.findViewById(R.id.tv_orderid);
                        util3.list_goods = convertView.findViewById(R.id.list_goods);
                        util3.tv_gouyou = convertView.findViewById(R.id.tv_gouyou);
                        util3.tv_zongjia = convertView.findViewById(R.id.tv_zongjia);
                        convertView.setTag(util3);
                    } else {
                        util3 = (Util) convertView.getTag();
                    }
                    OrderBean bean3 = mData.get(position);
                    util3.ll_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingYiwanchengActivity.class);
                            intent.putExtra("orderId", bean3.orderId);

                            startActivity(intent);
                        }
                    });

                    util3.tv_orderid.setText(bean3.orderId);

                    DingdanShangpinAdapter adapter3 = new DingdanShangpinAdapter(getActivity());
                    adapter3.mData = bean3.list;
                    util3.list_goods.setAdapter(adapter3);
                    util3.list_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DindanxiangqingYiwanchengActivity.class);
                            intent.putExtra("orderId", bean3.orderId);

                            startActivity(intent);
                        }
                    });

                    util3.tv_gouyou.setText("共有" + bean3.goodsNumber + "件商品");
                    util3.tv_zongjia.setText("¥" + bean3.totalPrice);

                    break;

            }

            return convertView;
        }


        class DingdanShangpinAdapter extends BaseAdapter {

            public Context mcontext;

            List<OrderGoodsBean> mData = new ArrayList<OrderGoodsBean>();

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
                DingdanShangpinAdapter.Util util = null;
                // 中间变量
                final int flag = position;
                if (convertView == null) {
                    util = new DingdanShangpinAdapter.Util();
                    LayoutInflater inflater = LayoutInflater.from(mcontext);
                    convertView = inflater.inflate(R.layout.item_dingdannshangpinn, null);
                    util.ll_all = convertView.findViewById(R.id.ll_all);
                    util.sdv_article = convertView.findViewById(R.id.sdv_article);
                    util.tv_name = convertView.findViewById(R.id.tv_name);
                    util.tv_jianjie=convertView.findViewById(R.id.tv_jianjie);
                    util.tv_counterprice = convertView.findViewById(R.id.tv_counterprice);
//                util.tv_retailprice = convertView.findViewById(R.id.tv_retailprice);
                    util.tv_number = convertView.findViewById(R.id.tv_number);

                    convertView.setTag(util);
                } else {
                    util = (DingdanShangpinAdapter.Util) convertView.getTag();
                }
                OrderGoodsBean bean = mData.get(position);
//                util.ll_all.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intent = new Intent(getActivity(), ShangPinxiangqingActivity.class);
//                        intent.putExtra("goodsid", bean.goodsId);
//
//                        startActivity(intent);
//                    }
//                });

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


        class Util {
            View ll_all;
            TextView tv_orderid;
            WithScrolleViewListView list_goods;
            TextView tv_gouyou;
            TextView tv_zongjia;
            View tv_quxiaodingdan;
            View tv_quzhifu;

            View tv_querensouhuo;
        }
    }
}
