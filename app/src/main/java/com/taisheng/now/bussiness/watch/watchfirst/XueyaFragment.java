package com.taisheng.now.bussiness.watch.watchfirst;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchBean;
import com.taisheng.now.bussiness.watch.bean.post.GetbloodpressurePostBean;
import com.taisheng.now.bussiness.watch.bean.post.ObtainBpxyHeartStepListDTOPostBean;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.result.GetbloodpressureResultBean;

import com.taisheng.now.bussiness.watch.bean.result.XueYaDayBean;

import com.taisheng.now.bussiness.watch.bean.result.XueYaDayResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XueyaResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;
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

public class XueyaFragment extends BaseFragment {

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_gaoya)
    TextView tvGaoya;
    @BindView(R.id.tv_diya)
    TextView tvDiya;
    @BindView(R.id.tv_html_line)
    WebView tvHtmlLine;
    @BindView(R.id.tv_html_bar)
    WebView tvHtmlBar;
    TextView tv_yuanchengceliang;
    private List<String> xListLine = new ArrayList<>();
    private List<Integer> dataLine = new ArrayList<>();
    private List<Integer> dataLine2 = new ArrayList<>();

    private List<String> xListBar = new ArrayList<>();
    private List<Integer> dataBar1 = new ArrayList<>();
    private List<Integer> dataBar2 = new ArrayList<>();
    private List<Integer> dataBar3 = new ArrayList<>();
    private List<Integer> dataBar4 = new ArrayList<>();
    private Date today = new Date();
    private Date nowshow = new Date();
    private int beforeDayNum = 0;
    private int start = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_xueya, container, false);
        tv_yuanchengceliang = rootView.findViewById(R.id.tv_yuanchengceliang);
        tv_yuanchengceliang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //远程测量接口
                BaseWatchBean bean = new BaseWatchBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                ApiUtils.getApiService_hasdialog().watchbloodMeasurement(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("指令发送成功");
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });

            }
        });
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

    /**
     * 日统计
     */
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
        //获取日统计
        ApiUtils.getApiService().obtainBpxyList(bean).enqueue(new TaiShengCallback<BaseBean<ArrayList<XueyaResultBean>>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArrayList<XueyaResultBean>>> response, BaseBean<ArrayList<XueyaResultBean>> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        webViewSet(tvHtmlLine, 1);
                        if (xListLine != null) {
                            xListLine.clear();
                        }
                        if (dataLine != null) {
                            dataLine.clear();
                        }
                        if (dataLine2 != null) {
                            dataLine2.clear();
                        }
                        if (message.result != null && message.result.size() > 0) {
                            for (int i = 0; i < message.result.size(); i++) {
                                XueyaResultBean xueyaResultBean = message.result.get(i);
                                String[] split = xueyaResultBean.createTime.split(" ");
                                String[] split1 = split[1].split(":");
                                xListLine.add(split1[0] + "时");
                                dataLine.add(Integer.valueOf(TextsUtils.isEmptys(xueyaResultBean.bloodPressureHigh + "", "0")));
                                dataLine2.add(Integer.valueOf(TextsUtils.isEmptys(xueyaResultBean.bloodPressureLow + "", "0")));
                            }
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArrayList<XueyaResultBean>>> call, Throwable t) {

            }
        });
    }

    void initData() {
        GetbloodpressurePostBean bean = new GetbloodpressurePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
//获取实时统计
        ApiUtils.getApiService().getbloodpressure(bean).enqueue(new TaiShengCallback<BaseBean<GetbloodpressureResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetbloodpressureResultBean>> response, BaseBean<GetbloodpressureResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null) {
                            WatchInstance.getInstance().watchBpxyHigh = message.result.bloodPressureHigh + "";
                            WatchInstance.getInstance().watchBpxyLow = message.result.bloodPressureLow + "";
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText(message.result.createTime.toString());
                            tvGaoya.setText(message.result.bloodPressureHigh + "");
                            tvDiya.setText(message.result.bloodPressureLow + "");
                        } else {

                            tvTime.setVisibility(View.GONE);
                            tvGaoya.setText("暂无数据");
                            tvDiya.setText("暂无数据");
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GetbloodpressureResultBean>> call, Throwable t) {

            }
        });
        ShishiCollectionBean bean1 = new ShishiCollectionBean();
        bean1.userId = UserInstance.getInstance().getUid();
        bean1.token = UserInstance.getInstance().getToken();
        bean1.deviceId = WatchInstance.getInstance().deviceId;
        //获取月统计
        ApiUtils.getApiService().xueya_querythismonth(bean1).enqueue(new TaiShengCallback<BaseBean<XueYaDayResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XueYaDayResultBean>> response, BaseBean<XueYaDayResultBean> message) {
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
                        if (dataBar3 != null) {
                            dataBar3.clear();
                        }
                        if (dataBar4 != null) {
                            dataBar4.clear();
                        }
                        for (int i = 0; i < message.result.records.size(); i++) {
                            XueYaDayBean xueYaDayBean = message.result.records.get(i);

                            String[] split = xueYaDayBean.createTime.split(" ")[0].split("-");
                            String m = split[1];
                            if (m.startsWith("0")) {
                                m = m.replace("0", "");
                            }
                            String d = split[2];
                            if (d.startsWith("0")) {
                                d = d.replace("0", "");
                            }
                            xListBar.add(d + "/" + m);
//                            xListBar.add( xueYaDayBean.createTime );
                            dataBar1.add(Integer.valueOf(TextsUtils.isEmptys(xueYaDayBean.bloodPressureHighMax + "", "0")));
                            dataBar2.add(Integer.valueOf(TextsUtils.isEmptys(xueYaDayBean.bloodPressureHighMin + "", "0")));
                            dataBar3.add(Integer.valueOf(TextsUtils.isEmptys(xueYaDayBean.bloodPressureLowMax + "", "0")));
                            dataBar4.add(Integer.valueOf(TextsUtils.isEmptys(xueYaDayBean.bloodPressureLowMin + "", "0")));

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XueYaDayResultBean>> call, Throwable t) {

            }
        });
        anrizhihuoquueya();

    }

    private void webViewSet(WebView webView, int type) {
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (type == 1) {
            webView.loadUrl("file:///android_asset/product/bloodline.html");
        } else {
            webView.loadUrl("file:///android_asset/product/bloodbar.html");
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
        public int getDatasLine2(int i) {
            return (int) dataLine2.get(i);
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

        @JavascriptInterface
        public int getDatasBar3(int i) {
            return (int) dataBar3.get(i);
        }

        @JavascriptInterface
        public int getDatasBar4(int i) {
            return (int) dataBar4.get(i);
        }
    }


}
