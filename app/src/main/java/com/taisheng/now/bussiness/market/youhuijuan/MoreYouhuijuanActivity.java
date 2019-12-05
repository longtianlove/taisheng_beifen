package com.taisheng.now.bussiness.market.youhuijuan;

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
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.LingqukajuanPostBean;
import com.taisheng.now.bussiness.bean.post.RecommendDoctorPostBean;
import com.taisheng.now.bussiness.bean.result.CainixihuanResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorsResultBean;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanBean;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanResultBanner;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.doctor.DoctorFragment;
import com.taisheng.now.bussiness.me.FuwuxieyiActivity;
import com.taisheng.now.bussiness.me.YisixieyiActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
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

public class MoreYouhuijuanActivity extends BaseActivity {
    View iv_back;

    MaterialDesignPtrFrameLayout ptr_refresh;
    TaishengListView lv_youhuijuans;

    YouhuiquanAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreyouhuijuan);
        initView();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ptr_refresh = (MaterialDesignPtrFrameLayout) findViewById(R.id.ptr_refresh);

        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                getDoctors();

            }
        });


        lv_youhuijuans = (TaishengListView) findViewById(R.id.lv_youhuijuans);
        madapter = new YouhuiquanAdapter(this);
        lv_youhuijuans.setAdapter(madapter);
        lv_youhuijuans.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });
        getDoctors();

    }




    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getDoctors() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo=PAGE_NO;
        bean.pageSize=10;
        DialogUtil.showProgress(this, "");

        ApiUtils.getApiService().couponlist(bean).enqueue(new TaiShengCallback<BaseBean<MallYouhuiquanResultBanner>>() {
            @Override
            public void onSuccess(Response<BaseBean<MallYouhuiquanResultBanner>> response, BaseBean<MallYouhuiquanResultBanner> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_youhuijuans.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_youhuijuans.setHasLoadMore(false);
                                lv_youhuijuans.setLoadAllViewText("暂时只有这么多优惠券");
                                lv_youhuijuans.setLoadAllFooterVisible(true);
                            } else {
                                lv_youhuijuans.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_youhuijuans.setHasLoadMore(false);
                            lv_youhuijuans.setLoadAllViewText("暂时只有这么多优惠券");
                            lv_youhuijuans.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<MallYouhuiquanResultBanner>> call, Throwable t) {
                ptr_refresh.refreshComplete();
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
            YouhuiquanAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new YouhuiquanAdapter.Util();
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
                util = (YouhuiquanAdapter.Util) convertView.getTag();
            }
            MallYouhuiquanBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            util.tv_discount.setText(bean.discount + "");
            util.tv_name.setText(bean.name);
            util.tv_tag.setText(bean.tag);
            util.tv_usedate.setText(bean.useDate);
            util.tv_lingqu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LingqukajuanPostBean bean1=new LingqukajuanPostBean();
                    bean1.userId=UserInstance.getInstance().getUid();
                    bean1.token=UserInstance.getInstance().getToken();
                    bean1.id=bean.id;
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
