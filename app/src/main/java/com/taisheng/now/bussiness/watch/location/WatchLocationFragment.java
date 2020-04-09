package com.taisheng.now.bussiness.watch.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchBean;
import com.taisheng.now.bussiness.watch.bean.result.NewLocationBean;
import com.taisheng.now.bussiness.watch.watchfirst.HistoryGuijiActivity;
import com.taisheng.now.bussiness.watch.watchfirst.WatchFirstAnQuanWeiLanActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.map.NewMapInstance;
import com.th.j.commonlibrary.utils.TextsUtils;

import retrofit2.Call;
import retrofit2.Response;

public class WatchLocationFragment extends BaseFragment {


    ImageView iv_back;
//    View iv_shezhi;

    View iv_dianziweilan;
    View iv_dingwei;
    View iv_shebei_location;

    private MapView mMapView = null;

    View iv_guiji;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_watchlocation, container, false);
        initView(rootView);

        return rootView;
    }


    void initView(View rootView) {
        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        iv_shezhi = rootView.findViewById(R.id.iv_shezhi);
//        iv_shezhi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), WatchFirstAnQuanWeiLanActivity.class);
//                startActivity(intent);
//            }
//        });
        iv_dianziweilan = rootView.findViewById(R.id.iv_dianziweilan);
        iv_dianziweilan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchFirstAnQuanWeiLanActivity.class);
                startActivity(intent);
            }
        });

        iv_guiji = rootView.findViewById(R.id.iv_guiji);
        iv_guiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryGuijiActivity.class);
                startActivity(intent);
            }
        });
        //获取地图控件引用
        mMapView = (MapView) rootView.findViewById(R.id.bmapView);
        NewMapInstance.getInstance().init(mMapView);

        iv_dingwei = rootView.findViewById(R.id.iv_dingwei);
        iv_dingwei.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewMapInstance.getInstance().startLoc();
            }
        });

        iv_shebei_location = rootView.findViewById(R.id.iv_shebei_location);
        iv_shebei_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseWatchBean bean = new BaseWatchBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                ApiUtils.getApiService_hasdialog().getNewPosition(bean).enqueue(new TaiShengCallback<BaseBean<NewLocationBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<NewLocationBean>> response, BaseBean<NewLocationBean> message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                NewLocationBean newLocationBean = message.result;
                                if (!TextsUtils.isEmpty(newLocationBean.latitude) && !TextsUtils.isEmpty(newLocationBean.latitude)) {
                                    LatLng latLng = new LatLng(Double.parseDouble(newLocationBean.latitude), Double.parseDouble(newLocationBean.longitude));
                                    NewMapInstance.shebeiLatLng = NewMapInstance.converterLatLng(latLng);
                                    NewMapInstance.getInstance().refreshMap();
                                    NewMapInstance.getInstance().setWatchCenter();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean<NewLocationBean>> call, Throwable t) {

                    }
                });
            }
        });

        BaseWatchBean bean = new BaseWatchBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService_hasdialog().getNewPosition(bean).enqueue(new TaiShengCallback<BaseBean<NewLocationBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<NewLocationBean>> response, BaseBean<NewLocationBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        NewLocationBean newLocationBean = message.result;
                        if (TextsUtils.isInteger(newLocationBean.latitude) && TextsUtils.isInteger(newLocationBean.latitude)) {
                            LatLng latLng = new LatLng(Double.parseDouble(newLocationBean.latitude), Double.parseDouble(newLocationBean.longitude));
                            NewMapInstance.shebeiLatLng = NewMapInstance.converterLatLng(latLng);
                            NewMapInstance.getInstance().refreshMap();
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<NewLocationBean>> call, Throwable t) {

            }
        });
        NewMapInstance.getInstance().startLoc();
    }


    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
//        NewMapInstance.getInstance().stopLocListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

}
