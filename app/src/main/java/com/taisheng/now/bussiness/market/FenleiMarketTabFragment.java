package com.taisheng.now.bussiness.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.article.ArticleContentActivity;
import com.taisheng.now.bussiness.bean.post.ArticlePostBean;
import com.taisheng.now.bussiness.bean.post.ArticleWithDoctorPostBean;
import com.taisheng.now.bussiness.bean.post.MarketTypePostBean;
import com.taisheng.now.bussiness.bean.result.ArticleBean;
import com.taisheng.now.bussiness.bean.result.ArticleResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorsResultBean;
import com.taisheng.now.bussiness.bean.result.market.ShangPinResultBeann;
import com.taisheng.now.bussiness.bean.result.market.ShangpinBean;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/1.
 */

public class FenleiMarketTabFragment extends BaseFragment {
    public String typeName;
    public TaishengListView lv_articles;
    MarketTypePostBean bean;
    ArticleAdapter madapter;


    MaterialDesignPtrFrameLayout ptr_refresh;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_market_tab, container, false);
        initView(rootView);
        initData();
        return rootView;
    }

    void initView(View rootView) {

        ptr_refresh = (MaterialDesignPtrFrameLayout) rootView.findViewById(R.id.ptr_refresh);
        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                initData();

            }
        });




        lv_articles = (TaishengListView) rootView.findViewById(R.id.lv_articles);
        madapter = new ArticleAdapter(mActivity);
        lv_articles.setAdapter(madapter);
        lv_articles.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getArticles();
            }
        });
    }

    void initData() {
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        bean = new MarketTypePostBean();
        getArticles();

    }





    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getArticles() {
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        switch (typeName) {
            case "营养保健":
                bean.type = "ad63e761d074716cd76202f78dacec56";
                break;
            case "食品滋补":
                bean.type = "064387677d5ffb88f2b6ef4810e94af3";
                break;
            case "中药材":
                bean.type = "aae5e9deb8fdec69b6e4982b9ee27214";
                break;
            case "积分兑换":
                //下面还有一个
                bean.type = "ad63e761d074716cd76202f78dacec55";
                break;

        }
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        DialogUtil.showProgress(mActivity, "");
        ApiUtils.getApiService().goodsTtype(bean).enqueue(new TaiShengCallback<BaseBean<ShangPinResultBeann>>() {
            @Override
            public void onSuccess(Response<BaseBean<ShangPinResultBeann>> response, BaseBean<ShangPinResultBeann> message) {
                DialogUtil.closeProgress();
                ptr_refresh.refreshComplete();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:

                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_articles.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_articles.setHasLoadMore(false);
                                lv_articles.setLoadAllViewText("暂时只有这么多商品");
                                lv_articles.setLoadAllFooterVisible(true);
                            } else {
                                lv_articles.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_articles.setHasLoadMore(false);
                            lv_articles.setLoadAllViewText("暂时只有这么多商品");
                            lv_articles.setLoadAllFooterVisible(true);
                        }

                        break;

                }

            }

            @Override
            public void onFail(Call<BaseBean<ShangPinResultBeann>> call, Throwable t) {
                DialogUtil.closeProgress();
                ptr_refresh.refreshComplete();
            }
        });
    }



    class ArticleAdapter extends BaseAdapter {

        public Context mcontext;

        List<ShangpinBean> mData = new ArrayList<ShangpinBean>();

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
                convertView = inflater.inflate(R.layout.item_jifenduihuan, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_article = convertView.findViewById(R.id.sdv_article);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_jianjie=convertView.findViewById(R.id.tv_jianjie);
                util.tv_counterprice = convertView.findViewById(R.id.tv_counterprice);
                util.tv_jifenlabel=convertView.findViewById(R.id.tv_jifenlabel);
                util.tv_retailprice = convertView.findViewById(R.id.tv_retailprice);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            ShangpinBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入商品详情

                    Intent intent = new Intent(getActivity(), ShangPinxiangqingActivity.class);
                    intent.putExtra("goodsid", bean.id);

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
            //上面还有一个
            if("积分兑换".equals(typeName)){
                util.tv_counterprice.setText(bean.retailPrice.multiply(new BigDecimal(100)) + "");
                util.tv_jifenlabel.setVisibility(View.VISIBLE);
                util.tv_retailprice.setVisibility(View.GONE);
//                util.tv_retailprice.setText(bean.counterPrice .multiply(new BigDecimal(100))+ "");
            }else{
                util.tv_jifenlabel.setVisibility(View.GONE);
                util.tv_retailprice.setVisibility(View.VISIBLE);
                util.tv_counterprice.setText("¥"+bean.retailPrice + "");
                util.tv_retailprice.setText("¥"+bean.counterPrice + "");

            }

            util.tv_retailprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_article;
            TextView tv_name;
            TextView tv_jianjie;
            TextView tv_counterprice;
            TextView tv_jifenlabel;
            TextView tv_retailprice;

        }
    }
}
