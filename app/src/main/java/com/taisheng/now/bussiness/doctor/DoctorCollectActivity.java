package com.taisheng.now.bussiness.doctor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.CollectListPostBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorCollectListResultBean;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class DoctorCollectActivity extends BaseHActivity {

    @BindView(R.id.lv_doctors)
    TaishengListView lvDoctors;
    @BindView(R.id.ptr_refresh)
    MaterialDesignPtrFrameLayout ptrRefresh;
    private DoctorAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;

    @Override
    public void initView() {
        setContentView(R.layout.activity_doctor_collect);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initData() {


    }

    @Override
    public void addData() {
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        getDoctors_hasdialog();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.physician_collection));
    }

    private void initViews() {
        /**
         * 下拉刷新
         */
        ptrRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                PAGE_SIZE = 10;
                getDoctors();
            }
        });
        madapter = new DoctorAdapter(this);
        lvDoctors.setAdapter(madapter);
        lvDoctors.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });
    }


    private void getDoctors_hasdialog() {
        CollectListPostBean bean = new CollectListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.collectionType = "1";
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().doctorcollectionlist(bean).enqueue(new TaiShengCallback<BaseBean<DoctorCollectListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorCollectListResultBean>> response, BaseBean<DoctorCollectListResultBean> message) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvDoctors.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvDoctors.setHasLoadMore(false);
                                lvDoctors.setLoadAllViewText("暂时只有这么多医生");
                                lvDoctors.setLoadAllFooterVisible(true);
                            } else {
                                lvDoctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvDoctors.setHasLoadMore(false);
                            lvDoctors.setLoadAllViewText("暂时只有这么多医生");
                            lvDoctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorCollectListResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });


    }
    private void getDoctors() {
        CollectListPostBean bean = new CollectListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.collectionType = "1";
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().doctorcollectionlist(bean).enqueue(new TaiShengCallback<BaseBean<DoctorCollectListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorCollectListResultBean>> response, BaseBean<DoctorCollectListResultBean> message) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvDoctors.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvDoctors.setHasLoadMore(false);
                                lvDoctors.setLoadAllViewText("暂时只有这么多医生");
                                lvDoctors.setLoadAllFooterVisible(true);
                            } else {
                                lvDoctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvDoctors.setHasLoadMore(false);
                            lvDoctors.setLoadAllViewText("暂时只有这么多医生");
                            lvDoctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorCollectListResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
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
                    Intent intent = new Intent(DoctorCollectActivity.this, DoctorDetailActivity.class);
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
            util.tv_onlineStatus.setVisibility(View.GONE);
            if ("1".equals(bean.onlineStatus)) {
                util.tv_onlineStatus.setText("在线");
                util.tv_onlineStatus.setBackgroundResource(R.drawable.bg_online);
            } else {
                util.tv_onlineStatus.setText("忙碌");
                util.tv_onlineStatus.setBackgroundResource(R.drawable.bg_online2);
            }
            SpanUtil.create()
//                            .addSection(String.valueOf(messageWaitTime) + "S"+"重新发送")  //添加带前景色的文字片段
                    .addForeColorSection("解答", ContextCompat.getColor(DoctorCollectActivity.this, R.color.color666666)) //设置相对字体
                    .addForeColorSection(bean.servicesNum, ContextCompat.getColor(DoctorCollectActivity.this, R.color.color00c8aa)) //设置相对字体
                    .addForeColorSection("次", ContextCompat.getColor(DoctorCollectActivity.this, R.color.color666666)) //设置相对字体
                    .showIn(util.tv_times); //显示到控件TextView中

            util.tv_title.setText(bean.title);
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

}
