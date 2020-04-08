package com.taisheng.now;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.base.BaseIvActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaceActivity extends BaseIvActivity {


    @BindView(R.id.tv_html)
    WebView tvHtml;
    List<String> xList=new ArrayList<>();
    List<Integer> data=new ArrayList<>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_pace);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        webViewSet();
        for (int i = 0; i < 6; i++) {
            xList.add((i+1)+"日");

        }

        for (int i = 0; i < 6; i++) {
            data.add((i*100));
        }
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }

    private void webViewSet() {
        tvHtml.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        tvHtml.loadUrl("file:///android_asset/product/pacetaking.html");
        //声明WebSettings子类
        WebSettings webSettings = tvHtml.getSettings();
        webSettings.setJavaScriptEnabled(true);//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript

        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);  //自适应屏幕
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        tvHtml.addJavascriptInterface(new JsAndroid(), "android");
    }

    @SuppressLint("JavascriptInterface")
    class JsAndroid {
        @JavascriptInterface
        public int getXlistSize() {
            return xList.size();
        }

        @JavascriptInterface
        public String getXlist(int i) {
            return xList.get(i);
        }

        @JavascriptInterface
        public int getDatas(int i) {
            return (int)data.get(i);
        }
    }
}
