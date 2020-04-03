package com.taisheng.now.bussiness.article;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mob.MobSDK;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.ArticleCollectionBean;
import com.taisheng.now.bussiness.bean.post.ArticleContentPostBean;
import com.taisheng.now.bussiness.bean.post.ArticleShareBean;
import com.taisheng.now.bussiness.bean.post.UpdateArticleReadCountPostBean;
import com.taisheng.now.bussiness.bean.result.ArticleContentBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DoubleClickUtil;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.wight.MyWebView;

import java.util.HashMap;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/8.
 */

public class ArticleContentActivity extends BaseIvActivity {


    @BindView(R.id.webView)
    MyWebView webView;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.tv_collect_label)
    TextView tvCollectLabel;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.tv_typename)
    TextView tvTypename;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private String collectionFlag;
    private String articleId;
    private String title;
    private String articlePic;
    private String summary;


    @Override
    public void initView() {
        setContentView(R.layout.activity_article_content);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        articleId = intent.getStringExtra("articleId");
        articlePic = intent.getStringExtra("articlePic");
        summary = intent.getStringExtra("summary");
        title = intent.getStringExtra("title");

    }

    @Override
    public void addData() {
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
                                loadWeb();
                            } catch (Exception e) {
                                Log.e("article", e.getMessage());
                            }
                        }
                        collectionFlag = message.result.collectionFlag;
                        if ("YES".equals(message.result.collectionFlag)) {
                            tvCollect.setEnabled(true);
                            tvCollectLabel.setText(getString(R.string.article_details04));
                        } else {
                            tvCollect.setEnabled(false);
                            tvCollectLabel.setText(getString(R.string.article_details03));
                        }
                        tvTypename.setText(message.result.typeName);
                        tvTime.setText(message.result.createTime);
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArticleContentBean>> call, Throwable t) {

            }
        });
        updateArticleReadCount();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.article_details));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.article_details01));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }
    @OnClick({R.id.tv_collect, R.id.ll_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_collect:
                break;
            case R.id.ll_collect:
                if (DoubleClickUtil.isFastMiniDoubleClick()) {
                    return;
                }
                ArticleCollectionBean bean = new ArticleCollectionBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.dataSource = "0";
                bean.dataId = articleId;
                ApiUtils.getApiService().saveCollectionArticleLog(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
//                                String resultFeedback = message.result.resultFeedback;
                                if ("YES".equals(collectionFlag)) {
                                    collectionFlag = "NO";
                                    tvCollect.setEnabled(false);
                                    tvCollectLabel.setText(getString(R.string.article_details03));
                                } else {
                                    collectionFlag = "YES";
                                    tvCollect.setEnabled(true);
                                    tvCollectLabel.setText(getString(R.string.article_details04));
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void updateArticleReadCount() {
        UpdateArticleReadCountPostBean bean = new UpdateArticleReadCountPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.articleId = articleId;
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
                bean.shareType = "app";
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
        oks.setTitleUrl(Constants.Url.Article.articleContent + articleId);
        // text是分享文本，所有平台都需要这个字段
        if (TextUtils.isEmpty(summary)) {
            oks.setText(getString(R.string.article_details02));
        } else {
            oks.setText(summary);
        }
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
//        oks.setImagePath("/sdcard/test.jpg");
//        oks.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app));
        oks.setImageUrl(articlePic);
        // url在微信、Facebook等平台中使用
        oks.setUrl(Constants.Url.Article.articleContent + articleId + "&shareType=app&userId=" + UserInstance.getInstance().getUid());
        // 启动分享GUI
        oks.show(this);
    }


    /**
     * webview设置
     */
    private void loadWeb() {
        String urlpinjie = Constants.Url.Article.articleContent + articleId + "&type=app&userId=" + UserInstance.getInstance().getUid();
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
                //重新为WebView设置高度
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) wb.getLayoutParams();
//                params.width =LinearLayout.LayoutParams.MATCH_PARENT;
//                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
//                wb.setLayoutParams(params);
            }
        });
        webView.loadUrl(urlpinjie);
        //是否可以后退
        webView.canGoBack();
        //是否可以前进
        webView.canGoForward();
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        //webSettings.setPluginsEnabled(true);//支持插件
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setTextZoom(100);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);  //自适应屏幕
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webView.addJavascriptInterface(new WebAppInterface(this), "android");
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setAppCacheEnabled(false);

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setOnScrollChangeListener(new MyWebView.OnScrollChangeListener() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

            }
        });
    }

    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setTitle(getString(R.string.app_name));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(Constants.Url.Article.articleContent + articleId);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
//        oks.setImagePath("/sdcard/test.jpg");
        oks.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app));
        // url在微信、Facebook等平台中使用
        oks.setUrl(Constants.Url.Article.articleContent + articleId);
        //启动分享
        oks.show(MobSDK.getContext());
    }



}
