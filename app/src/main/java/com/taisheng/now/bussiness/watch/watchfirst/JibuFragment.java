package com.taisheng.now.bussiness.watch.watchfirst;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.GetwatchstepPostBean;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.result.BushuBean;
import com.taisheng.now.bussiness.watch.bean.result.BushuResultBean;
import com.taisheng.now.bussiness.watch.bean.result.GetwatchstepResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class JibuFragment extends BaseFragment {
    @BindView(R.id.tv_bushu)
    TextView tvBushu;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.ll_guijiditu)
    LinearLayout llGuijiditu;
    @BindView(R.id.tv_gaoya)
    TextView tvGaoya;
    @BindView(R.id.tv_diya)
    TextView tvDiya;
    @BindView(R.id.tv_html)
    WebView tvHtml;
    private List<String> xList = new ArrayList<>();
    private List<Integer> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_jibu, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick(R.id.ll_guijiditu)
    public void onClick() {
        Intent intent = new Intent(getActivity(), HistoryGuijiActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }


    public void initData() {
        GetwatchstepPostBean bean = new GetwatchstepPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
//        获取计步的实时数据
        ApiUtils.getApiService().getwatchstep(bean).enqueue(new TaiShengCallback<BaseBean<GetwatchstepResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetwatchstepResultBean>> response, BaseBean<GetwatchstepResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null) {
                            WatchInstance.getInstance().stepNum = message.result.stepNum + "";
                            tvLabel.setVisibility(View.VISIBLE);
                            tvBushu.setText(WatchInstance.getInstance().stepNum);
                        } else {
                            tvLabel.setVisibility(View.GONE);
                            tvBushu.setText("0");
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GetwatchstepResultBean>> call, Throwable t) {

            }
        });
        getMonth();
    }


    private void getMonth() {
        ShishiCollectionBean bean = new ShishiCollectionBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
//        获取计步的月统计数据
        ApiUtils.getApiService().querythismonthwalk(bean).enqueue(new TaiShengCallback<BaseBean<BushuResultBean>>() {

            @Override
            public void onSuccess(Response<BaseBean<BushuResultBean>> response, BaseBean<BushuResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        webViewSet();
                        if (xList != null) {
                            xList.clear();
                        }
                        if (data != null) {
                            data.clear();
                        }
                        if (message.result.records != null && message.result.records.size() > 0) {
                            for (int i = 0; i < message.result.records.size(); i++) {
                                BushuBean bushuBean = message.result.records.get(i);
                                String[] split = bushuBean.createTime.split(" ");
                                String substring = split[0].substring(bushuBean.createTime.lastIndexOf("-") + 1);
                                xList.add(substring + "日");
                                data.add(Integer.valueOf(TextsUtils.isEmptys(bushuBean.getStepNum(), "0")));
                            }
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<BushuResultBean>> call, Throwable t) {

            }
        });

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
            return (int) data.get(i);
        }
    }
}
