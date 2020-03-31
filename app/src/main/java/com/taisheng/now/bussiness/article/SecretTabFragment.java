package com.taisheng.now.bussiness.article;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
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
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;
import com.th.j.commonlibrary.utils.DateUtil;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.SpanUtil;
import com.th.j.commonlibrary.utils.TextsUtils;
import com.th.j.commonlibrary.wight.RoundImgView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/1.
 */

public class SecretTabFragment extends BaseFragment {
    public String typeName;
    public String typeSoure;
    public TaishengListView lv_articles;
    ArticleAdapter madapter;
    private List<ArticleBean> data;

    MaterialDesignPtrFrameLayout ptr_refresh;

    RelativeLayout rl_all;
    SimpleDraweeView sdv_header;
    TextView tv_doctor_name;

    TextView tv_title;
    TextView btn_zixun, tv_goo_at, tv_praise,tv_year;

    private TextView tv_scorestar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_secret_tab, container, false);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        data = new ArrayList<>();
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

        rl_all = rootView.findViewById(R.id.ll_all);
        sdv_header = (SimpleDraweeView) rootView.findViewById(R.id.sdv_header);
        tv_doctor_name = (TextView) rootView.findViewById(R.id.tv_doctor_name);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_scorestar = (TextView) rootView.findViewById(R.id.tv_scorestar);
        btn_zixun = (TextView) rootView.findViewById(R.id.btn_zixun);
        tv_goo_at = (TextView) rootView.findViewById(R.id.tv_goo_at);
        tv_praise = (TextView) rootView.findViewById(R.id.tv_praise);
        tv_year = (TextView) rootView.findViewById(R.id.tv_year);

        lv_articles = (TaishengListView) rootView.findViewById(R.id.lv_articles);
        madapter = new ArticleAdapter(mActivity);
        lv_articles.setAdapter(madapter);
        lv_articles.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                PAGE_NO++;
                getArticles();
            }
        });
    }

  public  void initData() {
        DOCTOR_pageNo = 1;
        getDoctorTypeList();
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        if (data != null) {
            data.clear();
        }
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

        if(typeName.equals(Constants.SUSHENHUFU)){
            bean.type = 1;
        }else if (typeName.equals(Constants.JIANSHENYUNDONG)){
            bean.type = 2;
        }else if (typeName.equals(Constants.SHILIAOYANGSHENG)){
            bean.type = 3;
        }else if (typeName.equals(Constants.YONGYAOZHIDAO)){
            bean.type = 4;
        }else if (typeName.equals(Constants.MUYINGYUNYU)){
            bean.type = 5;
        }
        ApiUtils.getApiService().getDoctorTypeList(bean).enqueue(new TaiShengCallback<BaseBean<DoctorsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorsResultBean>> response, BaseBean<DoctorsResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {

                            DoctorBean bean = message.result.records.get(0);
                            rl_all.setVisibility(View.VISIBLE);
                            rl_all.setOnClickListener(new View.OnClickListener() {
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
                            if (!TextsUtils.isEmpty(bean.fromMedicineTime)) {
                                String time = DateUtil.getDatePoor2(Long.valueOf(DateUtil.getTime()) / 1000, Long.valueOf(DateUtil.dateToStamp(bean.fromMedicineTime)) / 1000);
                                tv_year.setText(getString(R.string.home03)+time+getString(R.string.home04));
                            }
                            tv_goo_at.setText(getString(R.string.home02) + bean.goodDiseases);

                            SpanUtil.create()
//                            .addSection(String.valueOf(messageWaitTime) + "S"+"重新发送")  //添加带前景色的文字片段
                                    .addForeColorSection("好评率：", ContextCompat.getColor(getActivity(), R.color.color666666)) //设置相对字体
                                    .addForeColorSection("98%      ", ContextCompat.getColor(getActivity(), R.color.color00c8aa)) //设置相对字体
                                    .addForeColorSection("咨询量：", ContextCompat.getColor(getActivity(), R.color.color666666)) //设置相对字体
                                    .addForeColorSection(bean.servicesNum + "", ContextCompat.getColor(getActivity(), R.color.color00c8aa)) //设置相对字体
                                    .showIn(tv_praise); //显示到控件TextView中

                            if (bean.score != null) {
                                tv_scorestar.setText(bean.score + getString(R.string.branch));
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
                                    LogUtilH.e(bean.score+"===bean.score");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            rl_all.setVisibility(View.GONE);
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
        ArticlePostBean bean = new ArticlePostBean();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.search = "";
        if (TextsUtils.isEmpty(typeSoure)){
            return;
        }
        bean.type=typeSoure;
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        DialogUtil.showProgress(mActivity, "");
        ApiUtils.getApiService().articleList(bean).enqueue(new TaiShengCallback<BaseBean<ArticleResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArticleResultBean>> response, BaseBean<ArticleResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null) {
                            if (message.result.records != null && message.result.records.size() > 0) {
                                lv_articles.setLoading(false);
                                data.addAll(message.result.records);
                                if (message.result.records.size() < 10) {
                                    lv_articles.setHasLoadMore(false);
                                    lv_articles.setLoadAllViewText("暂时只有这么多文章");
                                    lv_articles.setLoadAllFooterVisible(true);
                                } else {
                                    lv_articles.setHasLoadMore(true);
                                }
                                madapter.setmData(data);
                            } else {
                                //没有消息
                                lv_articles.setHasLoadMore(false);
                                lv_articles.setLoadAllViewText("暂时只有这么多文章");
                                lv_articles.setLoadAllFooterVisible(true);
                            }
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
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });
    }


    class ArticleAdapter extends BaseAdapter {

        private Context mcontext;

        private List<ArticleBean> mData;

        public ArticleAdapter(Context context) {
            this.mcontext = context;
        }

        public void setmData(List<ArticleBean> mData) {
            if (mData!=null) {
                this.mData = mData;
                this.notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
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
                util.sdv_article2 =  convertView.findViewById(R.id.sdv_article2);
                util.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
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
                    Intent intent = new Intent(mActivity, ArticleContentActivity.class);
                    intent.putExtra("articleId", bean.id);
                    intent.putExtra("articlePic", bean.picUrl);
                    intent.putExtra("summary", bean.summary);
                    intent.putExtra("title", bean.title);
                    startActivity(intent);
                }
            });
            util.sdv_article.setVisibility(View.GONE);
            util.sdv_article2.setVisibility(View.VISIBLE);
            String temp_url = bean.picUrl;
//            util.sdv_article.setImageURI(temp_url);
            Glide.with(mcontext)
                    .load(temp_url)
//                    .bitmapTransform(new GlideRoundUtils(mcontext,10, GlideRoundUtils.CornerType.ALL))
                    .placeholder(R.drawable.article_default)
                    .error(R.drawable.article_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(util.sdv_article2);
            util.tv_title.setText(TextsUtils.isEmptys(bean.title,""));
            util.tv_typename.setText(TextsUtils.isEmptys(bean.typeName,""));
            if (!TextsUtils.isEmpty(bean.createTime)) {
                String time = DateUtil.getDatePoor(Long.valueOf(DateUtil.getTime()) / 1000, Long.valueOf(DateUtil.dateToStamp(bean.createTime)) / 1000);
                util.tv_createtime.setText(TextsUtils.isEmptys(time, bean.createTime));
            }
            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_article;
            RoundImgView sdv_article2;
            TextView tv_title;
            TextView tv_typename;
            TextView tv_createtime;

        }
    }
}
