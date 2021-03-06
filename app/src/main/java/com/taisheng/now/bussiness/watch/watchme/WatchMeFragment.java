package com.taisheng.now.bussiness.watch.watchme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.RebootPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Uiutils;
import com.th.j.commonlibrary.wight.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class WatchMeFragment extends BaseFragment {
    @BindView(R.id.sdv_header)
    CircleImageView sdvHeader;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_zhanghao)
    TextView tvZhanghao;
    @BindView(R.id.iv_jiantou)
    TextView ivJiantou;
    @BindView(R.id.ll_togerenxinxi)
    RelativeLayout llTogerenxinxi;
    @BindView(R.id.ll_tongxunlu)
    TextView llTongxunlu;
    @BindView(R.id.ll_naozhong)
    TextView llNaozhong;
    @BindView(R.id.ll_chiyao)
    TextView llChiyao;
    @BindView(R.id.ll_sos)
    TextView llSos;
    @BindView(R.id.ll_xueya)
    TextView llXueya;
    @BindView(R.id.ll_xinlv)
    TextView llXinlv;
    @BindView(R.id.ll_kaiguan)
    TextView llKaiguan;
    @BindView(R.id.ll_chongqi)
    TextView llChongqi;
    @BindView(R.id.ll_huifuchuchang)
    TextView llHuifuchuchang;

    View ll_xueyajiaozhun;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_watchme, container, false);
        ll_xueyajiaozhun = rootView.findViewById(R.id.ll_xueyajiaozhun);
        ll_xueyajiaozhun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), WatchMeXueyajiaozhunActivity.class);
                startActivity(intent);

            }
        });
        ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }


    @OnClick({R.id.ll_togerenxinxi, R.id.ll_tongxunlu, R.id.ll_naozhong, R.id.ll_chiyao, R.id.ll_sos, R.id.ll_xueya, R.id.ll_xinlv, R.id.ll_kaiguan, R.id.ll_chongqi, R.id.ll_huifuchuchang})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_togerenxinxi:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchMeMessageActivity.class);
                }
                break;
            case R.id.ll_tongxunlu:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchMeTongxunluActivity.class);
                }
                break;
            case R.id.ll_naozhong:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchMeNaozhongListActivity.class);
                }
                break;
            case R.id.ll_chiyao:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchChiYaoListActivity.class);
                }
                break;
            case R.id.ll_sos:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchMejinjilianxirenActivity.class);
                }
                break;
            case R.id.ll_xueya:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchMeXueyajingActivity.class);
                }
                break;
            case R.id.ll_xinlv:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchMeXinlvyujingActivity.class);
                }
                break;
            case R.id.ll_kaiguan:
                if (intent == null) {
                    intent = new Intent(getActivity(), WatchMeKaiGuanActivity.class);
                }
                break;
            case R.id.ll_chongqi:
                reStart();
                break;
            case R.id.ll_huifuchuchang:
                resumeFactory();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void resumeFactory() {
        RebootPostBean bean = new RebootPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService_hasdialog().restoreFactorySettings(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {

                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        Uiutils.showToast("正在恢复出厂设置");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean> call, Throwable t) {

            }
        });
    }


    private void reStart() {
        RebootPostBean bean = new RebootPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService_hasdialog().reboot(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {

                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        Uiutils.showToast("正在重启！");
                        break;
                }

            }

            @Override
            public void onFail(Call<BaseBean> call, Throwable t) {

            }
        });
    }


    private void initData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getActivity())
                .load(Constants.Url.File_Host + WatchInstance.getInstance().headUrl)
                .apply(new RequestOptions().placeholder(R.drawable.article_default)
                        .error(R.drawable.article_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(sdvHeader);
        if (!TextUtils.isEmpty(WatchInstance.getInstance().deviceNickName)) {
            tvNickname.setText(WatchInstance.getInstance().deviceNickName);
        }
        if (!TextUtils.isEmpty(WatchInstance.getInstance().deviceId)) {
            tvZhanghao.setText(WatchInstance.getInstance().deviceId);
        }
    }
}
