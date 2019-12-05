package com.taisheng.now.bussiness.watch.watchyujing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.doctor.DoctorFragment;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.YuJingListPostBean;
import com.taisheng.now.bussiness.watch.bean.result.YujingResultBean;
import com.taisheng.now.bussiness.watch.bean.result.Yujingbean;
import com.taisheng.now.bussiness.watch.watchfirst.JibuFragment;
import com.taisheng.now.bussiness.watch.watchfirst.XinlvFragment;
import com.taisheng.now.bussiness.watch.watchfirst.XueyaFragment;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

public class WatchYujingFragment extends BaseFragment {






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
        iv_back=rootView.findViewById(R.id.iv_back);
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


    }

    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void initData() {
        YuJingListPostBean bean=new YuJingListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.clientId= WatchInstance.getInstance().deviceId;
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
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_doctors.setHasLoadMore(false);
                                lv_doctors.setLoadAllViewText("暂时只有这么多医生");
                                lv_doctors.setLoadAllFooterVisible(true);
                            } else {
                                lv_doctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_doctors.setHasLoadMore(false);
                            lv_doctors.setLoadAllViewText("暂时只有这么多医生");
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
                convertView = inflater.inflate(R.layout.item_doctors, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                    convertView.setTag(util);
            } else {
                util = (DoctorAdapter.Util) convertView.getTag();
            }
            Yujingbean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mActivity, DoctorDetailActivity.class);
//                    intent.putExtra("id", bean.id);
//                    intent.putExtra("nickName", bean.nickName);
//                    intent.putExtra("title", bean.title);
//                    intent.putExtra("fromMedicineTime", bean.fromMedicineTime);
//                    intent.putExtra("jobIntroduction", bean.jobIntroduction);
//                    intent.putExtra("score", bean.score);
//                    intent.putExtra("goodDiseases", bean.goodDiseases);
//                    startActivity(intent);
                }
            });



            return convertView;
        }


        class Util {
            View ll_all;

        }
    }

    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
