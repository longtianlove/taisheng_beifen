package com.taisheng.now.bussiness.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.post.LingqukajuanPostBean;
import com.taisheng.now.bussiness.bean.result.CainixihuanResultBean;
import com.taisheng.now.bussiness.bean.result.JifenzhuanquBean;
import com.taisheng.now.bussiness.bean.result.MallBannerBean;
import com.taisheng.now.bussiness.bean.result.MallBannerResultBanner;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanBean;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanResultBanner;
import com.taisheng.now.bussiness.bean.result.RemenshangpinBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.market.gouwuche.GouwucheActivity;
import com.taisheng.now.bussiness.market.youhuijuan.MoreYouhuijuanActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.shipin.OpenGLBaseModule.GLThread;
import com.taisheng.now.util.GlideImageLoader;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.taisheng.now.view.WithListViewScrollView;
import com.taisheng.now.view.WithScrolleViewListView;
import com.taisheng.now.view.banner.BannerViewPager;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.TextsUtils;
import com.th.j.commonlibrary.wight.RoundImgView;
import com.youth.banner.Banner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

public class MarketFragment extends BaseFragment {

    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.bannerContaner)
    Banner bannerContaner;
    @BindView(R.id.ll_healthfile)
    LinearLayout llHealthfile;
    @BindView(R.id.ll_shipinzibu)
    LinearLayout llShipinzibu;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.ll_zhongyaocai)
    LinearLayout llZhongyaocai;
    @BindView(R.id.ll_jifenduihuan)
    LinearLayout llJifenduihuan;
    @BindView(R.id.lv_youhuijuans)
    WithScrolleViewListView lvYouhuijuans;
    @BindView(R.id.tv_moreyouhuijuan)
    TextView tvMoreyouhuijuan;
    @BindView(R.id.rv_hot_goods)
    RecyclerView rvHotGoods;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.lv_jifenduihuan)
    WithScrolleViewListView lvJifenduihuan;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.scl_bag)
    WithListViewScrollView sclBag;
    @BindView(R.id.ptr_refresh)
    MaterialDesignPtrFrameLayout ptrRefresh;

    RecyclerView.LayoutManager mLayoutManager;
    HotGoodsAdapter hotGoodsAdapter;
    ArticleAdapter madapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_market, container, false);
        ButterKnife.bind(this, rootView);

        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        MainActivity activity = (MainActivity) getActivity();
        activity.ivRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GouwucheActivity.class);
                startActivity(intent);
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchkey = etSearch.getText().toString();
                if (TextUtils.isEmpty(searchkey)) {
                    return;
                }
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("searchkey", searchkey);
                startActivity(intent);
            }
        });


        llHealthfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FenleiMarketActivity.selectTab = 0;
                if (FenleiMarketActivity.tlTab != null) {
                    (FenleiMarketActivity.tlTab.getTabAt(0)).select();
                }
                Intent intent = new Intent(getActivity(), FenleiMarketActivity.class);
                startActivity(intent);
            }
        });
        llShipinzibu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FenleiMarketActivity.selectTab = 1;
                if (FenleiMarketActivity.tlTab != null) {
                    (FenleiMarketActivity.tlTab.getTabAt(1)).select();
                }
                Intent intent = new Intent(getActivity(), FenleiMarketActivity.class);
                startActivity(intent);
            }
        });
        llZhongyaocai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FenleiMarketActivity.selectTab = 2;
                if (FenleiMarketActivity.tlTab != null) {
                    (FenleiMarketActivity.tlTab.getTabAt(2)).select();
                }
                Intent intent = new Intent(getActivity(), FenleiMarketActivity.class);
                startActivity(intent);
            }
        });
        llJifenduihuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FenleiMarketActivity.selectTab = 3;
                if (FenleiMarketActivity.tlTab != null) {
                    (FenleiMarketActivity.tlTab.getTabAt(3)).select();
                }
                Intent intent = new Intent(getActivity(), FenleiMarketActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 下拉刷新
         */
        ptrRefresh.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }
        });
//

        bannerContaner = rootView.findViewById(R.id.bannerContaner);


        tvMoreyouhuijuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreYouhuijuanActivity.class);
                startActivity(intent);
            }
        });

        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        hotGoodsAdapter = new HotGoodsAdapter(getActivity());
        rvHotGoods.setLayoutManager(mLayoutManager);
//        rvHotGoods.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        rvHotGoods.setAdapter(hotGoodsAdapter);


        madapter = new ArticleAdapter(mActivity);
        lvJifenduihuan.setAdapter(madapter);


        youhuiquanAdapter = new YouhuiquanAdapter(getActivity());
        lvYouhuijuans.setAdapter(youhuiquanAdapter);
    }

    void initData() {
        getBanner();
        getYouhuiquan();
        getHotGoodsJifenduihuan();
    }


    public void getBanner() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = 1;
        bean.pageSize = 5;
        ApiUtils.getApiService().banner(bean).enqueue(new TaiShengCallback<BaseBean<MallBannerResultBanner>>() {
            @Override
            public void onSuccess(Response<BaseBean<MallBannerResultBanner>> response, BaseBean<MallBannerResultBanner> message) {
                ptrRefresh.refreshComplete();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        List<String> pictureUrls = new ArrayList<>();
                        if (message.result.records != null && !message.result.records.isEmpty()) {

                            for (MallBannerBean bean :
                                    message.result.records) {
                                pictureUrls.add(bean.url);
                            }
                            bannerContaner.setImageLoader(new GlideImageLoader());
                            bannerContaner.setImages(pictureUrls);
                            bannerContaner.start();

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<MallBannerResultBanner>> call, Throwable t) {
                ptrRefresh.refreshComplete();
            }
        });
    }


    YouhuiquanAdapter youhuiquanAdapter;

    public void getYouhuiquan() {

        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = 1;
        bean.pageSize = 10;
        ApiUtils.getApiService().coupon(bean).enqueue(new TaiShengCallback<BaseBean<MallYouhuiquanResultBanner>>() {
            @Override
            public void onSuccess(Response<BaseBean<MallYouhuiquanResultBanner>> response, BaseBean<MallYouhuiquanResultBanner> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            youhuiquanAdapter.mData = message.result.records;
                            youhuiquanAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<MallYouhuiquanResultBanner>> call, Throwable t) {
                LogUtilH.e(t.toString() + "---------------1------1------------");
            }
        });
    }


    class YouhuiquanAdapter extends BaseAdapter {

        public Context mcontext;

        List<MallYouhuiquanBean> mData = new ArrayList<MallYouhuiquanBean>();

        public YouhuiquanAdapter(Context context) {
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
                convertView = inflater.inflate(R.layout.item_youhuijuan, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_discount = convertView.findViewById(R.id.tv_discount);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_tag = convertView.findViewById(R.id.tv_tag);
                util.tv_usedate = convertView.findViewById(R.id.tv_usedate);
                util.tv_lingqu = convertView.findViewById(R.id.tv_lingqu);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            MallYouhuiquanBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            if (!TextsUtils.isEmpty(bean.discount + "")) {
                util.tv_discount.setText(mcontext.getString(R.string.mony_code) + bean.discount + "");
            } else {
                util.tv_discount.setText(mcontext.getString(R.string.mony_code) + "0.00");
            }
            util.tv_name.setText(bean.name);
            util.tv_tag.setText(bean.tag);
            util.tv_usedate.setText(bean.useDate);
            util.tv_lingqu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LingqukajuanPostBean bean1 = new LingqukajuanPostBean();
                    bean1.userId = UserInstance.getInstance().getUid();
                    bean1.token = UserInstance.getInstance().getToken();
                    bean1.id = bean.id;
                    ApiUtils.getApiService().getCoupon(bean1).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    getYouhuiquan();
                                    ToastUtil.showAtCenter(message.message);
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
                }
            });

            return convertView;
        }


        class Util {
            View ll_all;

            TextView tv_discount;
            TextView tv_name;
            TextView tv_tag;
            TextView tv_usedate;
            View tv_lingqu;
        }
    }

    public void getHotGoodsJifenduihuan() {
        BasePostBean bean = new BasePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        ApiUtils.getApiService().cainixihuan(bean).enqueue(new TaiShengCallback<BaseBean<CainixihuanResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<CainixihuanResultBean>> response, BaseBean<CainixihuanResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.hotGoodsList != null && !message.result.hotGoodsList.isEmpty()) {
                            hotGoodsAdapter.mData = message.result.hotGoodsList;

                            hotGoodsAdapter.notifyDataSetChanged();
                        }
                        if (message.result.scoreList != null && !message.result.scoreList.isEmpty()) {
                            madapter.mData = message.result.scoreList;
                            madapter.notifyDataSetChanged();
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<CainixihuanResultBean>> call, Throwable t) {
            }
        });
    }


    public class HotGoodsAdapter extends RecyclerView.Adapter {

        private Context mContext;
        public List<RemenshangpinBean> mData; //定义数据源

        //定义构造方法，默认传入上下文和数据源
        public HotGoodsAdapter(Context context) {
            this.mContext = context;

        }

        @Override  //将ItemView渲染进来，创建ViewHolder
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_hot_goods, null);
            return new MyViewHolder(view);
        }

        @Override  //将数据源的数据绑定到相应控件上
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder holder2 = (MyViewHolder) holder;
            RemenshangpinBean hotGoodsBean = mData.get(position);
            if (hotGoodsBean != null) {
                Glide.with(mContext)
                        .load(hotGoodsBean.picUrl)
                        .apply(new RequestOptions().error(R.drawable.article_default).placeholder(R.drawable.article_default))
                        .into(holder2.sdv_header);
                holder2.tv_goods_name.setText(hotGoodsBean.name);
                if (!TextsUtils.isEmpty(hotGoodsBean.retailPrice + "")) {
                    holder2.tv_goods_jiage.setText(mContext.getString(R.string.mony_code) + hotGoodsBean.retailPrice + "");
                } else {
                    holder2.tv_goods_jiage.setText(mContext.getString(R.string.mony_code) + "0.00");
                }
                holder2.ll_all.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mActivity, ShangPinxiangqingActivity.class);
                        intent.putExtra("goodsid", hotGoodsBean.id);
                        startActivity(intent);
                    }
                });
            }

            int screenWidth = (Uiutils.getScreenWidth(mContext) / 2) - Uiutils.dp2px(mContext, 6f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth);
            holder2.sdv_header.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (position % 2 == 0) {
                params.leftMargin = Uiutils.dp2px(mContext, 6f);
                params.rightMargin = Uiutils.dp2px(mContext, 3f);
            } else {
                params.leftMargin = Uiutils.dp2px(mContext, 3f);
                params.rightMargin = Uiutils.dp2px(mContext, 6f);
            }
            params.bottomMargin = Uiutils.dp2px(mContext, 6f);
            holder2.ll_all.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        //定义自己的ViewHolder，将View的控件引用在成员变量上
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public View ll_all;
            public RoundImgView sdv_header;
            public TextView tv_goods_name;
            public TextView tv_goods_jiage;
            public TextView tv_sales;
            public View iv_jifennduihuan;


            public MyViewHolder(View itemView) {
                super(itemView);
                ll_all = itemView.findViewById(R.id.ll_all);
                sdv_header = itemView.findViewById(R.id.sdv_header);
                tv_goods_name = itemView.findViewById(R.id.tv_goods_name);
                tv_goods_jiage = itemView.findViewById(R.id.tv_goods_jiage);
                iv_jifennduihuan = itemView.findViewById(R.id.iv_jifennduihuan);
                tv_sales = itemView.findViewById(R.id.tv_sales);
            }
        }
    }


    class ArticleAdapter extends BaseAdapter {

        public Context mcontext;

        List<JifenzhuanquBean> mData = new ArrayList<JifenzhuanquBean>();

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
                convertView = inflater.inflate(R.layout.item_jifenduihuanshouye, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_article = convertView.findViewById(R.id.sdv_article);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_jianjie = convertView.findViewById(R.id.tv_jianjie);
                util.tv_counterprice = convertView.findViewById(R.id.tv_counterprice);
//                util.tv_retailprice = convertView.findViewById(R.id.tv_retailprice);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            JifenzhuanquBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入商品详情

                    Intent intent = new Intent(mActivity, ShangPinxiangqingActivity.class);
                    intent.putExtra("goodsid", bean.id);

                    startActivity(intent);
                }
            });
            Glide.with(mcontext)
                    .load(bean.picUrl)
                    .apply(new RequestOptions().error(R.drawable.article_default).placeholder(R.drawable.article_default))
                    .into(util.sdv_article);

            util.tv_name.setText(bean.name);
            util.tv_jianjie.setText(bean.brief);
            if (!TextsUtils.isEmpty(bean.retailPrice+"")){
                util.tv_counterprice.setText(bean.retailPrice.multiply(new BigDecimal(100)) + "");
            }else {
                util.tv_counterprice.setText("0");
            }
//            util.tv_retailprice.setText(bean.counterPrice .multiply(new BigDecimal(100))+ "");

//            util.tv_retailprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            return convertView;
        }


        class Util {
            View ll_all;
            RoundImgView sdv_article;
            TextView tv_name;
            TextView tv_jianjie;
            TextView tv_counterprice;
//            TextView tv_retailprice;

        }
    }
}
