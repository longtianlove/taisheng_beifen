package com.taisheng.now.bussiness.watch.watchfirst;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import butterknife.ButterKnife;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.map.HistoryGuijiMapInstance;
import com.taisheng.now.map.TrackInstance;

import java.util.ArrayList;

/**
 * Created by dragon on 2019/6/29.
 */

public class HistoryGuijiActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    View iv_dingwei;

    private MapView mMapView = null;


    @Override
    public void initView() {
        setContentView(R.layout.activity_watchhistoryguiji);
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
        tvTitle.setText(getString(R.string.locus_map));
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setBackgroundResource(R.drawable.icon_shezhi);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryGuijiActivity.this, WatchFirstAnQuanWeiLanActivity.class);
                startActivity(intent);
            }
        });
    }

   private void initViews() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        HistoryGuijiMapInstance.getInstance().init(mMapView);
        TrackInstance.getInstance().queryHistoryTrack(this, mMapView.getMap());
        iv_dingwei = findViewById(R.id.iv_dingwei);
        iv_dingwei.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HistoryGuijiMapInstance.getInstance().startLoc();
            }
        });

    }

    ArrayList<LatLng> routeList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
//        NewMapInstance.getInstance().startLoc();

//        GuijiPostBean bean = new GuijiPostBean();
//        bean.userId = UserInstance.getInstance().getUid();
//        bean.token = UserInstance.getInstance().getToken();
//        bean.deviceId = WatchInstance.getInstance().deviceId;
//        ApiUtils.getApiService().getWatchUdList(bean).enqueue(new TaiShengCallback<BaseBean<GuijiResultBean>>() {
//            @Override
//            public void onSuccess(Response<BaseBean<GuijiResultBean>> response, BaseBean<GuijiResultBean> message) {
//                switch (message.code) {
//                    case Constants.HTTP_SUCCESS:
//
////                        if(message.result!=null) {
////                            String jsonRecordsString = message.result.toString();
////                            ArrayList<GuijiBean> records = (ArrayList<GuijiBean>) JSONArray.parseArray(jsonRecordsString, GuijiBean.class);
////
////                            if (records != null && records.size() > 0) {
////                                routeList.clear();
////                                for (GuijiBean bean :records) {
////                                    LatLng temp = new LatLng(Double.parseDouble(bean.latitude), Double.parseDouble(bean.longitude));
////                                    routeList.add(NewMapInstance.converterLatLng(temp));
////
////                                }
////
////                                NewMapInstance.getInstance().addRouteLine(routeList);
////                                NewMapInstance.getInstance().initStartMarker(routeList.get(0));
////                                NewMapInstance.getInstance().initendMarker(routeList.get(routeList.size() - 1));
////                            }
////                        }
//
//                        if (message.result.records != null && message.result.records.size() > 0) {
//                            routeList.clear();
//                            for (GuijiBean bean : message.result.records) {
//                                LatLng temp = new LatLng(Double.parseDouble(bean.latitude), Double.parseDouble(bean.longitude));
//                                    routeList.add(NewMapInstance.converterLatLng(temp));
//                            }
//                            NewMapInstance.getInstance().routeList=routeList;
//                            NewMapInstance.getInstance().refreshMap();
////                            NewMapInstance.getInstance().addRouteLine(routeList);
////                            NewMapInstance.getInstance().initStartMarker(routeList.get(0));
////                            NewMapInstance.getInstance().initendMarker(routeList.get(routeList.size()-1));
//                        }
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<GuijiResultBean>> call, Throwable t) {
//
//            }
//        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
//        NewMapInstance.getInstance().stopLocListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }


}
