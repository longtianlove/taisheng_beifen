package com.taisheng.now.bussiness.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.result.ConsultBean;
import com.taisheng.now.bussiness.bean.result.ConsultListResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class ZixunjiluActivity extends BaseHActivity {


    @BindView(R.id.lv_doctors)
    TaishengListView lvDoctors;
    @BindView(R.id.ptr_refresh)
    MaterialDesignPtrFrameLayout ptrRefresh;
    private ConsultAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;

    @Override
    public void initView() {
        setContentView(R.layout.activity_zixunjilu);
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
        getconsultList_first();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.advisory_record));
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
                getconsultList();
            }
        });
        lvDoctors = (TaishengListView) findViewById(R.id.lv_doctors);
        madapter = new ConsultAdapter(this);
        lvDoctors.setAdapter(madapter);
        lvDoctors.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getconsultList();
            }
        });
    }

    void getconsultList_first() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;

        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService_hasdialog().consultList(bean).enqueue(new TaiShengCallback<BaseBean<ConsultListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ConsultListResultBean>> response, BaseBean<ConsultListResultBean> message) {
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
                                lvDoctors.setLoadAllViewText("暂时只有这么多咨询");
                                lvDoctors.setLoadAllFooterVisible(true);
                            } else {
                                lvDoctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvDoctors.setHasLoadMore(false);
                            lvDoctors.setLoadAllViewText("暂时只有这么多咨询");
                            lvDoctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ConsultListResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });

    }
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
                                lvDoctors.setLoadAllViewText("暂时只有这么多咨询");
                                lvDoctors.setLoadAllFooterVisible(true);
                            } else {
                                lvDoctors.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvDoctors.setHasLoadMore(false);
                            lvDoctors.setLoadAllViewText("暂时只有这么多咨询");
                            lvDoctors.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ConsultListResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
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
            Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
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
