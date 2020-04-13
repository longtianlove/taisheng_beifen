package com.taisheng.now.bussiness.watch.watchfirst;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.GetheartratePostBean;
import com.taisheng.now.bussiness.watch.bean.post.ObtainBpxyHeartStepListDTOPostBean;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.result.BushuBean;
import com.taisheng.now.bussiness.watch.bean.result.GetheartrateResultBean;

import com.taisheng.now.bussiness.watch.bean.result.XinLvBean;

import com.taisheng.now.bussiness.watch.bean.result.XinLvResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XinlvAnriqiResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class XinlvFragment extends BaseFragment {
    @BindView(R.id.tv_xinlv)
    TextView tvXinlv;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_html_line)
    WebView tvHtmlLine;
    @BindView(R.id.tv_html_bar)
    WebView tvHtmlBar;
    private Date today = new Date();
    private Date nowshow = new Date();
    private int beforeDayNum = 0;
    private int start = 0;
    private List<String> xListLine = new ArrayList<>();
    private List<Integer> dataLine = new ArrayList<>();

    private List<String> xListBar = new ArrayList<>();
    private List<Integer> dataBar1 = new ArrayList<>();
    private List<Integer> dataBar2 = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_xinlv, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        beforeDayNum = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        long time1 = cal.getTimeInMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = new Date();
        try {
            date2 = sdf.parse(WatchInstance.getInstance().createTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        start = (int) between_days;
    }

    private void anrizhihuoquueya() {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(today);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, beforeDayNum);  //设置为前一天
        nowshow = calendar.getTime();   //得到前一天的时间

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(nowshow);

        ObtainBpxyHeartStepListDTOPostBean bean = new ObtainBpxyHeartStepListDTOPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        bean.queryDate = dateString;
        //获取实时日统计
        ApiUtils.getApiService().obtainHeartList(bean).enqueue(new TaiShengCallback<BaseBean<ArrayList<XinlvAnriqiResultBean>>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArrayList<XinlvAnriqiResultBean>>> response, BaseBean<ArrayList<XinlvAnriqiResultBean>> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        webViewSet(tvHtmlLine, 1);
                        if (xListLine != null) {
                            xListLine.clear();
                        }
                        if (dataLine != null) {
                            dataLine.clear();
                        }
                        if (message.result != null && message.result.size() > 0) {
                            for (int i = 0; i < message.result.size(); i++) {
                                XinlvAnriqiResultBean xinlvAnriqiResultBean = message.result.get(i);
                                String[] split = xinlvAnriqiResultBean.createTime.split(" ")[1].split(":");
                                xListLine.add(split[0] + "时");
                                dataLine.add(Integer.valueOf(TextsUtils.isEmptys(xinlvAnriqiResultBean.heartNum + "", "0")));
                            }
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArrayList<XinlvAnriqiResultBean>>> call, Throwable t) {

            }
        });
    }

    void initData() {
        GetheartratePostBean bean = new GetheartratePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;

//获取实时统计
        ApiUtils.getApiService().getheartrate(bean).enqueue(new TaiShengCallback<BaseBean<GetheartrateResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetheartrateResultBean>> response, BaseBean<GetheartrateResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null) {
                            WatchInstance.getInstance().watchHeart = message.result.heartNum + "";
                            tvLabel.setText("次/分");
                            tvXinlv.setVisibility(View.VISIBLE);
                            tvXinlv.setText(WatchInstance.getInstance().watchHeart);
                        } else {
                            //todo 数据为空的时候修改UI
                            tvLabel.setText("暂无数据");
                            tvXinlv.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GetheartrateResultBean>> call, Throwable t) {

            }
        });


        anrizhihuoquueya();

        ShishiCollectionBean bean1 = new ShishiCollectionBean();
        bean1.userId = UserInstance.getInstance().getUid();
        bean1.token = UserInstance.getInstance().getToken();
        bean1.deviceId = WatchInstance.getInstance().deviceId;
        //获取月统计
        ApiUtils.getApiService().xunlv_querythismonth(bean1).enqueue(new TaiShengCallback<BaseBean<XinLvResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XinLvResultBean>> response, BaseBean<XinLvResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        webViewSet(tvHtmlBar, 2);
                        if (xListBar != null) {
                            xListBar.clear();
                        }
                        if (dataBar1 != null) {
                            dataBar1.clear();
                        }
                        if (dataBar2 != null) {
                            dataBar2.clear();
                        }
                        if (message.result.records != null && message.result.records.size() > 0) {
                            for (int i = 0; i < message.result.records.size(); i++) {
                                XinLvBean xinLvBean = message.result.records.get(i);
                                String[] split = xinLvBean.createTime.split(" ")[0].split("-");
                                String m = split[1];
                                if (m.startsWith("0")) {
                                    m = m.replace("0", "");
                                }
                                String d = split[2];
                                if (d.startsWith("0")) {
                                    d = d.replace("0", "");
                                }
                                xListBar.add(d + "/" + m);
                                dataBar1.add(Integer.valueOf(TextsUtils.isEmptys(xinLvBean.heartNumMax + "", "0")));
                                dataBar2.add(Integer.valueOf(TextsUtils.isEmptys(xinLvBean.heartNumMin + "", "0")));
                            }
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XinLvResultBean>> call, Throwable t) {

            }
        });
    }

    private void webViewSet(WebView webView, int type) {
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (type == 1) {
            webView.loadUrl("file:///android_asset/product/heartline.html");
        } else {
            webView.loadUrl("file:///android_asset/product/heartbar.html");
        }

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
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
        webView.addJavascriptInterface(new JsAndroid(), "android");
    }

    @SuppressLint("JavascriptInterface")
    class JsAndroid {
        @JavascriptInterface
        public int getXlistSize() {
            return xListLine.size();
        }

        @JavascriptInterface
        public String getXlistineL(int i) {
            return xListLine.get(i);
        }

        @JavascriptInterface
        public int getDatasLine(int i) {
            return (int) dataLine.get(i);
        }

        @JavascriptInterface
        public int getXlistBarSize() {
            return xListBar.size();
        }

        @JavascriptInterface
        public String getXlistBar(int i) {
            return xListBar.get(i);
        }

        @JavascriptInterface
        public int getDatasBar(int i) {
            return (int) dataBar1.get(i);
        }

        @JavascriptInterface
        public int getDatasBar2(int i) {
            return (int) dataBar2.get(i);
        }
    }

}
