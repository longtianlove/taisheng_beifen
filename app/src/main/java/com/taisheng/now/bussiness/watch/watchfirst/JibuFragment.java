package com.taisheng.now.bussiness.watch.watchfirst;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.GetwatchstepPostBean;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.result.BushuResultBean;
import com.taisheng.now.bussiness.watch.bean.result.GetwatchstepResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import retrofit2.Call;
import retrofit2.Response;

public class JibuFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_jibu, container, false);
        initView(rootView);


        initData();

        return rootView;
    }


    View ll_guijiditu;
//    private ArrayList<Entry> list = new ArrayList<>();  //数据集合
//    private ArrayList<Entry> list_month = new ArrayList<>();

    TextView tv_bushu;
    TextView tv_label;
//    private LineChart mChart;
//    LineChart chart_month;

    void initView(View rootView) {
        tv_bushu = rootView.findViewById(R.id.tv_bushu);
        tv_label = rootView.findViewById(R.id.tv_label);
        ll_guijiditu = rootView.findViewById(R.id.ll_guijiditu);
        ll_guijiditu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryGuijiActivity.class);
                startActivity(intent);
            }
        });
//        mChart = (LineChart) rootView.findViewById(R.id.chart);
//        chart_month = rootView.findViewById(R.id.chart_month);
//        list.clear();
//        for (int i = 0; i < 10; i++) {
//            list.add(new Entry(i, (float) (Math.random() * 80)));
//        }
//
//
//        //直接调用即可
//        LineChartUtils lineChartUtils = new LineChartUtils(list, mChart,"#529FFB","步数");
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
    }


    void initData() {
//        ShishiCollectionBean bean = new ShishiCollectionBean();
//        bean.userId = UserInstance.getInstance().getUid();
//        bean.token = UserInstance.getInstance().getToken();
//        bean.deviceId = WatchInstance.getInstance().deviceId;
////        bean.deviceId = "9613050381";
//
//        ApiUtils.getApiService().getcollection(bean).enqueue(new TaiShengCallback<BaseBean<ShiShiCollecgtionResultBean>>() {
//            @Override
//            public void onSuccess(Response<BaseBean<ShiShiCollecgtionResultBean>> response, BaseBean<ShiShiCollecgtionResultBean> message) {
//                switch (message.code) {
//                    case Constants.HTTP_SUCCESS:
////                        public String watchBpxyHigh;
////                        public String watchBpxyLow;
////                        public String stepNum;
////                        public String watchHeart;
//                        WatchInstance.getInstance().watchBpxyHigh = message.result.watchBpxyHigh;
//                        WatchInstance.getInstance().watchBpxyLow = message.result.watchBpxyLow;
//                        WatchInstance.getInstance().stepNum = message.result.stepNum;
//                        WatchInstance.getInstance().watchHeart = message.result.watchHeart;
//                        tv_bushu.setText(WatchInstance.getInstance().stepNum);
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<ShiShiCollecgtionResultBean>> call, Throwable t) {
//
//            }
//        });


        GetwatchstepPostBean bean = new GetwatchstepPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;

//        获取计步的实时数据
        ApiUtils.getApiService_hasdialog().getwatchstep(bean).enqueue(new TaiShengCallback<BaseBean<GetwatchstepResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetwatchstepResultBean>> response, BaseBean<GetwatchstepResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
//                        public String watchBpxyHigh;
//                        public String watchBpxyLow;
//                        public String stepNum;
//                        public String watchHeart;
//                        WatchInstance.getInstance().watchBpxyHigh = message.result.watchBpxyHigh;
//                        WatchInstance.getInstance().watchBpxyLow = message.result.watchBpxyLow;
//                        WatchInstance.getInstance().stepNum = message.result.stepNum;
//                        WatchInstance.getInstance().watchHeart = message.result.watchHeart;
//                        tv_bushu.setText(WatchInstance.getInstance().stepNum);
                        if (message.result != null) {
                            WatchInstance.getInstance().stepNum = message.result.stepNum + "";
                            tv_label.setVisibility(View.VISIBLE);
                            tv_bushu.setText(WatchInstance.getInstance().stepNum);
                        } else {
                            //todo 数据为空的时候修改UI
                            tv_label.setVisibility(View.GONE);
                            tv_bushu.setText("暂无数据");

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GetwatchstepResultBean>> call, Throwable t) {

            }
        });

//        ApiUtils.getApiService().querythisweekwalk(bean).enqueue(new TaiShengCallback<BaseBean<BushuResultBean>>() {
//
//            @Override
//            public void onSuccess(Response<BaseBean<BushuResultBean>> response, BaseBean<BushuResultBean> message) {
//                switch (message.code) {
//                    case Constants.HTTP_SUCCESS:
//
//                        if (message.result.records != null && message.result.records.size() > 0) {
//
//                            list.clear();
//                            ArrayList<String> days = new ArrayList<>();
//                            for (int i = 0; i < message.result.records.size(); i++) {
//                                list.add(new Entry(i, Integer.parseInt(message.result.records.get(i).stepNum)));
//                                String[] temp = message.result.records.get(i).createTime.split(" ");
//                                String[] temp1 = temp[0].split("-");
//                                days.add(temp1[1] + "-" + temp1[2]);
//
//                            }
//                            //自定义x轴显示
//                            MyXFormatter formatter = new MyXFormatter();
//                            formatter.days = days;
//                            XAxis xAxis = mChart.getXAxis();
//                            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//                            xAxis.setDrawAxisLine(false);
//                            xAxis.setDrawGridLines(false);
//                            //显示个数
////                            xAxis.setLabelCount(days.size());
//                            xAxis.setValueFormatter(formatter);
//                            LineChartUtils lineChartUtils = new LineChartUtils(list, mChart, "#00C8AA", "步数");
//
//                        }
//
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<BushuResultBean>> call, Throwable t) {
//
//            }
//        });


        getMonth();


    }


    void getMonth() {
        ShishiCollectionBean bean = new ShishiCollectionBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
//        获取计步的月统计数据
        ApiUtils.getApiService_hasdialog().querythismonthwalk(bean).enqueue(new TaiShengCallback<BaseBean<BushuResultBean>>() {

            @Override
            public void onSuccess(Response<BaseBean<BushuResultBean>> response, BaseBean<BushuResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:

                        if (message.result.records != null && message.result.records.size() > 0) {

//                            list_month.clear();
//                            ArrayList<String> days = new ArrayList<>();
//                            for (int i = 0; i < message.result.records.size(); i++) {
//                                list_month.add(new Entry(i, Integer.parseInt(message.result.records.get(i).stepNum)));
//                                String[] temp = message.result.records.get(i).createTime.split(" ");
//                                String[] temp1 = temp[0].split("-");
//                                days.add(temp1[1] + "-" + temp1[2]);
//
//                            }
//
//                            //自定义x轴显示
//                            MyXFormatter formatter = new MyXFormatter();
//                            formatter.days = days;
//                            XAxis xAxis = chart_month.getXAxis();
//                            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//                            xAxis.setDrawAxisLine(false);
//                            xAxis.setDrawGridLines(false);
//                            //显示个数
////                            xAxis.setLabelCount(days.size());
//                            xAxis.setValueFormatter(formatter);
//                            LineChartUtils lineChartUtils = new LineChartUtils(list_month, chart_month, "#00C8AA", "步数");
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<BushuResultBean>> call, Throwable t) {

            }
        });

    }


}
