package com.taisheng.now.bussiness.doctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.bean.post.getListDoctorTypePostBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorsResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;
import com.th.j.commonlibrary.utils.SpanUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class DoctorsFenleiFragment extends BaseFragment {


    MaterialDesignPtrFrameLayout ptr_refresh;

    TaishengListView lv_doctors;
    DoctorAdapter madapter;
    private List<DoctorBean> data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_doctorfenlei, container, false);
        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();

        return rootView;
    }

    void initView(View rootView) {
        data=new ArrayList();
        ptr_refresh = (MaterialDesignPtrFrameLayout) rootView.findViewById(R.id.ptr_refresh);
        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                if (data != null) {
                    data.clear();
                }
                getDoctors();

            }
        });
        lv_doctors = (TaishengListView) rootView.findViewById(R.id.lv_doctors);
        madapter = new DoctorAdapter(mActivity);
        lv_doctors.setAdapter(madapter);
        lv_doctors.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                PAGE_NO ++;
                getDoctors();
            }
        });
    }

    void initData() {
        PAGE_NO = 1;
        if (data != null) {
            data.clear();
        }
        getDoctors_first();
    }

    int PAGE_NO = 1;
    int PAGE_SIZE = 10;
    public String type;


    void getDoctors_first() {
        getListDoctorTypePostBean bean = new getListDoctorTypePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.type = type;
        DialogUtil.showProgress(mActivity, "");
        ApiUtils.getApiService_hasdialog().getListDoctor(bean).enqueue(new TaiShengCallback<BaseBean<DoctorsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorsResultBean>> response, BaseBean<DoctorsResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_doctors.setLoading(false);
                            data.addAll( message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_doctors.setHasLoadMore(false);
                                lv_doctors.setLoadAllViewText("暂时只有这么多医生");
                                lv_doctors.setLoadAllFooterVisible(true);
                            } else {
                                lv_doctors.setHasLoadMore(true);
                            }
                            madapter.setmData(data);
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
            public void onFail(Call<BaseBean<DoctorsResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });


    }


    void getDoctors() {
        getListDoctorTypePostBean bean = new getListDoctorTypePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.type = type;
        DialogUtil.showProgress(mActivity, "");
        ApiUtils.getApiService().getListDoctor(bean).enqueue(new TaiShengCallback<BaseBean<DoctorsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorsResultBean>> response, BaseBean<DoctorsResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_doctors.setLoading(false);
                            data.addAll( message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_doctors.setHasLoadMore(false);
                                lv_doctors.setLoadAllViewText("暂时只有这么多医生");
                                lv_doctors.setLoadAllFooterVisible(true);
                            } else {
                                lv_doctors.setHasLoadMore(true);
                            }
                            madapter.setmData(data);
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
            public void onFail(Call<BaseBean<DoctorsResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });


    }

    class DoctorAdapter extends BaseAdapter {

        private Context mcontext;

        private List<DoctorBean> mData ;

        public DoctorAdapter(Context context) {
            this.mcontext = context;
        }

        public void setmData(List<DoctorBean> mData) {
            this.mData = mData;
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
            Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_doctors, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_header = (SimpleDraweeView) convertView.findViewById(R.id.sdv_header);
                util.tv_doctor_name = (TextView) convertView.findViewById(R.id.tv_doctor_name);
                util.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                util.tv_onlineStatus = (TextView) convertView.findViewById(R.id.tv_onlineStatus);
                util.tv_times = (TextView) convertView.findViewById(R.id.tv_times);
                util.dlwl_doctor_label = (DoctorLabelWrapLayout) convertView.findViewById(R.id.dlwl_doctor_label);
                util.scorestar = (ScoreStar) convertView.findViewById(R.id.scorestar);
                util.btn_zixun = (TextView) convertView.findViewById(R.id.btn_zixun);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            DoctorBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DoctorDetailActivity.class);
                    intent.putExtra("id", bean.id);
                    intent.putExtra("nickName", bean.nickName);
                    intent.putExtra("title", bean.title);
                    intent.putExtra("fromMedicineTime", bean.fromMedicineTime);
                    intent.putExtra("jobIntroduction", bean.jobIntroduction);
                    intent.putExtra("score", bean.score);
                    intent.putExtra("goodDiseases", bean.goodDiseases);
                    startActivity(intent);
                }
            });
            if (bean.avatar != null) {
                Uri uri = Uri.parse(bean.avatar);
                util.sdv_header.setImageURI(uri);
            }
            util.tv_doctor_name.setText(bean.nickName);
            util.tv_title.setText(bean.title);
            if ("1".equals(bean.onlineStatus)) {
                util.tv_onlineStatus.setText("在线");
                util.tv_onlineStatus.setBackgroundResource(R.drawable.bg_online);
            } else {
                util.tv_onlineStatus.setText("忙碌");
                util.tv_onlineStatus.setBackgroundResource(R.drawable.bg_online2);
            }
            SpanUtil.create()
//                            .addSection(String.valueOf(messageWaitTime) + "S"+"重新发送")  //添加带前景色的文字片段
                    .addForeColorSection("解答", ContextCompat.getColor(getActivity(), R.color.color666666)) //设置相对字体
                    .addForeColorSection(bean.servicesNum, ContextCompat.getColor(getActivity(), R.color.color00c8aa)) //设置相对字体
                    .addForeColorSection("次", ContextCompat.getColor(getActivity(), R.color.color666666)) //设置相对字体
                    .showIn(util.tv_times); //显示到控件TextView中
            if (bean.goodDiseases != null) {
                String[] doctorlabel = bean.goodDiseases.split(",");
                util.dlwl_doctor_label.setData(doctorlabel, mActivity, 10, 5, 1, 5, 1, 4, 4, 4, 8);

            }

            if (bean.score != null) {
                util.scorestar.setScore(bean.score);
            }
            util.btn_zixun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DoctorDetailActivity.class);
                    intent.putExtra("id", bean.id);
                    intent.putExtra("nickName", bean.nickName);
                    intent.putExtra("title", bean.title);
                    intent.putExtra("fromMedicineTime", bean.fromMedicineTime);
                    intent.putExtra("jobIntroduction", bean.jobIntroduction);
                    intent.putExtra("score", bean.score);
                    intent.putExtra("goodDiseases", bean.goodDiseases);
                    startActivity(intent);
                }
            });

            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_header;
            TextView tv_doctor_name;
            TextView tv_onlineStatus;
            TextView tv_title;
            TextView tv_times;
            DoctorLabelWrapLayout dlwl_doctor_label;
            ScoreStar scorestar;
            TextView btn_zixun;
        }
    }


    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
