package com.taisheng.now.bussiness.article;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.bean.post.ArticleWithDoctorPostBean;
import com.taisheng.now.bussiness.bean.result.ArticleBean;
import com.taisheng.now.bussiness.bean.post.ArticlePostBean;
import com.taisheng.now.bussiness.bean.result.ArticleResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorsResultBean;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/1.
 */

public class SecretTabFragment extends BaseFragment {
    public String typeName;
    public TaishengListView lv_articles;
    ArticlePostBean bean;
    ArticleAdapter madapter;


    MaterialDesignPtrFrameLayout ptr_refresh;

    View ll_all;
    SimpleDraweeView sdv_header;
    TextView tv_doctor_name;

    TextView tv_title;
    TextView tv_onlineStatus;
    TextView tv_times;
    DoctorLabelWrapLayout dlwl_doctor_label;
    ScoreStar scorestar;
    TextView btn_zixun;

    View btn_change;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_secret_tab, container, false);
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

        ll_all = rootView.findViewById(R.id.ll_all);
        sdv_header = (SimpleDraweeView) rootView.findViewById(R.id.sdv_header);
        tv_doctor_name = (TextView) rootView.findViewById(R.id.tv_doctor_name);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_onlineStatus = (TextView) rootView.findViewById(R.id.tv_onlineStatus);
        tv_times = (TextView) rootView.findViewById(R.id.tv_times);
        dlwl_doctor_label = (DoctorLabelWrapLayout) rootView.findViewById(R.id.dlwl_doctor_label);
        scorestar = (ScoreStar) rootView.findViewById(R.id.scorestar);
        btn_zixun = (TextView) rootView.findViewById(R.id.btn_zixun);


        btn_change = rootView.findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DOCTOR_pageNo++;
                getDoctorTypeList();
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
        DOCTOR_pageNo = 1;
        getDoctorTypeList();
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        bean = new ArticlePostBean();
        getArticles();

    }

    public int DOCTOR_pageNo = 1;
    public int DOCTOR_pageSize = 1;

    void getDoctorTypeList() {
        ArticleWithDoctorPostBean bean = new ArticleWithDoctorPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = DOCTOR_pageNo;
        bean.pageSize = DOCTOR_pageSize;
        switch (typeName) {
            case Constants.SUSHENHUFU:
                bean.type = 1;
                break;
            case Constants.JIANSHENYUNDONG:
                bean.type = 2;
                break;
            case Constants.SHILIAOYANGSHENG:
                bean.type = 3;
                break;
            case Constants.YONGYAOZHIDAO:
                bean.type = 4;
                break;
            case Constants.MUYINGYUNYU:
                bean.type = 5;
                break;
        }
        ApiUtils.getApiService().getDoctorTypeList(bean).enqueue(new TaiShengCallback<BaseBean<DoctorsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorsResultBean>> response, BaseBean<DoctorsResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {

                            DoctorBean bean = message.result.records.get(0);
                            ll_all.setVisibility(View.VISIBLE);
                            ll_all.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mActivity, DoctorDetailActivity.class);
                                    intent.putExtra("id", bean.id);
                                    intent.putExtra("nickName", bean.nickName);
                                    intent.putExtra("title", bean.title);
                                    intent.putExtra("fromMedicineTime", bean.fromMedicineTime);
                                    intent.putExtra("jobIntroduction", bean.jobIntroduction);
                                    intent.putExtra("score", bean.score);
                                    intent.putExtra("goodDiseases", bean.goodDiseases);
                                    startActivity(intent);
                                }
                            });
                            if (bean.avatar != null) {
                                Uri uri = Uri.parse(bean.avatar);
                                sdv_header.setImageURI(uri);
                            }
                            tv_doctor_name.setText(bean.nickName);
                            if (typeName.equals(Constants.YONGYAOZHIDAO)) {
                                tv_title.setText("职业药师");
                            } else {
                                tv_title.setText(bean.title);
                            }
                            if ("1".equals(bean.onlineStatus)) {
                                tv_onlineStatus.setText("在线");
                                tv_onlineStatus.setTextColor(Color.parseColor("#ff0dd500"));
                            } else {
                                tv_onlineStatus.setText("忙碌");
                                tv_onlineStatus.setTextColor(Color.parseColor("#ffff554e"));
                            }
                            tv_times.setText(bean.servicesNum);
                            if (bean.goodDiseases != null) {
                                String[] doctorlabel = bean.goodDiseases.split(",");
                                dlwl_doctor_label.setData(doctorlabel, mActivity, 10, 5, 1, 5, 1, 4, 4, 4, 8);

                            }

                            if (bean.score != null) {
                                scorestar.setScore(bean.score);
                            }
                            btn_zixun.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mActivity, DoctorDetailActivity.class);
                                    intent.putExtra("id", bean.id);
                                    intent.putExtra("nickName", bean.nickName);
                                    intent.putExtra("title", bean.title);
                                    intent.putExtra("fromMedicineTime", bean.fromMedicineTime);
                                    intent.putExtra("jobIntroduction", bean.jobIntroduction);
                                    intent.putExtra("score", bean.score);
                                    intent.putExtra("goodDiseases", bean.goodDiseases);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            ll_all.setVisibility(View.GONE);

                        }
                        break;
                }


            }

            @Override
            public void onFail(Call<BaseBean<DoctorsResultBean>> call, Throwable t) {

            }
        });
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getArticles() {
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.search = "";
        switch (typeName) {
            case Constants.SUSHENHUFU:
                bean.type = "fe50b23ae5e68434def76f67cef35d01";
                break;
            case Constants.JIANSHENYUNDONG:
                bean.type = "fe50b23ae5e68434def76f67cef35d04";
                break;
            case Constants.SHILIAOYANGSHENG:
                bean.type = "fe50b23ae5e68434def76f67cef35d02";
                break;
            case Constants.YONGYAOZHIDAO:
                bean.type = "fe50b23ae5e68434def76f67cef35d05";
                break;
            case Constants.MUYINGYUNYU:
                bean.type = "fe50b23ae5e68434def76f67cef35d03";
                break;
        }
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        DialogUtil.showProgress(mActivity, "");
        ApiUtils.getApiService().articleList(bean).enqueue(new TaiShengCallback<BaseBean<ArticleResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArticleResultBean>> response, BaseBean<ArticleResultBean> message) {
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
                                lv_articles.setLoadAllViewText("暂时只有这么多文章");
                                lv_articles.setLoadAllFooterVisible(true);
                            } else {
                                lv_articles.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_articles.setHasLoadMore(false);
                            lv_articles.setLoadAllViewText("暂时只有这么多文章");
                            lv_articles.setLoadAllFooterVisible(true);
                        }

                        break;

                }

            }

            @Override
            public void onFail(Call<BaseBean<ArticleResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();
                ptr_refresh.refreshComplete();
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
            ArticleAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new ArticleAdapter.Util();
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
                util = (ArticleAdapter.Util) convertView.getTag();
            }
            ArticleBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ArticleContentActivity.class);
                    intent.putExtra("articleId", bean.id);
                    intent.putExtra("articlePic",bean.picUrl);
                    intent.putExtra("summary",bean.summary);
                    intent.putExtra("title",bean.title);
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
