package com.taisheng.now.bussiness.market.youhuijuan;

import android.content.Context;
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
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.LingqukajuanPostBean;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanBean;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanResultBanner;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;
import com.th.j.commonlibrary.utils.TextsUtils;

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

public class MoreYouhuijuanActivity extends BaseHActivity {

    @BindView(R.id.lv_youhuijuans)
    TaishengListView lvYouhuijuans;
    @BindView(R.id.ptr_refresh)
    MaterialDesignPtrFrameLayout ptrRefresh;
    private YouhuiquanAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;

    @Override
    public void initView() {
        setContentView(R.layout.activity_moreyouhuijuan);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.coupon_area));
    }

    private void initViews() {

        /**
         * 下拉刷新
         */
        ptrRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                getDoctors();

            }
        });

        madapter = new YouhuiquanAdapter(this);
        lvYouhuijuans.setAdapter(madapter);
        lvYouhuijuans.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });
        getDoctors();

    }


    private void getDoctors() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = 10;
        DialogUtil.showProgress(this, "");

        ApiUtils.getApiService().couponlist(bean).enqueue(new TaiShengCallback<BaseBean<MallYouhuiquanResultBanner>>() {
            @Override
            public void onSuccess(Response<BaseBean<MallYouhuiquanResultBanner>> response, BaseBean<MallYouhuiquanResultBanner> message) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvYouhuijuans.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvYouhuijuans.setHasLoadMore(false);
                                lvYouhuijuans.setLoadAllViewText("暂时只有这么多优惠券");
                                lvYouhuijuans.setLoadAllFooterVisible(true);
                            } else {
                                lvYouhuijuans.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvYouhuijuans.setHasLoadMore(false);
                            lvYouhuijuans.setLoadAllViewText("暂时只有这么多优惠券");
                            lvYouhuijuans.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<MallYouhuiquanResultBanner>> call, Throwable t) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });


    }


    class YouhuiquanAdapter extends BaseAdapter {

        public Context mcontext;

        List<MallYouhuiquanBean> mData = new ArrayList<MallYouhuiquanBean>();

        public YouhuiquanAdapter(Context context) {
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
                convertView = inflater.inflate(R.layout.item_youhuijuan, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_discount = convertView.findViewById(R.id.tv_discount);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_tag = convertView.findViewById(R.id.tv_tag);
                util.tv_usedate = convertView.findViewById(R.id.tv_usedate);
                util.tv_lingqu = convertView.findViewById(R.id.tv_lingqu);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            MallYouhuiquanBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if (!TextsUtils.isEmpty(bean.discount + "")) {
                util.tv_discount.setText(mcontext.getString(R.string.mony_code) + bean.discount + "");
            } else {
                util.tv_discount.setText(mcontext.getString(R.string.mony_code) + "0");
            }

            util.tv_name.setText(bean.name);
            util.tv_tag.setText(bean.tag);
            util.tv_usedate.setText(bean.useDate);
            util.tv_lingqu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LingqukajuanPostBean bean1 = new LingqukajuanPostBean();
                    bean1.userId = UserInstance.getInstance().getUid();
                    bean1.token = UserInstance.getInstance().getToken();
                    bean1.id = bean.id;
                    ApiUtils.getApiService().getCoupon(bean1).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    getDoctors();
                                    ToastUtil.showAtCenter(message.message);
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
                }
            });
            return convertView;
        }


        class Util {
            View ll_all;

            TextView tv_discount;
            TextView tv_name;
            TextView tv_tag;
            TextView tv_usedate;
            View tv_lingqu;
        }
    }
}
