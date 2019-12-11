package com.taisheng.now.bussiness.watch.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.GuijiPostBean;
import com.taisheng.now.bussiness.watch.bean.post.YuJingListPostBean;
import com.taisheng.now.bussiness.watch.bean.post.YujingxinxiSetYiduPostBean;
import com.taisheng.now.bussiness.watch.bean.result.GuijiBean;
import com.taisheng.now.bussiness.watch.bean.result.GuijiResultBean;
import com.taisheng.now.bussiness.watch.bean.result.YujingResultBean;
import com.taisheng.now.bussiness.watch.bean.result.Yujingbean;
import com.taisheng.now.bussiness.watch.watchfirst.HistoryGuijiActivity;
import com.taisheng.now.bussiness.watch.watchfirst.WatchFirstAnQuanWeiLanActivity;
import com.taisheng.now.bussiness.watch.watchfirst.WatchFirstGuijiActivity;
import com.taisheng.now.bussiness.watch.watchme.WatchMeYujingxinxiXiangqingActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.map.NewMapInstance;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

public class WatchLocationFragment extends BaseFragment {


    ImageView iv_back;
//    View iv_shezhi;

    View iv_dianziweilan;
    View iv_dingwei;

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
        iv_dianziweilan=rootView.findViewById(R.id.iv_dianziweilan);
        iv_dianziweilan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WatchFirstAnQuanWeiLanActivity.class);
                startActivity(intent);
            }
        });

        iv_guiji=rootView.findViewById(R.id.iv_guiji);
        iv_guiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HistoryGuijiActivity.class);
                startActivity(intent);
            }
        });
        //获取地图控件引用
        mMapView = (MapView)rootView.findViewById(R.id.bmapView);
        NewMapInstance.getInstance().init(mMapView);

        iv_dingwei = rootView.findViewById(R.id.iv_dingwei);
        iv_dingwei.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewMapInstance.getInstance().startLoc();
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
