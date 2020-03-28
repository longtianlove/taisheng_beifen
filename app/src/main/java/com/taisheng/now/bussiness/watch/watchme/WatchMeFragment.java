package com.taisheng.now.bussiness.watch.watchme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.WatchsListActivity;
import com.taisheng.now.bussiness.watch.bean.post.RebootPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class WatchMeFragment extends BaseFragment {
    View iv_back;
    View tv_change_device;
    View ll_togerenxinxi;
    SimpleDraweeView sdv_header;
    TextView tv_nickname;
    TextView tv_zhanghao;
    View tv_qiandao;


    View ll_tongxunlu;
    View ll_naozhong;
    View ll_chiyao;

    View ll_xinlv;
    View ll_xueya;
    View ll_kaiguan;


    View ll_chongqi;
    View ll_huifuchuchang;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_watchme, container, false);

        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();

        return rootView;
    }

    View.OnClickListener toMeMessageActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), WatchMeMessageActivity.class);
            startActivity(intent);
        }
    };

    void initView(View rootView) {
        iv_back = rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tv_change_device = rootView.findViewById(R.id.tv_change_device);
        tv_change_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchsListActivity.class);
                startActivity(intent);
            }
        });
        ll_togerenxinxi = rootView.findViewById(R.id.ll_togerenxinxi);
        ll_togerenxinxi.setOnClickListener(toMeMessageActivityListener);
        sdv_header = (SimpleDraweeView) rootView.findViewById(R.id.sdv_header);
        sdv_header.setOnClickListener(toMeMessageActivityListener);
        tv_zhanghao = rootView.findViewById(R.id.tv_zhanghao);
        tv_nickname = (TextView) rootView.findViewById(R.id.tv_nickname);
        tv_nickname.setOnClickListener(toMeMessageActivityListener);
        tv_qiandao = rootView.findViewById(R.id.tv_qiandao);
        tv_qiandao.setOnClickListener(toMeMessageActivityListener);

        ll_tongxunlu=rootView.findViewById(R.id.ll_tongxunlu);
        ll_tongxunlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchMeTongxunluActivity.class);
                startActivity(intent);
            }
        });


        ll_naozhong = rootView.findViewById(R.id.ll_naozhong);
        ll_naozhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchMeNaozhongListActivity.class);
                startActivity(intent);
            }
        });
        ll_chiyao = rootView.findViewById(R.id.ll_chiyao);
        ll_chiyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchChiYaoListActivity.class);
                startActivity(intent);
            }
        });
        ll_xinlv = rootView.findViewById(R.id.ll_xinlv);
        ll_xinlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchMeXinlvyujingActivity.class);
                startActivity(intent);
            }
        });

        ll_xueya = rootView.findViewById(R.id.ll_xueya);
        ll_xueya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchMeXueyajingActivity.class);
                startActivity(intent);
            }
        });
        ll_kaiguan = rootView.findViewById(R.id.ll_kaiguan);
        ll_kaiguan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchMeKaiGuanActivity.class);
                startActivity(intent);
            }
        });

        ll_chongqi = rootView.findViewById(R.id.ll_chongqi);
        ll_chongqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RebootPostBean bean=new RebootPostBean();
                bean.userId=UserInstance.getInstance().getUid();
                bean.token=UserInstance.getInstance().getToken();
                bean.deviceId =WatchInstance.getInstance().deviceId;
                ApiUtils.getApiService().reboot(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {

                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("正在重启！");
                                break;
                        }

                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        ll_huifuchuchang = rootView.findViewById(R.id.ll_huifuchuchang);
        ll_huifuchuchang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RebootPostBean bean=new RebootPostBean();
                bean.userId=UserInstance.getInstance().getUid();
                bean.token=UserInstance.getInstance().getToken();
                bean.deviceId =WatchInstance.getInstance().deviceId;
                ApiUtils.getApiService().restoreFactorySettings(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {

                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("正在恢复出厂设置！");
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });

    }

    void initData() {


    }

    @Override
    public void onStart() {
        super.onStart();
        if (WatchInstance.getInstance().headUrl != null) {
            Uri uri = Uri.parse(Constants.Url.File_Host + WatchInstance.getInstance().headUrl);
            sdv_header.setImageURI(uri);
        }
        if (!TextUtils.isEmpty(WatchInstance.getInstance().deviceNickName)) {
            tv_nickname.setText(WatchInstance.getInstance().deviceNickName);
        }
        if (!TextUtils.isEmpty(WatchInstance.getInstance().deviceId)) {
            tv_zhanghao.setText(WatchInstance.getInstance().deviceId);
        }
    }

    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
