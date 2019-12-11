package com.taisheng.now.bussiness.watch.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.YuJingListPostBean;
import com.taisheng.now.bussiness.watch.bean.post.YujingxinxiSetYiduPostBean;
import com.taisheng.now.bussiness.watch.bean.result.YujingResultBean;
import com.taisheng.now.bussiness.watch.bean.result.Yujingbean;
import com.taisheng.now.bussiness.watch.watchme.WatchMeYujingxinxiXiangqingActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

public class WatchLocationFragment extends BaseFragment {


    View iv_back;


    MaterialDesignPtrFrameLayout ptr_refresh;

    TaishengListView lv_doctors;
    DoctorAdapter madapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_watchyujing, container, false);
        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();


        return rootView;
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_watchfirst);
//        initView();
//
//
////        EventBus.getDefault().register(this);
//        initData();
//    }

    void initView(View rootView) {
        iv_back = rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        ptr_refresh = (MaterialDesignPtrFrameLayout) rootView.findViewById(R.id.ptr_refresh);
        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                initData();

            }
        });
        lv_doctors = (TaishengListView) rootView.findViewById(R.id.lv_doctors);
        madapter = new DoctorAdapter(mActivity);
        lv_doctors.setAdapter(madapter);
        lv_doctors.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                initData();
            }
        });

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0, sticky = true)
    public void getYujingxinxi(EventManage.getYujingxinxi event) {
        PAGE_NO = 1;
        initData();

    }

    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void initData() {
        YuJingListPostBean bean = new YuJingListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.clientId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().getWatchWarningAll(bean).enqueue(new TaiShengCallback<BaseBean<YujingResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<YujingResultBean>> response, BaseBean<YujingResultBean> message) {
                ptr_refresh.refreshComplete();

                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_doctors.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                                Yujingbean bean1=message.result.records.get(0);
                                EventManage.tongzhiWeidu event=new EventManage.tongzhiWeidu();
                                event.weidu=bean1.status;
                               EventBus.getDefault().post(event);
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_doctors.setHasLoadMore(false);
                                lv_doctors.setLoadAllViewText("暂时只有这么多消息");
                                lv_doctors.setLoadAllFooterVisible(true);
                            } else {
                                lv_doctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_doctors.setHasLoadMore(false);
                            lv_doctors.setLoadAllViewText("暂时只有这么多消息");
                            lv_doctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<YujingResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
            }
        });

    }

    class DoctorAdapter extends BaseAdapter {

        public Context mcontext;

        List<Yujingbean> mData = new ArrayList<Yujingbean>();

        public DoctorAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            if (mData == null) {
                return 0;
            } else {
                return mData.size();
            }
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
            DoctorAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new DoctorAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_yujingxiaoxi, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_jiancetongzhi = convertView.findViewById(R.id.tv_jiancetongzhi);
                util.tv_time = convertView.findViewById(R.id.tv_time);
                util.tv_message = convertView.findViewById(R.id.tv_message);
                util.iv_weidu = convertView.findViewById(R.id.iv_weidu);
                convertView.setTag(util);
            } else {
                util = (DoctorAdapter.Util) convertView.getTag();
            }
            Yujingbean bean = mData.get(position);
            Util finalUtil = util;
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    YujingxinxiSetYiduPostBean bean1 = new YujingxinxiSetYiduPostBean();
                    bean1.userId = UserInstance.getInstance().getUid();
                    bean1.token = UserInstance.getInstance().getToken();
                    bean1.id = bean.id;
                    ApiUtils.getApiService().watchWarningupdateBykey(bean1).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    finalUtil.iv_weidu.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(getActivity(), WatchMeYujingxinxiXiangqingActivity.class);
                                    intent.putExtra("warningType", bean.warningContent);
                                    intent.putExtra("message", bean.warningContent);
                                    intent.putExtra("createTime", bean.createTime);
                                    startActivity(intent);
                                    PAGE_NO = 1;
                                    initData();
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });


                }
            });
            util.tv_time.setText(bean.createTime);
            util.tv_message.setText(bean.warningContent);
            String temp;
            switch (bean.warningType) {
                case "heart":
                    temp = "心率信息通知";
                    break;
                case "bpxy":
                    temp = "血压信息通知";
                    break;
                case "bphrt":
                    temp = "心率信息通知";
                    break;
                case "LK":
                    temp = "低电量通知";
                    break;
                default:
                    temp = "监测信息通知";
                    break;
            }
            util.tv_jiancetongzhi.setText(temp);

            if ("0".equals(bean.status)) {
                util.iv_weidu.setVisibility(View.INVISIBLE);
            } else {
                util.iv_weidu.setVisibility(View.VISIBLE);
            }
            return convertView;
        }


        class Util {
            View ll_all;
            TextView tv_jiancetongzhi;
            TextView tv_time;
            TextView tv_message;
            View iv_weidu;

        }
    }


    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
