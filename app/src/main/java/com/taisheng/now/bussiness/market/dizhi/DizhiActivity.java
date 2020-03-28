package com.taisheng.now.bussiness.market.dizhi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class DizhiActivity extends BaseIvActivity {

    @BindView(R.id.lv_dizhis)
    TaishengListView lvDizhis;
    @BindView(R.id.ptr_refresh)
    MaterialDesignPtrFrameLayout ptrRefresh;
    @BindView(R.id.tv_adddizhi)
    TextView tvAdddizhi;
    private DizhiAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;

    @Override
    public void initView() {
        setContentView(R.layout.activity_dizhi);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initData() {
        initDatas();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.receiving_address));
    }


    @OnClick({R.id.tv_adddizhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_adddizhi:
                DingdanInstance.getInstance().fromDizhi = "1";
                Intent intent = new Intent(DizhiActivity.this, DizhiBianjiActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void initViews() {

        /**
         * 下拉刷新
         */
        ptrRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                initData();

            }
        });

        madapter = new DizhiAdapter(this);
        lvDizhis.setAdapter(madapter);
        lvDizhis.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                PAGE_NO = 1;
                initData();
            }
        });
    }


    private void initDatas() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = 10;
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().addressList(bean).enqueue(new TaiShengCallback<BaseBean<DizhilistResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DizhilistResultBean>> response, BaseBean<DizhilistResultBean> message) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvDizhis.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvDizhis.setHasLoadMore(false);
                                lvDizhis.setLoadAllViewText("暂时只有这么多地址");
                                lvDizhis.setLoadAllFooterVisible(true);
                            } else {
                                lvDizhis.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvDizhis.setHasLoadMore(false);
                            lvDizhis.setLoadAllViewText("暂时只有这么多地址");
                            lvDizhis.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DizhilistResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });
    }

    class DizhiAdapter extends BaseAdapter {

        public Context mcontext;

        List<DizhilistBean> mData = new ArrayList<DizhilistBean>();

        public DizhiAdapter(Context context) {
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
                convertView = inflater.inflate(R.layout.item_dizhi, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_phone = convertView.findViewById(R.id.tv_phone);
                util.tv_address = convertView.findViewById(R.id.tv_address);
                util.tv_ismdefault = convertView.findViewById(R.id.tv_ismdefault);
                util.tv_bianji = convertView.findViewById(R.id.tv_bianji);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            DizhilistBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    DingdanInstance.getInstance().name = bean.name;
                    DingdanInstance.getInstance().phone = bean.phone;
                    DingdanInstance.getInstance().address = bean.province + bean.city + bean.county + bean.addressDetail;

                    intent.putExtra("name", bean.name);
                    intent.putExtra("phone", bean.phone);
                    intent.putExtra("address", bean.province + bean.city + bean.county + bean.addressDetail);
                    DingdanInstance.getInstance().addressId = bean.id;
                    setResult(1, intent);
                    finish();
                }
            });
            util.tv_name.setText(bean.name);
            util.tv_phone.setText(bean.phone);
            util.tv_address.setText(bean.province + bean.city + bean.county + bean.addressDetail);
            if (bean.defaultAddress == 1) {
                util.tv_ismdefault.setVisibility(View.VISIBLE);
            } else {
                util.tv_ismdefault.setVisibility(View.GONE);
            }

            util.tv_bianji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DizhiActivity.this, DizhiBianjiActivity.class);
                    intent.putExtra("name", bean.name);
                    intent.putExtra("phone", bean.phone);
                    intent.putExtra("province", bean.province);
                    intent.putExtra("city", bean.city);
                    intent.putExtra("county", bean.county);
                    intent.putExtra("xiangxidizhi", bean.addressDetail);
                    intent.putExtra("defaultAddress", bean.defaultAddress + "");
                    intent.putExtra("dizhiid", bean.id);
                    startActivity(intent);
                }
            });


            return convertView;
        }


        class Util {
            View ll_all;
            TextView tv_name;
            TextView tv_phone;
            TextView tv_address;
            View tv_ismdefault;
            View tv_bianji;


        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        PAGE_NO = 1;
    }
}
