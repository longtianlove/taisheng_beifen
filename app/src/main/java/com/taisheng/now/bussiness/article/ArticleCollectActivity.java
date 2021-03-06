package com.taisheng.now.bussiness.article;

import android.content.Context;
import android.content.Intent;
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
import com.taisheng.now.bussiness.bean.post.CollectListPostBean;
import com.taisheng.now.bussiness.bean.result.ArticleBean;
import com.taisheng.now.bussiness.bean.result.ArticleCollectListResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class ArticleCollectActivity extends BaseHActivity {

    @BindView(R.id.lv_doctors)
    TaishengListView lvDoctors;
    @BindView(R.id.ptr_refresh)
    MaterialDesignPtrFrameLayout ptrRefresh;
    private ArticleAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;
    @Override
    public void initView() {
        setContentView(R.layout.activity_article_collect);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        getDoctors_hasdialog();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.treasure));
    }

   private void initViews() {

        /**
         * 下拉刷新
         */
        ptrRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                PAGE_SIZE = 10;
                getDoctors();
            }
        });

        madapter = new ArticleAdapter(this);
        lvDoctors.setAdapter(madapter);
        lvDoctors.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });
    }


    private void getDoctors_hasdialog() {
        CollectListPostBean bean = new CollectListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.collectionType = "2";
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().articlecollectionlist(bean).enqueue(new TaiShengCallback<BaseBean<ArticleCollectListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArticleCollectListResultBean>> response, BaseBean<ArticleCollectListResultBean> message) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvDoctors.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvDoctors.setHasLoadMore(false);
                                lvDoctors.setLoadAllViewText("暂时只有这么多文章");
                                lvDoctors.setLoadAllFooterVisible(true);
                            } else {
                                lvDoctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvDoctors.setHasLoadMore(false);
                            lvDoctors.setLoadAllViewText("暂时只有这么多文章");
                            lvDoctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArticleCollectListResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });

    }

   private void getDoctors() {
        CollectListPostBean bean = new CollectListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.collectionType = "2";
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().articlecollectionlist(bean).enqueue(new TaiShengCallback<BaseBean<ArticleCollectListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArticleCollectListResultBean>> response, BaseBean<ArticleCollectListResultBean> message) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvDoctors.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvDoctors.setHasLoadMore(false);
                                lvDoctors.setLoadAllViewText("暂时只有这么多文章");
                                lvDoctors.setLoadAllFooterVisible(true);
                            } else {
                                lvDoctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvDoctors.setHasLoadMore(false);
                            lvDoctors.setLoadAllViewText("暂时只有这么多文章");
                            lvDoctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArticleCollectListResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
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
                    Intent intent = new Intent(ArticleCollectActivity.this, ArticleContentActivity.class);
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
