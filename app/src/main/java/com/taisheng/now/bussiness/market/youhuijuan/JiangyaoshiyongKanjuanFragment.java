package com.taisheng.now.bussiness.market.youhuijuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.bean.post.KanjuanPostBean;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanBean;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanResultBanner;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.dingdan.DingdanjiesuanActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.TaishengListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class JiangyaoshiyongKanjuanFragment extends BaseFragment {

    public String assessmentType;


    //    MaterialDesignPtrFrameLayout ptr_refresh;
    TaishengListView list_kajuan;

    YouhuiquanAdapter madapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_jiangyaoshiyongkajuan, container, false);
        initView(rootView);
        initData();
        return rootView;
    }

    void initView(View rootView) {
//        ptr_refresh = (MaterialDesignPtrFrameLayout) rootView.findViewById(R.id.ptr_refresh);
//        /**
//         * 下拉刷新
//         */
//        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                PAGE_NO = 1;
//                getDoctors();
//
//            }
//        });
        list_kajuan = (TaishengListView) rootView.findViewById(R.id.list_kajuan);


        madapter = new YouhuiquanAdapter(getContext());
        list_kajuan.setAdapter(madapter);
        list_kajuan.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });

    }

    void initData() {
        getDoctors();
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getDoctors() {
        KanjuanPostBean bean = new KanjuanPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = 10;
        switch (assessmentType) {
            case "1":
                bean.status = "0";
                break;
            case "2":
                bean.status = "2";
                break;
            case "3":
                bean.status = "1";
                break;

        }
        DialogUtil.showProgress(getActivity(), "");

        ApiUtils.getApiService().getCouponlist(bean).enqueue(new TaiShengCallback<BaseBean<MallYouhuiquanResultBanner>>() {
            @Override
            public void onSuccess(Response<BaseBean<MallYouhuiquanResultBanner>> response, BaseBean<MallYouhuiquanResultBanner> message) {
//                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            list_kajuan.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
//                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                list_kajuan.setHasLoadMore(false);
                                list_kajuan.setLoadAllViewText("暂时只有这么多优惠券");
                                list_kajuan.setLoadAllFooterVisible(true);
                            } else {
                                list_kajuan.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            list_kajuan.setHasLoadMore(false);
                            list_kajuan.setLoadAllViewText("暂时只有这么多优惠券");
                            list_kajuan.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<MallYouhuiquanResultBanner>> call, Throwable t) {
//                ptr_refresh.refreshComplete();
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
                convertView = inflater.inflate(R.layout.item_jiangyaoshiyongyouhuijuan, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_discount = convertView.findViewById(R.id.tv_discount);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_tag = convertView.findViewById(R.id.tv_tag);
                util.tv_usedate = convertView.findViewById(R.id.tv_usedate);
                convertView.setTag(util);
            } else {
                util = (YouhuiquanAdapter.Util) convertView.getTag();
            }
            MallYouhuiquanBean bean = mData.get(position);
            Util finalUtil = util;
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ("1".equals(assessmentType)) {

                        if(bean.min.compareTo(new BigDecimal(DingdanInstance.getInstance().zongjia))==1){
                            ToastUtil.showAtCenter("不可使用");
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("tv_discount", bean.discount);
                        DingdanInstance.getInstance().tv_discount=bean.discount+"";
                        DingdanInstance.getInstance().couponId=bean.id;
                        getActivity().setResult(2, intent);
                        getActivity().finish();
                    }


                }
            });
            util.tv_discount.setText("¥"+bean.discount + "");
            util.tv_name.setText(bean.name);
            util.tv_tag.setText(bean.tag);
            util.tv_usedate.setText(bean.useDate);

            return convertView;
        }


        class Util {
            View ll_all;

            TextView tv_discount;
            TextView tv_name;
            TextView tv_tag;
            TextView tv_usedate;
        }
    }
}
