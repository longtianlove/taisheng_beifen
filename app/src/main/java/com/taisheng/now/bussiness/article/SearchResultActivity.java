package com.taisheng.now.bussiness.article;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.taisheng.now.bussiness.bean.post.ArticlePostBean;
import com.taisheng.now.bussiness.bean.result.ArticleBean;
import com.taisheng.now.bussiness.bean.result.ArticleResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;

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

    @BindView(R.id.lv_articles)
    TaishengListView lvArticles;
    private ArticlePostBean bean;
    private ArticleAdapter madapter;
    private  String searchkey;
    private  int PAGE_NO = 1;
    private  int PAGE_SIZE = 10;
    @Override
    public void initView() {
        setContentView(R.layout.activity_searchresult);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        searchkey = intent.getStringExtra("searchkey");
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        bean = new ArticlePostBean();

    }

    @Override
    public void addData() {
        getArticles();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.search_result));
    }

    private void initViews() {
        madapter = new ArticleAdapter(this);
        lvArticles.setAdapter(madapter);
        lvArticles.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getArticles();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        PAGE_NO = 1;
    }

    private void getArticles() {
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.search = searchkey;
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        bean.type = "";
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().articleList(bean).enqueue(new TaiShengCallback<BaseBean<ArticleResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArticleResultBean>> response, BaseBean<ArticleResultBean> message) {
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:

                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvArticles.setLoading(false);
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvArticles.setHasLoadMore(false);
                                lvArticles.setLoadAllViewText("暂时只有这么多文章");
                                lvArticles.setLoadAllFooterVisible(true);
                            } else {
                                lvArticles.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvArticles.setHasLoadMore(false);
                            lvArticles.setLoadAllViewText("暂时只有这么多文章");
                            lvArticles.setLoadAllFooterVisible(true);
                        }

                        break;

                }

            }

            @Override
            public void onFail(Call<BaseBean<ArticleResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();
            }
        });
    }


    class ArticleAdapter extends BaseAdapter {

        public Context mcontext;

        List<ArticleBean> mData = new ArrayList<ArticleBean>();

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
                convertView = inflater.inflate(R.layout.item_article, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_article = (SimpleDraweeView) convertView.findViewById(R.id.sdv_article);
                util.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                util.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                util.tv_typename = (TextView) convertView.findViewById(R.id.tv_typename);
                util.tv_createtime = (TextView) convertView.findViewById(R.id.tv_createtime);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            ArticleBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchResultActivity.this, ArticleContentActivity.class);
                    intent.putExtra("articleId", bean.id);
                    intent.putExtra("articlePic", bean.picUrl);
                    intent.putExtra("summary", bean.summary);
                    intent.putExtra("title", bean.title);
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
            util.tv_title.setText(bean.title);
            util.tv_content.setText(bean.summary);
//            try {
//                if (bean.content != null) {
//                    util.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//                    RichText.fromHtml(bean.content).into(util.tv_content);
//                }
//            }catch (Exception e){
//                Log.e("article",e.getMessage());
//            }
            util.tv_typename.setText(bean.typeName);
            util.tv_createtime.setText(bean.createTime);

            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_article;
            TextView tv_title;
            TextView tv_content;
            TextView tv_typename;
            TextView tv_createtime;

        }
    }
}
