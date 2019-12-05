package com.taisheng.now.bussiness.watch.watchfirst;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.result.ShiShiCollecgtionResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XueYaDayResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.veken.chartview.bean.ChartBean;
import com.veken.chartview.drawtype.DrawBgType;
import com.veken.chartview.drawtype.DrawConnectLineType;
import com.veken.chartview.drawtype.DrawLineType;
import com.veken.chartview.view.LineChartView;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class XueyaFragment extends BaseFragment {

    TextView tv_time;
    TextView tv_gaoya;
    TextView tv_diya;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_xueya, container, false);
        initView(rootView);


        initData();

        return rootView;
    }


    private ArrayList<Entry> list = new ArrayList<>();  //数据集合
    private ArrayList<Entry> list1 = new ArrayList<>();  //数据集合


    private LineChart mChart;

    void initView(View rootView) {
        tv_time = rootView.findViewById(R.id.tv_time);
        tv_gaoya = rootView.findViewById(R.id.tv_gaoya);
        tv_diya = rootView.findViewById(R.id.tv_diya);

        this.mChart = (LineChart) rootView.findViewById(R.id.chart);


    }


    void initData() {

        ShishiCollectionBean bean = new ShishiCollectionBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.clientId = WatchInstance.getInstance().deviceId;
//        bean.clientId = "9613050381";

        ApiUtils.getApiService().getcollection(bean).enqueue(new TaiShengCallback<BaseBean<ShiShiCollecgtionResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ShiShiCollecgtionResultBean>> response, BaseBean<ShiShiCollecgtionResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
//                        public String watchBpxyHigh;
//                        public String watchBpxyLow;
//                        public String stepNum;
//                        public String watchHeart;
                        WatchInstance.getInstance().watchBpxyHigh = message.result.watchBpxyHigh;
                        WatchInstance.getInstance().watchBpxyLow = message.result.watchBpxyLow;
                        WatchInstance.getInstance().stepNum = message.result.stepNum;
                        WatchInstance.getInstance().watchHeart = message.result.watchHeart;
                        tv_time.setText(message.result.updateTime.toString());
                        tv_gaoya.setText(message.result.watchBpxyHigh);
                        tv_diya.setText(message.result.watchBpxyLow);

                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ShiShiCollecgtionResultBean>> call, Throwable t) {

            }
        });


        ApiUtils.getApiService().querythedaybpxy(bean).enqueue(new TaiShengCallback<BaseBean<XueYaDayResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XueYaDayResultBean>> response, BaseBean<XueYaDayResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null && message.result.records.size() > 0) {
                            list.clear();
                            list1.clear();
                            ArrayList<String> days = new ArrayList<>();
                            for (int i = 0; i < message.result.records.size(); i++) {
                                list.add(new Entry(i, message.result.records.get(i).bpxyHigh));
                                list1.add(new Entry(i, message.result.records.get(i).bpxyLow));
                                String[] temp=message.result.records.get(i).createTime.split(" ");
                                days.add(temp[1]);

                            }


                            //自定义x轴显示
                            MyXFormatter formatter = new MyXFormatter();
                            formatter.days=days;
                            XAxis xAxis = mChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setDrawGridLines(false);
                            //显示个数
//                            xAxis.setLabelCount(days.size());
                            xAxis.setValueFormatter(formatter);


                            //直接调用即可
                            LineChartUtils lineChartUtils = new LineChartUtils(list, list1, mChart);

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XueYaDayResultBean>> call, Throwable t) {

            }
        });
    }


}
