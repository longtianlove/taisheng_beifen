package com.taisheng.now.bussiness.me;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.result.ConsultBean;
import com.taisheng.now.bussiness.bean.result.ConsultListResultBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
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

public class ZixunjiluActivity extends BaseActivity {
    View iv_back;

    MaterialDesignPtrFrameLayout ptr_refresh;

    TaishengListView lv_doctors;
    ConsultAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixunjilu);
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
        lv_doctors = (TaishengListView) findViewById(R.id.lv_doctors);
        madapter = new ConsultAdapter(this);
        lv_doctors.setAdapter(madapter);
        lv_doctors.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getconsultList();
            }
        });
    }

    void initData() {

        PAGE_NO = 1;
        PAGE_SIZE = 10;
        getconsultList();
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;
    String nickName;

    void getconsultList() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;

        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().consultList(bean).enqueue(new TaiShengCallback<BaseBean<ConsultListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ConsultListResultBean>> response, BaseBean<ConsultListResultBean> message) {
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
                                lv_doctors.setLoadAllViewText("暂时只有这么多咨询");
                                lv_doctors.setLoadAllFooterVisible(true);
                            } else {
                                lv_doctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_doctors.setHasLoadMore(false);
                            lv_doctors.setLoadAllViewText("暂时只有这么多咨询");
                            lv_doctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ConsultListResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });

    }

    class ConsultAdapter extends BaseAdapter {

        public Context mcontext;

        List<ConsultBean> mData = new ArrayList<ConsultBean>();

        public ConsultAdapter(Context context) {
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
            ConsultAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new ConsultAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_consult, null);
                util.tv_doctor_name = (TextView) convertView.findViewById(R.id.tv_doctor_name);
                util.tv_createTime = (TextView) convertView.findViewById(R.id.tv_createTime);
                util.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            ConsultBean bean = mData.get(position);
            util.tv_doctor_name.setText(bean.doctorRealName);
            util.tv_createTime.setText(bean.createTime);
            util.tv_content.setText(bean.suggestion);
            return convertView;
        }


        class Util {
            TextView tv_doctor_name;
            TextView tv_createTime;
            TextView tv_content;


        }
    }

}
