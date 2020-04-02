package com.taisheng.now.bussiness.watch.watchfirst;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import com.taisheng.now.bussiness.watch.bean.result.GetheartrateResultBean;
import com.taisheng.now.bussiness.watch.bean.result.ShiShiCollecgtionResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XinlvAnriqiResultBean;
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

public class XinlvFragment extends BaseFragment {


    ListView lv_data;
    DataAdapter madapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_xinlv, container, false);
        initView(rootView);


        return rootView;
    }

//    private ArrayList<Entry> list = new ArrayList<>();  //数据集合


    TextView tv_xinlv;
    TextView tv_label;
    //    private LineChart mChart;
    View ll_health_left;
    View ll_health_right;
    TextView tv_date;

    void initView(View rootView) {
        tv_xinlv = rootView.findViewById(R.id.tv_xinlv);
        tv_label=rootView.findViewById(R.id.tv_label);
//       mChart = (LineChart) rootView.findViewById(R.id.chart);
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
        madapter=new DataAdapter(getActivity());
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
        ApiUtils.getApiService().obtainHeartList(bean).enqueue(new TaiShengCallback<BaseBean<ArrayList<XinlvAnriqiResultBean>>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArrayList<XinlvAnriqiResultBean>>> response, BaseBean<ArrayList<XinlvAnriqiResultBean>> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        madapter.mData=message.result;
                        madapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ArrayList<XinlvAnriqiResultBean>>> call, Throwable t) {

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
//                        WatchInstance.getInstance().watchBpxyHigh = message.result.watchBpxyHigh;
//                        WatchInstance.getInstance().watchBpxyLow = message.result.watchBpxyLow;
//                        WatchInstance.getInstance().stepNum = message.result.stepNum;
//                        WatchInstance.getInstance().watchHeart = message.result.watchHeart;
//                        tv_xinlv.setText(WatchInstance.getInstance().watchHeart);
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<ShiShiCollecgtionResultBean>> call, Throwable t) {
//
//            }
//        });
        GetheartratePostBean bean = new GetheartratePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;


        ApiUtils.getApiService().getheartrate(bean).enqueue(new TaiShengCallback<BaseBean<GetheartrateResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetheartrateResultBean>> response, BaseBean<GetheartrateResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
//                        WatchInstance.getInstance().watchBpxyHigh = message.result.watchBpxyHigh;
//                        WatchInstance.getInstance().watchBpxyLow = message.result.watchBpxyLow;
//                        WatchInstance.getInstance().stepNum = message.result.stepNum;
//                        WatchInstance.getInstance().watchHeart = message.result.watchHeart;
//                        tv_xinlv.setText(WatchInstance.getInstance().watchHeart);
                        if(message.result!=null){
                            WatchInstance.getInstance().watchHeart = message.result.heartNum+"";
                            tv_label.setVisibility(View.VISIBLE);
                            tv_xinlv.setText(WatchInstance.getInstance().watchHeart);
                        }else{
                            tv_xinlv.setText("暂无数据");
                            tv_label.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GetheartrateResultBean>> call, Throwable t) {

            }
        });


//        anrizhihuoquueya();

//        ApiUtils.getApiService().querythedayheart(bean).enqueue(new TaiShengCallback<BaseBean<XinLvResultBean>>() {
//            @Override
//            public void onSuccess(Response<BaseBean<XinLvResultBean>> response, BaseBean<XinLvResultBean> message) {
//                switch (message.code) {
//                    case Constants.HTTP_SUCCESS:
//                        if (message.result .records!= null && message.result.records.size() > 0) {
//                            list.clear();
//                            ArrayList<String> days = new ArrayList<>();
//                            for (int i = 0; i < message.result.records.size(); i++) {
//                                list.add(new Entry(i, (int)message.result.records.get(i).heartNum));
//                                String[] temp=message.result.records.get(i).createTime.split(" ");
//                                days.add(temp[1]);
//
//                            }
//                            //自定义x轴显示
//                            MyXFormatter formatter = new MyXFormatter();
//                            formatter.days=days;
//                            XAxis xAxis = mChart.getXAxis();
//                            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//                            xAxis.setDrawAxisLine(false);
//                            xAxis.setDrawGridLines(false);
//                            //显示个数
////                            xAxis.setLabelCount(days.size());
//                            xAxis.setValueFormatter(formatter);
//
//                            LineChartUtils lineChartUtils = new LineChartUtils(list, mChart, "#FF2C58", "心率");
//
//
//                        }
//
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<XinLvResultBean>> call, Throwable t) {
//
//            }
//        });
    }


    class DataAdapter extends BaseAdapter {

        public Context mcontext;

        List<XinlvAnriqiResultBean> mData = new ArrayList<XinlvAnriqiResultBean>();

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
                util = new DataAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_xinlv_data, null);
                util.tv_xinlv=convertView.findViewById(R.id.tv_xinlv);
                util.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(util);
            } else {
                util = (DataAdapter.Util) convertView.getTag();
            }
            XinlvAnriqiResultBean bean=mData.get(position);

            util.tv_xinlv.setText(bean.heartNum+"");
            util.tv_time.setText(bean.createTime);

            return convertView;
        }


        class Util {
            TextView tv_time;
            TextView tv_xinlv;

        }
    }


}
