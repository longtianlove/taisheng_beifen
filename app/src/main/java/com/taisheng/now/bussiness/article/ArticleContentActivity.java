package com.taisheng.now.bussiness.article;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.mob.MobSDK;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.ArticleCollectionBean;
import com.taisheng.now.bussiness.bean.post.ArticleShareBean;
import com.taisheng.now.bussiness.bean.post.CollectAddorRemovePostBean;
import com.taisheng.now.bussiness.bean.post.UpdateArticleReadCountPostBean;
import com.taisheng.now.bussiness.bean.result.ArticleContentBean;
import com.taisheng.now.bussiness.bean.post.ArticleContentPostBean;
import com.taisheng.now.bussiness.bean.result.CollectAddorRemoveResultBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DoubleClickUtil;
import com.zzhoujay.richtext.RichText;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/8.
 */

public class ArticleContentActivity extends BaseActivity {


    View iv_back;
    TextView tv_title;
    View tv_share;
    //    TextView tv_content;
    WebView webView;

    View ll_collect;
    TextView tv_collect;
    TextView tv_collect_label;
    TextView tv_typename;
    TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        initView();
        initData();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_share=findViewById(R.id.tv_share);
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
//        tv_content = (TextView) findViewById(R.id.tv_content);
        webView = (WebView) findViewById(R.id.webView);

        ll_collect = findViewById(R.id.ll_collect);
        ll_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (DoubleClickUtil.isFastMiniDoubleClick()) {
                    return;
                }
                ArticleCollectionBean bean = new ArticleCollectionBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.label = "app";
                bean.dataId = articleId;
                ApiUtils.getApiService().saveCollectionArticleLog(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
//                                String resultFeedback = message.result.resultFeedback;
                                if ("YES".equals(collectionFlag)) {
                                    collectionFlag = "NO";
                                    tv_collect.setEnabled(false);
                                    tv_collect_label.setText("  收藏");
                                } else {
                                    collectionFlag = "YES";
                                    tv_collect.setEnabled(true);
                                    tv_collect_label.setText("已收藏");
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        tv_collect = (TextView) findViewById(R.id.tv_collect);
        tv_collect_label = (TextView) findViewById(R.id.tv_collect_label);
        tv_typename = (TextView) findViewById(R.id.tv_typename);
        tv_time = (TextView) findViewById(R.id.tv_time);
    }


    String collectionFlag;
    String articleId;
    String title;
    String articlePic;
    String summary;



    void initData() {
        Intent intent = getIntent();
        articleId = intent.getStringExtra("articleId");
        articlePic=intent.getStringExtra("articlePic");
        summary=intent.getStringExtra("summary");
        title=intent.getStringExtra("title");
        ArticleContentPostBean bean = new ArticleContentPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.articleId = articleId;
        ApiUtils.getApiService().articleQeryById(bean).enqueue(new TaiShengCallback<BaseBean<ArticleContentBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArticleContentBean>> response, BaseBean<ArticleContentBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
//                        tv_title.setText(message.result.title);
//                        tv_content.setText(message.result.content);
                        if (message.result.content != null) {
//                            tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//                            RichText.fromHtml(message.result.content).into(tv_content);
                            try {
//                                tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//                                RichText.fromHtml(message.result.content).into(tv_content);
//                                String content=message.result.content.replace("<img", "<img style=\"max-width:100%;height:auto");


//                                String sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
//                                        "initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />" +
//                                        "<style>img{max-width:100% !important;height:auto !important;}</style>"
//                                        + "<style>body{max-width:100% !important;}</style>" + "</head><body>";
//                                webView.loadDataWithBaseURL(null, sHead + message.result.content + "</body></html>", "text/html", "utf-8", null);
                                webView.loadUrl(Constants.Url.Article.articleContent+articleId+"&type=app&userId="+UserInstance.getInstance().getUid());
                            } catch (Exception e) {
                                Log.e("article", e.getMessage());
                            }
                        }
                        collectionFlag = message.result.collectionFlag;
                        if ("YES".equals(message.result.collectionFlag)) {
                            tv_collect.setEnabled(true);
                            tv_collect_label.setText("已收藏");
                        } else {
                            tv_collect.setEnabled(false);
                            tv_collect_label.setText("  收藏");
                        }
                        tv_typename.setText(message.result.typeName);
                        tv_time.setText(message.result.createTime);
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArticleContentBean>> call, Throwable t) {

            }
        });
        updateArticleReadCount();
    }

    public void updateArticleReadCount() {
        UpdateArticleReadCountPostBean bean=new UpdateArticleReadCountPostBean();
        bean.userId=UserInstance.getInstance().getUid();
        bean.token=UserInstance.getInstance().getToken();
        bean.articleId=articleId;
        ApiUtils.getApiService().updateArticleReadCount(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {

            }

            @Override
            public void onFail(Call<BaseBean> call, Throwable t) {

            }
        });
    }




    //java
    private void showShare() {
        PlatformActionListener callback = new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //  分享成功后的操作或者提示
                ArticleShareBean bean = new ArticleShareBean();
                bean.userId = UserInstance.getInstance().getUid();
//                bean.token = UserInstance.getInstance().getToken();
                bean.shareType= "app";
                bean.articleId = articleId;
                ApiUtils.getApiService().saveShareArticleLog(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:

                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                //  失败，打印throwable为错误码
            }

            @Override
            public void onCancel(Platform platform, int i) {
                //  分享取消操作
            }
        };


        OnekeyShare oks = new OnekeyShare();
        // 设置自定义的外部回调
        oks.setCallback(callback);
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(Constants.Url.Article.articleContent+articleId);
        // text是分享文本，所有平台都需要这个字段
        if(TextUtils.isEmpty(summary)){
            oks.setText("想要健康的身体，看这里~");
        }else{
            oks.setText(summary);
        }
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
//        oks.setImagePath("/sdcard/test.jpg");
//        oks.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app));
        oks.setImageUrl(articlePic);
        // url在微信、Facebook等平台中使用
        oks.setUrl(Constants.Url.Article.articleContent+articleId+"&shareType=app&userId="+UserInstance.getInstance().getUid());
        // 启动分享GUI
        oks.show(this);
    }


    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setTitle("泰晟健康");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(Constants.Url.Article.articleContent+articleId);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
//        oks.setImagePath("/sdcard/test.jpg");
        oks.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app));
        // url在微信、Facebook等平台中使用
        oks.setUrl(Constants.Url.Article.articleContent+articleId);
        //启动分享
        oks.show(MobSDK.getContext());
    }


}
