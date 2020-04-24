package com.taisheng.now.bussiness.watch.watchme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.HealthCheckListPostBean;
import com.taisheng.now.bussiness.bean.result.CheckHistoryBean;
import com.taisheng.now.bussiness.bean.result.CheckHistoryResultBean;
import com.taisheng.now.bussiness.healthfiles.HealthCheckResultHistoryActivity;
import com.taisheng.now.bussiness.healthfiles.ZhongyitizhiFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchListPostBean;
import com.taisheng.now.bussiness.watch.bean.post.XueyajiaozhunBean;
import com.taisheng.now.bussiness.watch.bean.result.XinlvXueyaYujingBean;
import com.taisheng.now.bussiness.watch.bean.result.XueyajiaozhuAllResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XueyajiaozhunResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.taisheng.now.view.TaishengListView;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeXueyajiaozhunActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_xinlvpingzuidazhi)
    EditText tvXinlvpingzuidazhi;
    @BindView(R.id.tv_xinlvpingzuixiaozhi)
    EditText tvXinlvpingzuixiaozhi;
    @BindView(R.id.tv_save)
    TextView tvSave;
    TaishengListView lv_history;
    CheckHistoryAdapter madapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_xueyajz);
        lv_history = findViewById(R.id.lv_history);
        madapter = new CheckHistoryAdapter(this);
        lv_history.setAdapter(madapter);
        lv_history.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getHistoryMore();
            }
        });
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        PAGE_NO = 1;
        BaseWatchListPostBean bean = new BaseWatchListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;

        ApiUtils.getApiService_hasdialog().jiaozhunLishi(bean).enqueue(new TaiShengCallback<BaseBean<XueyajiaozhuAllResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XueyajiaozhuAllResultBean>> response, BaseBean<XueyajiaozhuAllResultBean> message) {
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_history.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 20 && message.result.records.size() > 0) {
                                lv_history.setHasLoadMore(false);
                                lv_history.setLoadAllViewText("暂时只有这么多结果");
                                lv_history.setLoadAllFooterVisible(true);
                            } else {
                                lv_history.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XueyajiaozhuAllResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();
            }
        });
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText("血压校准");
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 20;

    void getHistoryMore() {
        BaseWatchListPostBean bean = new BaseWatchListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        ApiUtils.getApiService_hasdialog().jiaozhunLishi(bean).enqueue(new TaiShengCallback<BaseBean<XueyajiaozhuAllResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<XueyajiaozhuAllResultBean>> response, BaseBean<XueyajiaozhuAllResultBean> message) {
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_history.setLoading(false);

                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 20) {
                                lv_history.setHasLoadMore(false);
                                lv_history.setLoadAllViewText("暂时只有这么多结果");
                                lv_history.setLoadAllFooterVisible(true);
                            } else {
                                lv_history.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_history.setHasLoadMore(false);
                            lv_history.setLoadAllViewText("暂时只有这么多结果");
                            lv_history.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<XueyajiaozhuAllResultBean>> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                if (TextsUtils.isEmpty(TextsUtils.getTexts(tvXinlvpingzuidazhi)) || TextsUtils.isEmpty(TextsUtils.getTexts(tvXinlvpingzuixiaozhi))) {
                    Uiutils.showToast(getString(R.string.please_input));
                    return;
                }
                if (Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuidazhi)) < Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuixiaozhi))) {
                    Uiutils.showToast("高压不能小于低压");
                    return;
                }
                XueyajiaozhunBean bean = new XueyajiaozhunBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.bloodPressureHigh = Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuidazhi));
                bean.bloodPressureLow = Integer.parseInt(TextsUtils.getTexts(tvXinlvpingzuixiaozhi));

                ApiUtils.getApiService_hasdialog().calibration(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                initData();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
                break;
        }
    }


    class CheckHistoryAdapter extends BaseAdapter {

        public Context mcontext;

        List<XueyajiaozhunResultBean> mData = new ArrayList<XueyajiaozhunResultBean>();

        public CheckHistoryAdapter(Context context) {
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
            CheckHistoryAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new CheckHistoryAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_xueyajiaozhunlishi, null);
                util.tv_gaoya = convertView.findViewById(R.id.tv_gaoya);
                util.tv_diya = convertView.findViewById(R.id.tv_diya);
                util.tv_createTime = convertView.findViewById(R.id.tv_createTime);
                convertView.setTag(util);
            } else {
                util = (CheckHistoryAdapter.Util) convertView.getTag();
            }
            XueyajiaozhunResultBean bean = mData.get(position);
            util.tv_gaoya.setText(bean.bloodPressureHigh + "");
            util.tv_diya.setText(bean.bloodPressureLow + "");
            util.tv_createTime.setText(bean.createTime);

            return convertView;
        }


        class Util {

            TextView tv_gaoya;
            TextView tv_diya;
            TextView tv_createTime;

        }
    }
}
