package com.taisheng.now.bussiness.me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.MyPingjiaBean;
import com.taisheng.now.bussiness.bean.result.MyPingjiaResultBean;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.StarGrade;
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

public class MyPingjiaActivity extends BaseActivity {
    View iv_back;
    MaterialDesignPtrFrameLayout ptr_refresh;
    TaishengListView lv_mypingluns;
    MyPingjiaAdapter madapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pingjia);
        initView();
        initData();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
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
        lv_mypingluns = (TaishengListView) findViewById(R.id.lv_mypingluns);
        madapter = new MyPingjiaAdapter(this);
        lv_mypingluns.setAdapter(madapter);
        lv_mypingluns.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getMyPingjias();
            }
        });

    }

    void initData() {

         PAGE_NO = 1;
         PAGE_SIZE = 10;
        getMyPingjias();
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getMyPingjias() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().myDoctorScores(bean).enqueue(new TaiShengCallback<BaseBean<MyPingjiaResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<MyPingjiaResultBean>> response, BaseBean<MyPingjiaResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_mypingluns.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_mypingluns.setHasLoadMore(false);
                                lv_mypingluns.setLoadAllViewText("暂时只有这么多评论");
                                lv_mypingluns.setLoadAllFooterVisible(true);
                            } else {
                                lv_mypingluns.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_mypingluns.setHasLoadMore(false);
                            lv_mypingluns.setLoadAllViewText("暂时只有这么多评论");
                            lv_mypingluns.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<MyPingjiaResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });
    }


    class MyPingjiaAdapter extends BaseAdapter {

        public Context mcontext;

        List<MyPingjiaBean> mData = new ArrayList<MyPingjiaBean>();

        public MyPingjiaAdapter(Context context) {
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
            MyPingjiaAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new MyPingjiaAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_mypingjialist, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.starGrade = (StarGrade) convertView.findViewById(R.id.starGrade);
                util.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                util.sdv_header = (SimpleDraweeView) convertView.findViewById(R.id.sdv_header);
                util.tv_doctor_name = (TextView) convertView.findViewById(R.id.tv_doctor_name);
                util.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                util.tv_times = (TextView) convertView.findViewById(R.id.tv_times);
                util.dlwl_doctor_label = (DoctorLabelWrapLayout) convertView.findViewById(R.id.dlwl_doctor_label);
                util.scorestar = (ScoreStar) convertView.findViewById(R.id.scorestar);
                util.btn_zixun = (TextView) convertView.findViewById(R.id.btn_zixun);
                util.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(util);
            } else {
                util = (MyPingjiaAdapter.Util) convertView.getTag();
            }
            MyPingjiaBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyPingjiaActivity.this, DoctorDetailActivity.class);
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

            try {
                util.starGrade.setScore(Float.parseFloat(bean.consultationScore));
            } catch (Exception e) {
                Log.e("Float.parseFloat", e.getMessage());
            }

            util.tv_content.setText(bean.content);
            if (bean.avatar != null) {
                Uri uri = Uri.parse(bean.avatar);
                util.sdv_header.setImageURI(uri);
            }
            util.tv_doctor_name.setText(bean.nickName);
            util.tv_title.setText(bean.title);
            util.tv_times.setText(bean.answerNum);
            if (bean.goodDiseases != null) {
                String[] doctorlabel = bean.goodDiseases.split(",");
                util.dlwl_doctor_label.setData(doctorlabel, MyPingjiaActivity.this, 10, 5, 1, 5, 1, 4, 4, 4, 8);

            }

            if (bean.score != null) {
                util.scorestar.setScore(bean.score);
            }
            util.btn_zixun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyPingjiaActivity.this, DoctorDetailActivity.class);
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

            util.tv_time.setText(bean.createTime);

            return convertView;
        }


        class Util {
            View ll_all;
            com.taisheng.now.view.StarGrade starGrade;
            TextView tv_content;
            SimpleDraweeView sdv_header;
            TextView tv_doctor_name;
            TextView tv_title;
            TextView tv_times;
            DoctorLabelWrapLayout dlwl_doctor_label;
            ScoreStar scorestar;
            TextView btn_zixun;

            TextView tv_time;
        }
    }
}
