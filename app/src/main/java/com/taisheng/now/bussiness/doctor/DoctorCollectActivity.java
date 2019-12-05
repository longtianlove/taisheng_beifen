package com.taisheng.now.bussiness.doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.CollectListPostBean;
import com.taisheng.now.bussiness.bean.result.ArticleCollectListResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorCollectListResultBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class DoctorCollectActivity extends BaseActivity {
    View iv_back;
    MaterialDesignPtrFrameLayout ptr_refresh;
    TaishengListView lv_doctors;
    DoctorAdapter madapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_collect);
        initView();
        initData();
    }
    void initView(){
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ptr_refresh = (MaterialDesignPtrFrameLayout)findViewById(R.id.ptr_refresh);
        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();

            }
        });
        lv_doctors = (TaishengListView)findViewById(R.id.lv_doctors);
        madapter = new DoctorAdapter(this);
        lv_doctors.setAdapter(madapter);
        lv_doctors.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });
    }
    void initData() {
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        getDoctors();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    int PAGE_NO = 1;
    int PAGE_SIZE = 10;
    String nickName;

    void getDoctors() {
        CollectListPostBean bean = new CollectListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.collectionType="1";
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().doctorcollectionlist(bean).enqueue(new TaiShengCallback<BaseBean<DoctorCollectListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorCollectListResultBean>> response, BaseBean<DoctorCollectListResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
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
            public void onFail(Call<BaseBean<DoctorCollectListResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });


    }

    class DoctorAdapter extends BaseAdapter {

        public Context mcontext;

        List<DoctorBean> mData = new ArrayList<DoctorBean>();

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
                util.sdv_header = (SimpleDraweeView) convertView.findViewById(R.id.sdv_header);
                util.tv_doctor_name = (TextView) convertView.findViewById(R.id.tv_doctor_name);
                util.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                util.tv_onlineStatus= (TextView) convertView.findViewById(R.id.tv_onlineStatus);
                util.tv_times = (TextView) convertView.findViewById(R.id.tv_times);
                util.dlwl_doctor_label = (DoctorLabelWrapLayout) convertView.findViewById(R.id.dlwl_doctor_label);
                util.scorestar = (ScoreStar) convertView.findViewById(R.id.scorestar);
                util.btn_zixun = (TextView) convertView.findViewById(R.id.btn_zixun);
                convertView.setTag(util);
            } else {
                util = (DoctorAdapter.Util) convertView.getTag();
            }
            DoctorBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DoctorCollectActivity.this, DoctorDetailActivity.class);
                    intent.putExtra("id", bean.id);
                    intent.putExtra("nickName",bean.nickName);
                    intent.putExtra("title",bean.title);
                    intent.putExtra("fromMedicineTime",bean.fromMedicineTime);
                    intent.putExtra("jobIntroduction",bean.jobIntroduction);
                    intent.putExtra("score",bean.score);
                    intent.putExtra("goodDiseases",bean.goodDiseases);
                    startActivity(intent);
                }
            });
            if(bean.avatar!=null) {
                Uri uri = Uri.parse(bean.avatar);
                util.sdv_header.setImageURI(uri);
            }
            util.tv_doctor_name.setText(bean.nickName);

            if("1".equals(bean.onlineStatus)){
                util.tv_onlineStatus.setText("在线");
                util.tv_onlineStatus.setTextColor(Color.parseColor("#ff0dd500"));
            }else{
                util.tv_onlineStatus.setText("忙碌");
                util.tv_onlineStatus.setTextColor(Color.parseColor("#ffff554e"));
            }
            util.tv_title.setText(bean.title);
            util.tv_times.setText(bean.servicesNum);
            if (bean.goodDiseases != null) {
                String[] doctorlabel = bean.goodDiseases.split(",");
                util.dlwl_doctor_label.setData(doctorlabel, DoctorCollectActivity.this, 10, 5, 1, 5, 1, 4, 4, 4, 8);

            }

            if (bean.score != null) {
                util.scorestar.setScore(bean.score);
            }
            util.btn_zixun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DoctorCollectActivity.this, DoctorDetailActivity.class);
                    intent.putExtra("id", bean.id);
                    intent.putExtra("nickName",bean.nickName);
                    intent.putExtra("title",bean.title);
                    intent.putExtra("fromMedicineTime",bean.fromMedicineTime);
                    intent.putExtra("jobIntroduction",bean.jobIntroduction);
                    intent.putExtra("score",bean.score);
                    intent.putExtra("goodDiseases",bean.goodDiseases);
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

}
