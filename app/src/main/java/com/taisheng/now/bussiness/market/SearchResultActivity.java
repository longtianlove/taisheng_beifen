package com.taisheng.now.bussiness.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.MarketPostBean;
import com.taisheng.now.bussiness.bean.result.market.ShangPinResultBeann;
import com.taisheng.now.bussiness.bean.result.market.ShangpinBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class SearchResultActivity extends BaseHActivity {

    @BindView(R.id.lv_goods)
    TaishengListView lvGoods;
    private MarketPostBean bean;
    private ArticleAdapter madapter;
    private String searchkey;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;

    @Override
    public void initView() {
        setContentView(R.layout.activity_marketsearchresult);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        madapter = new ArticleAdapter(this);
        lvGoods.setAdapter(madapter);
        lvGoods.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getArticles();
            }
        });
    }

    @Override
    public void addData() {
        Intent intent = getIntent();
        searchkey = intent.getStringExtra("searchkey");
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        bean = new MarketPostBean();
        getArticles();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.search_result));
    }


    @Override
    protected void onResume() {
        super.onResume();
        PAGE_NO = 1;
    }

   private void getArticles() {
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.keyWord = searchkey;
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService_hasdialog().goodslist(bean).enqueue(new TaiShengCallback<BaseBean<ShangPinResultBeann>>() {
            @Override
            public void onSuccess(Response<BaseBean<ShangPinResultBeann>> response, BaseBean<ShangPinResultBeann> message) {
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:

                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvGoods.setLoading(false);
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvGoods.setHasLoadMore(false);
                                lvGoods.setLoadAllViewText("暂时只有这么多商品");
                                lvGoods.setLoadAllFooterVisible(true);
                            } else {
                                lvGoods.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvGoods.setHasLoadMore(false);
                            lvGoods.setLoadAllViewText("暂时只有这么多商品");
                            lvGoods.setLoadAllFooterVisible(true);
                        }

                        break;

                }

            }

            @Override
            public void onFail(Call<BaseBean<ShangPinResultBeann>> call, Throwable t) {
                DialogUtil.closeProgress();
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
                util.tv_counterprice = convertView.findViewById(R.id.tv_counterprice);
                util.tv_jifenlabel = convertView.findViewById(R.id.tv_jifenlabel);
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

                    Intent intent = new Intent(SearchResultActivity.this, ShangPinxiangqingActivity.class);
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
            if (bean.scoreGoods == 0) {
                util.tv_counterprice.setText(bean.retailPrice.multiply(new BigDecimal(100)) + "");
                util.tv_jifenlabel.setVisibility(View.VISIBLE);
                util.tv_retailprice.setVisibility(View.GONE);
//                util.tv_retailprice.setText(bean.counterPrice .multiply(new BigDecimal(100))+ "");
            } else {
                util.tv_jifenlabel.setVisibility(View.GONE);
                util.tv_retailprice.setVisibility(View.VISIBLE);
                util.tv_counterprice.setText("¥" + bean.retailPrice + "");
                util.tv_retailprice.setText("¥" + bean.counterPrice + "");

            }
            util.tv_retailprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_article;
            TextView tv_name;
            TextView tv_counterprice;
            TextView tv_jifenlabel;
            TextView tv_retailprice;

        }
    }

}
