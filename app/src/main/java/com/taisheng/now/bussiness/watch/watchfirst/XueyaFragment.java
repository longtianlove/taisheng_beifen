package com.taisheng.now.bussiness.watch.watchfirst;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.GetbloodpressurePostBean;
import com.taisheng.now.bussiness.watch.bean.post.ObtainBpxyHeartStepListDTOPostBean;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.result.GetbloodpressureResultBean;
import com.taisheng.now.bussiness.watch.bean.result.ShiShiCollecgtionResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XueyaResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class XueyaFragment extends BaseFragment {

    TextView tv_time;
    TextView tv_gaoya;
    TextView tv_diya;
    ListView lv_data;
    DataAdapter madapter;

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


    //    private LineChart mChart;
    View ll_health_left;
    View ll_health_right;
    TextView tv_date;

    void initView(View rootView) {
        tv_time = rootView.findViewById(R.id.tv_time);
        tv_gaoya = rootView.findViewById(R.id.tv_gaoya);
        tv_diya = rootView.findViewById(R.id.tv_diya);

//        this.mChart = (LineChart) rootView.findViewById(R.id.chart);
        ll_health_left = rootView.findViewById(R.id.ll_health_left);
        ll_health_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedData(-1);
            }
        });
        ll_health_right = rootView.findViewById(R.id.ll_health_right);
        ll_health_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedData(1);
            }
        });

        tv_date = (TextView) rootView.findViewById(R.id.tv_date);
        lv_data = rootView.findViewById(R.id.lv_data);
        madapter = new DataAdapter(getActivity());
        lv_data.setAdapter(madapter);

    }

    @Override
    public void onStart() {
        super.onStart();
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


        if (start == 0) {
            ll_health_left.setVisibility(View.INVISIBLE);
            ll_health_right.setVisibility(View.INVISIBLE);
        } else {
            ll_health_left.setVisibility(View.VISIBLE);
            ll_health_right.setVisibility(View.INVISIBLE);
        }
    }


    //更新整天的信息
    public void updateSelectedData(int section) {

        beforeDayNum = beforeDayNum + section;
        if ((start < beforeDayNum) && (beforeDayNum < 0)) {
            ll_health_left.setVisibility(View.VISIBLE);
            ll_health_right.setVisibility(View.VISIBLE);
        } else if (beforeDayNum == 0) {
            ll_health_left.setVisibility(View.VISIBLE);
            ll_health_right.setVisibility(View.INVISIBLE);
        } else if (beforeDayNum == start) {
            ll_health_left.setVisibility(View.INVISIBLE);
            ll_health_right.setVisibility(View.VISIBLE);
        }
        if (start == 0) {
            ll_health_left.setVisibility(View.INVISIBLE);
            ll_health_right.setVisibility(View.INVISIBLE);
        }
        anrizhihuoquueya();

    }


    Date today = new Date();
    Date nowshow = new Date();
    int beforeDayNum = 0;
    int start = 0;

    void anrizhihuoquueya() {
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
        ApiUtils.getApiService().obtainBpxyList(bean).enqueue(new TaiShengCallback<BaseBean<ArrayList<XueyaResultBean>>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArrayList<XueyaResultBean>>> response, BaseBean<ArrayList<XueyaResultBean>> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        madapter.mData = message.result;
                        madapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArrayList<XueyaResultBean>>> call, Throwable t) {

            }
        });


//        if (beforeDayNum == -1) {
//            tv_date.setText(dateString + "(昨天)");
//        } else {
//            tv_date.setText(dateString);
//        }
        tv_date.setText(dateString);
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
//                        tv_time.setText(message.result.updateTime.toString());
//                        tv_gaoya.setText(message.result.watchBpxyHigh);
//                        tv_diya.setText(message.result.watchBpxyLow);
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<ShiShiCollecgtionResultBean>> call, Throwable t) {
//
//            }
//        });
        GetbloodpressurePostBean bean = new GetbloodpressurePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        bean.deviceId = "359193978994051";

        ApiUtils.getApiService().getbloodpressure(bean).enqueue(new TaiShengCallback<BaseBean<GetbloodpressureResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetbloodpressureResultBean>> response, BaseBean<GetbloodpressureResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
//                        WatchInstance.getInstance().watchBpxyHigh = message.result.watchBpxyHigh;
//                        WatchInstance.getInstance().watchBpxyLow = message.result.watchBpxyLow;
//                        WatchInstance.getInstance().stepNum = message.result.stepNum;
//                        WatchInstance.getInstance().watchHeart = message.result.watchHeart;
//                        tv_time.setText(message.result.updateTime.toString());
//                        tv_gaoya.setText(message.result.watchBpxyHigh);
//                        tv_diya.setText(message.result.watchBpxyLow);
                        if (message.result != null) {
                            WatchInstance.getInstance().watchBpxyHigh = message.result.bloodPressureHigh + "";
                            WatchInstance.getInstance().watchBpxyLow = message.result.bloodPressureLow + "";
                            tv_time.setVisibility(View.VISIBLE);
                            tv_time.setText(message.result.createTime.toString());
                            tv_gaoya.setText(message.result.bloodPressureHigh+"");
                            tv_diya.setText(message.result.bloodPressureLow+"");
                        } else {
                            tv_time.setVisibility(View.GONE);
                            tv_gaoya.setText("暂无数据");
                            tv_diya.setText("暂无数据");
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GetbloodpressureResultBean>> call, Throwable t) {

            }
        });
        anrizhihuoquueya();

//        ApiUtils.getApiService().querythedaybpxy(bean).enqueue(new TaiShengCallback<BaseBean<XueYaDayResultBean>>() {
//            @Override
//            public void onSuccess(Response<BaseBean<XueYaDayResultBean>> response, BaseBean<XueYaDayResultBean> message) {
//                switch (message.code) {
//                    case Constants.HTTP_SUCCESS:
//                        if (message.result != null && message.result.records.size() > 0) {
////                            list.clear();
////                            list1.clear();
////                            ArrayList<String> days = new ArrayList<>();
////                            for (int i = 0; i < message.result.records.size(); i++) {
////                                list.add(new Entry(i,message.result.records.get(i).bpxyHigh));
////                                list1.add(new Entry(i, message.result.records.get(i).bpxyLow));
////                                String[] temp=message.result.records.get(i).createTime.split(" ");
////                                days.add(temp[1]);
////
////                            }
////
////
////                            //自定义x轴显示
////                            MyXFormatter formatter = new MyXFormatter();
////                            formatter.days=days;
////                            XAxis xAxis = mChart.getXAxis();
////                            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
////                            xAxis.setDrawAxisLine(false);
////                            xAxis.setDrawGridLines(false);
////                            //显示个数
//////                            xAxis.setLabelCount(days.size());
////                            xAxis.setValueFormatter(formatter);
////
////
////                            //直接调用即可
////                            LineChartUtils lineChartUtils = new LineChartUtils(list, list1, mChart);
//
//                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<XueYaDayResultBean>> call, Throwable t) {
//
//            }
//        });
    }


    class DataAdapter extends BaseAdapter {

        public Context mcontext;

        List<XueyaResultBean> mData = new ArrayList<XueyaResultBean>();

        public DataAdapter(Context context) {
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
            Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_xueya_data, null);
                util.tvgaoya = convertView.findViewById(R.id.tv_gaoya);
                util.tvdiya = convertView.findViewById(R.id.tv_diya);
                util.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            XueyaResultBean bean = mData.get(position);
            util.tvgaoya.setText(bean.bpxyHigh + "");
            util.tvdiya.setText(bean.bpxyLow + "");
            util.tv_time.setText(bean.createTime);

            return convertView;
        }


        class Util {
            TextView tvgaoya;
            TextView tvdiya;
            TextView tv_time;

        }
    }


}
